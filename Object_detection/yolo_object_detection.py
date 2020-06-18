import cv2
import numpy as np
import image_slicer
import socket
import threading
import select

from Object_detection.encode import *

confThreshold = 0.8  # Confidence threshold
nmsThreshold = 0.4  # Non-maximum suppression threshold
inpWidth = 608  # Width of network's input image
inpHeight = 608  # Height of network's input image

HOST = "localhost"
PORT = 65432
s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)

print("socket created")
try:
    s.bind((HOST, PORT))
except socket.error as err:
    print('Bind failed. Error Code : ' .format(err))

s.listen(5)
print("socket listening")

# Load Yolo
net = cv2.dnn.readNet("yolov3_training_last.weights", "yolov3_training.cfg")

# Name custom object
classes = ["Ah", "Kh", "Qh", "Jh", "10h", "9h", "8h", "7h", "6h", "5h", "4h", "3h", "2h", "Ad", "Kd", "Qd", "Jd", "10d",
           "9d", "8d", "7d",
           "6d", "5d", "4d", "3d", "2d", "Ac", "Kc", "Qc", "Jc", "10c", "9c", "8c", "7c", "6c", "5c", "4c", "3c", "2c",
           "As", "Ks", "Qs",
           "Js", "10s", "9s", "8s", "7s", "6s", "5s", "4s", "3s", "2s"]

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
        assert (classId < len(classes))
        label = '%s:%s' % (classes[classId], label)

    # Display the label at the top of the bounding box
    labelSize, baseLine = cv2.getTextSize(label, cv2.FONT_HERSHEY_SIMPLEX, 0.5, 1)
    top = max(top, labelSize[1])
    cv2.putText(frame, label, (left, top), cv2.FONT_HERSHEY_SIMPLEX, 2, COLORS[classId], 6)


# Remove the bounding boxes with low confidence using non-maxima suppression
classIds_o = []
boxes_o = []


def postprocess(frame, outs, filename):
    _, _, y, x = filename.split("_")
    a, b = x.split(".")
    x = a
    frameHeight = frame.shape[0]
    frameWidth = frame.shape[1]

    # Scan through all the bounding boxes output from the network and keep only the
    # ones with high confidence scores. Assign the box's class label as the class with the highest score.
    confidences = []
    classIds = []
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
                if classId in classIds_o:
                    duplicates.append(classId)
                    duplicates.append(classId)
                    i = classIds_o.index(classId)
                    box1 = boxes_o[i]
                    box2 = [left, top, width, height, x, y]

                    if box1[4] == "02":
                        box1[0] += frameWidth
                    if box2[4] == "02":
                        box2[0] += frameWidth
                    if box1[5] == "02":
                        box1[1] += frameHeight
                    if box2[5] == "02":
                        box2[1] += frameHeight

                    duplicate_boxes.append(box1)
                    duplicate_boxes.append(box2)
                else:
                    box = [left, top, width, height, x, y]
                    if box[4] == "02":
                        box[0] += frameWidth
                    if box[5] == "02":
                        box[1] += frameHeight
                    singles.append((classId, box))
                classIds.append(classId)
                classIds_o.append(classId)
                confidences.append(float(confidence))
                boxes.append([left, top, width, height])
                boxes_o.append([left, top, width, height, x, y])

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

    # classIds = list(dict.fromkeys(classIds))


singles = []
duplicates = []
duplicate_boxes = []
lowest_y_box = None


