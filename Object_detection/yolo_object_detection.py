
import cv2
import numpy as np
import image_slicer

confThreshold = 0.8  #Confidence threshold
nmsThreshold = 0.4   #Non-maximum suppression threshold
inpWidth = 608     #Width of network's input image
inpHeight = 608      #Height of network's input image


# Load Yolo
net = cv2.dnn.readNet("yolov3_training_last.weights", "yolov3_training.cfg")


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
    cv2.rectangle(frame, (left, top), (right, bottom), (0, 0, 255), 4)

    label = '%.2f' % conf

    # Get the label for the class name and its confidence
    if classes:
        assert(classId < len(classes))
        label = '%s:%s' % (classes[classId], label)

    #Display the label at the top of the bounding box
    labelSize, baseLine = cv2.getTextSize(label, cv2.FONT_HERSHEY_SIMPLEX, 0.5, 1)
    top = max(top, labelSize[1])
    cv2.putText(frame, label, (left, top), cv2.FONT_HERSHEY_SIMPLEX, 2, COLORS[classId], 6)


# Remove the bounding boxes with low confidence using non-maxima suppression
def postprocess(frame, outs):

    frameHeight = frame.shape[0]
    frameWidth = frame.shape[1]

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
                #if classId not in classIds:
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

    #classIds = list(dict.fromkeys(classIds))



cap = cv2.VideoCapture(0)

while(True):

    # Capture frame-by-frame
    ret, frame = cap.read()

    if not ret:
        print("Error getting webcam feed")
        break

    cv2.imshow('frame', frame)

    k= cv2.waitKey(1)
    if k % 256 == 27:
        # ESC pressed
        print("Escape hit, closing...")
        break
    elif k % 256 == 32:
        # SPACE pressed
        detected_cards = list()
        print("Space pressed")
        frame = cv2.imread("./IMG_3694.JPG")
        cv2.imwrite("./split_images/image.png", frame)
        tiles = image_slicer.slice("./split_images/image.png", 4, True)
        print("Image has been split")

        for tile in tiles:

            frame = cv2.imread(tile.filename)
            blob = cv2.dnn.blobFromImage(frame, 1 / 255.0, (416, 416), (0, 0, 0), True, crop=False)

            net.setInput(blob)

            print("Processing: "+tile.filename)

            outs = net.forward(output_layers)

            postprocess(frame, outs)

            cv2.imwrite(tile.filename, frame)

        print("Joining images")
        tiles = image_slicer.open_images_in("D:\playing-card-detection-master\split_images")
        image = image_slicer.join(tiles)
        print("Saving final image")
        image.save("bund.png")
        print("Final image saved")

cap.release()
cv2.destroyAllWindows()

