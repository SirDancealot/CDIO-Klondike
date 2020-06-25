This project is in two parts, the SolitaireClient and the yolo_object_detection.


In order to run the project both parts needs to be executed, the SolitaireClient by running the file "run client.bat" and the yolo_object_detection by running "yolo_object_detection.exe".

In order to run the SolitaireClient JavaFX needs to be installed and the "$JavaFX-Version\bin" needs to be added to the $PATH of the computer.

In order to run yolo_object_detection the files "yolov3_training.cfg" and "yolov3_training_last.weights" needs to be placed in the same folder as the executable.

The object_detection will use the default webcame of the machine it is running on and try to detect playing-cards based on what it sees.