def registrer_piles():
    gameState = GameState()
    global lowest_y_box
    if lowest_y_box is None:
        lowest_y_box = [0xffffffff, 0xffffffff]
        for box in duplicate_boxes:
            if box[1] < lowest_y_box[1]:
                lowest_y_box = box
    # print(lowest_y_box)
    box_height = int(lowest_y_box[3] * 1.25)
    box_height_range = range(lowest_y_box[1], lowest_y_box[1] + box_height)
    row_width = None
    row_x = []
    row_final = [[], [], [], [], [], [], []]
    row_final_top = []
    for i in range(0, len(duplicates), 2):
        l1 = duplicate_boxes[i][0] if duplicate_boxes[i][0] < duplicate_boxes[i + 1][0] else duplicate_boxes[i + 1][0]
        t1 = duplicate_boxes[i][1] if duplicate_boxes[i][1] < duplicate_boxes[i + 1][1] else duplicate_boxes[i + 1][1]
        l2 = duplicate_boxes[i][0] if duplicate_boxes[i][0] > duplicate_boxes[i + 1][0] else duplicate_boxes[i + 1][0]
        t2 = duplicate_boxes[i][1] if duplicate_boxes[i][1] > duplicate_boxes[i + 1][1] else duplicate_boxes[i + 1][1]
        row_width = int((l2 - l1) / 2)
        if True not in np.in1d(row_x, range(l1 - row_width, l1 + row_width)):
            row_x.append(l1)
        # cv2.rectangle(img, (l1, t1), (l2, t2), (255, 255, 255), 4)
    row_x.sort()
    # singles.sort(key=lambda tup: tup[1][1])
    for tup in singles:
        if tup[1][1] not in box_height_range:
            for i in range(len(row_x)):
                if row_x[i] in range(tup[1][0] - row_width, tup[1][0] + row_width):
                    row_final[i].append(tup)
        else:
            row_final_top.append(tup)
            pass

    row_count = 0
    for row in row_final:
        row.sort(key=lambda tup: tup[1][1])
        for i in range(len(row)):
            rank_suit = classes[row[i][0]]
            gameState.gameCards[row_count].append(class_to_card(rank_suit))

        row_count = row_count + 1

    row_final_top.sort(key=lambda tup: tup[1][0])
    for i in range(len(row_final_top)):
        rank_suit = classes[row_final_top[i][0]]
        if i == 0:
            gameState.shownStock = class_to_card(rank_suit)
        else:
            gameState.finalCards[i - 1] = class_to_card(rank_suit)

    s = encode_game(gameState)
    #print(s)
    #for c in s:
      #  print(bin(ord(c)))

    return s


def class_to_card(cls):
    if len(cls) == 3:
        val_c = cls[0:2]
        suit_c = cls[2]
    else:
        val_c = cls[0]
        suit_c = cls[1]

    v_switch = {
        '0': 0,
        'A': 1,
        '2': 2,
        '3': 3,
        '4': 4,
        '5': 5,
        '6': 6,
        '7': 7,
        '8': 8,
        '9': 9,
        '10': 10,
        'J': 11,
        'Q': 12,
        'K': 13,
    }
    val = v_switch[val_c]

    s_switch = {
        '_': 0,
        'h': 1,
        's': 2,
        'd': 3,
        'c': 4
    }
    suit = s_switch[suit_c]
    return Card(suit, val)


cap = cv2.VideoCapture(0)


while (True):


    # Capture frame-by-frame
    ret, frame = cap.read()

    if not ret:
        print("Error getting webcam feed")
        break

    cv2.imshow('frame', frame)

    k = cv2.waitKey(1)
    if k % 256 == 27:
        # ESC pressed
        print("Escape hit, closing...")
        break
    elif k % 256 == 32:
        # SPACE pressed
        detected_cards = list()
        print("Space pressed")
        frame = cv2.imread("./JPEG_20200618_154748.jpg")
        cv2.imwrite("./split_images/image.png", frame)
        tiles = image_slicer.slice("./split_images/image.png", 4, True)
        print("Image has been split")

        for tile in tiles:
            frame = cv2.imread(tile.filename)
            blob = cv2.dnn.blobFromImage(frame, 1 / 255.0, (416, 416), (0, 0, 0), True, crop=False)

            net.setInput(blob)

            print("Processing: " + tile.filename)

            outs = net.forward(output_layers)

            postprocess(frame, outs, tile.filename)

            cv2.imwrite(tile.filename, frame)

        print("Joining images")
        tiles = image_slicer.open_images_in("./split_images")
        image = image_slicer.join(tiles)
        print("Saving final image")
        image.save("sliced.png")
        image = cv2.imread("sliced.png")
        #image = registrer_piles(image)
        cv2.imwrite("final.png", image)
        print("Final image saved")

        conn, addr = s.accept()
        conn.send(registrer_piles().encode())
        conn.close()

        singles = []
        duplicates = []
        duplicate_boxes = []
        lowest_y_box = None


cap.release()
cv2.destroyAllWindows()
