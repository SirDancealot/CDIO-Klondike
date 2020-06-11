
import cv2
import numpy as np



confThreshold = 0.5  #Confidence threshold
nmsThreshold = 0.4   #Non-maximum suppression threshold
inpWidth = 288      #Width of network's input image
inpHeight = 288      #Height of network's input image


# Load Yolo
net = cv2.dnn.readNet("Kopi_af_yolov3_training_last.weights", "yolov3_training.cfg")


# Name custom object
classes = ["Ah","Kh","Qh","Jh","10h","9h","8h","7h","6h","5h","4h","3h","2h","Ad","Kd","Qd","Jd","10d","9d","8d","7d",
           "6d","5d","4d","3d","2d","Ac","Kc","Qc","Jc","10c","9c","8c","c7","c6","c5","c4","c3","c2","As","Ks","Qs",
           "Js","10s","9s","8s","7s","6s","5s","4s","3s","2s"]


layer_names = net.getLayerNames()
output_layers = [layer_names[i[0] - 1] for i in net.getUnconnectedOutLayers()]
COLORS = np.random.uniform(0, 255, size=(len(classes), 3))


# Draw the predicted bounding box
def drawPred(classId, conf, left, top, right, bottom):
    # Draw a bounding box.
    cv2.rectangle(frame, (left, top), (right, bottom), (0, 0, 255))

    label = '%.2f' % conf

    # Get the label for the class name and its confidence
    if classes:
        assert(classId < len(classes))
        label = '%s:%s' % (classes[classId], label)

    #Display the label at the top of the bounding box
    labelSize, baseLine = cv2.getTextSize(label, cv2.FONT_HERSHEY_SIMPLEX, 0.5, 1)
    top = max(top, labelSize[1])
    cv2.putText(frame, label, (left, top), cv2.FONT_HERSHEY_SIMPLEX, 0.5, (255,255,255))


# Remove the bounding boxes with low confidence using non-maxima suppression
def postprocess(frame, outs):

    frameHeight = frame.shape[0]
    frameWidth = frame.shape[1]

    classIds = []
    confidences = []
    boxes = []
    # Scan through all the bounding boxes output from the network and keep only the
    # ones with high confidence scores. Assign the box's class label as the class with the highest score.
    classIds = []
    confidences = []
    boxes = []

    for out in outs:
        for detection in out:
            scores = detection[5:]
            classId = np.argmax(scores)
            confidence = scores[classId]

            if confidence > confThreshold:
                center_x = int(detection[0] * frameWidth)
                center_y = int(detection[1] * frameHeight)
                width = int(detection[2] * frameWidth)
                height = int(detection[3] * frameHeight)
                left = int(center_x - width / 2)
                top = int(center_y - height / 2)
                classIds.append(classId)
                confidences.append(float(confidence))
                boxes.append([left, top, width, height])

    # Perform non maximum suppression to eliminate redundant overlapping boxes with
    # lower confidences.
    indices = cv2.dnn.NMSBoxes(boxes, confidences, confThreshold, nmsThreshold)
    for i in indices:
        i = i[0]
        box = boxes[i]
        left = box[0]
        top = box[1]
        width = box[2]
        height = box[3]
        drawPred(classIds[i], confidences[i], left, top, left + width, top + height)





cap = cv2.VideoCapture(0)

while(True):

    # Capture frame-by-frame
    _, frame = cap.read()


    # Our operations on the frame come here

    blob = cv2.dnn.blobFromImage(frame,1/255.0, (416, 416), (0, 0, 0), True, crop=False)

    net.setInput(blob)
    outs = net.forward(output_layers)

    postprocess(frame, outs)

    # Display the resulting frame
    cv2.imshow('frame',frame)
    if cv2.waitKey(1) & 0xFF == ord('q'):
        break



cv2.destroyAllWindows()




