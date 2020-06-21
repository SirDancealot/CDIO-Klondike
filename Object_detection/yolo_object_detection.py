import cv2
import numpy as np

from encode import *
from comm import Comm

confThreshold = 0.7  # Confidence threshold
nmsThreshold = 0.4  # Non-maximum suppression threshold
inpWidth = 608  # Width of network's input image
inpHeight = 608  # Height of network's input image

img_name = "./v7.jpg"
capture_from_webcam = True

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
def draw_pred(class_id, conf, left, top, right, bottom):
    # Draw a bounding box.
    cv2.rectangle(frame, (left, top), (right, bottom), (0, 0, 255), 4)

    label = '%.2f' % conf

    # Get the label for the class name and its confidence
    if classes:
        assert (class_id < len(classes))
        label = '%s:%s' % (classes[class_id], label)

    # Display the label at the top of the bounding box
    labelSize, baseLine = cv2.getTextSize(label, cv2.FONT_HERSHEY_SIMPLEX, 0.5, 1)
    top = max(top, labelSize[1])
    cv2.putText(frame, label, (left, top), cv2.FONT_HERSHEY_SIMPLEX, 2, COLORS[class_id], 6)


# Remove the bounding boxes with low confidence using non-maxima suppression
classIds_o = []
boxes_o = []


def postprocess(_frame, _outs):
    frame_height = _frame.shape[0]
    frame_width = _frame.shape[1]

    # Scan through all the bounding boxes output from the network and keep only the
    # ones with high confidence scores. Assign the box's class label as the class with the highest score.
    confidences = []
    class_ids = []
    boxes = []

    for out in _outs:
        for detection in out:
            scores = detection[5:]
            class_id = np.argmax(scores)
            confidence = scores[class_id]

            if confidence > confThreshold:
                center_x = int(detection[0] * frame_width)
                center_y = int(detection[1] * frame_height)
                width = int(detection[2] * frame_width)
                height = int(detection[3] * frame_height)
                left = int(center_x - width / 2)
                top = int(center_y - height / 2)
                if class_id in classIds_o:
                    duplicates.append(class_id)
                    duplicates.append(class_id)
                    i = classIds_o.index(class_id)
                    box1 = boxes_o[i]
                    box2 = [left, top, width, height]

                    duplicate_boxes.append(box1)
                    duplicate_boxes.append(box2)
                else:
                    box = [left, top, width, height]
                    singles.append((class_id, box))
                class_ids.append(class_id)
                classIds_o.append(class_id)
                confidences.append(float(confidence))
                boxes.append([left, top, width, height])
                boxes_o.append([left, top, width, height])

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
        draw_pred(class_ids[i], confidences[i], left, top, left + width, top + height)


singles = []
duplicates = []
duplicate_boxes = []
lowest_y_box = None


def registrer_piles(img_width):
    game_state = GameState()
    global lowest_y_box
    if lowest_y_box is None:
        lowest_y_box = [0xffffffff, 0xffffffff]
        for box in duplicate_boxes:
            if box[1] < lowest_y_box[1]:
                lowest_y_box = box
    box_height = int(lowest_y_box[3] * 1.25)
    box_height_range = range(lowest_y_box[1], lowest_y_box[1] + box_height)
    row_width = None
    row_x_cords = []
    game_rows = [[], [], [], [], [], [], []]
    top_cards = []
    for i in range(0, len(duplicates), 2):
        l1 = duplicate_boxes[i][0] if duplicate_boxes[i][0] < duplicate_boxes[i + 1][0] else duplicate_boxes[i + 1][0]
        l2 = duplicate_boxes[i][0] if duplicate_boxes[i][0] > duplicate_boxes[i + 1][0] else duplicate_boxes[i + 1][0]
        row_width = int((l2 - l1) / 2)
        if True not in np.in1d(row_x_cords, range(l1 - row_width, l1 + row_width)):
            row_x_cords.append(l1)
    row_x_cords.sort()
    for tup in singles:
        if tup[1][1] not in box_height_range:
            for i in range(len(row_x_cords)):
                if row_x_cords[i] in range(tup[1][0] - row_width, tup[1][0] + row_width):
                    game_rows[i].append(tup)
        else:
            top_cards.append(tup)
            pass

    row_count = 0
    for row in game_rows:
        row.sort(key=lambda tup: tup[1][1])
        for i in range(len(row)):
            rank_suit = classes[row[i][0]]
            game_state.gameCards[row_count].append(class_to_card(rank_suit))

        row_count = row_count + 1

    top_cards.sort(key=lambda tup: tup[1][0])
    if top_cards[0][1][0] > int(img_width / 3):
        top_cards.insert(0, None)
    for i in range(len(top_cards)):
        if top_cards[i] is not None:
            rank_suit = classes[top_cards[i][0]]
            if i == 0:
                game_state.shownStock = class_to_card(rank_suit)
            else:
                game_state.finalCards[i - 1] = class_to_card(rank_suit)

    s = encode_game(game_state)

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
comm = Comm()

while (True):
    # if comm.is_ready():
    response = comm.recv().decode("utf-8")
    print(response)
    # Capture frame-by-frame
    if capture_from_webcam:
        ret, frame = cap.read()

        if not ret:
            print("Error getting webcam feed")
            break

        cv2.imshow('frame', frame)
        cv2.waitKey(1)

    # k = cv2.waitKey(1)
    # if k % 256 == 27:
    if response == "stop":
        # ESC pressed
        print("Escape hit, closing...")
        break
    # elif k % 256 == 32:
    else:
        # SPACE pressed
        detected_cards = list()
        print("Space pressed")
        if not capture_from_webcam:
            frame = cv2.imread(img_name)
        cv2.imwrite("./split_images/image.png", frame)
        blob = cv2.dnn.blobFromImage(frame, 1 / 255.0, (832, 1024), (0, 0, 0), True, crop=False)
        net.setInput(blob)
        outs = net.forward(output_layers)
        postprocess(frame, outs)
        cv2.imwrite("./registrered.png", frame)

        comm.send(registrer_piles(frame.shape[1]))
        # cv2.imwrite("final.png", image)
        print("Final image saved")

        singles = []
        duplicates = []
        duplicate_boxes = []
        lowest_y_box = None

comm.close()
cap.release()
cv2.destroyAllWindows()
