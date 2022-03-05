import os
from pathlib import Path

import cv2
import numpy


def detect_skin(original_path, predictions_directory):
    image = cv2.imread(original_path)
    rgb_image = cv2.cvtColor(image, cv2.COLOR_BGR2RGB)
    hsv_image = cv2.cvtColor(image, cv2.COLOR_BGR2HSV)
    ycrcb_image = cv2.cvtColor(image, cv2.COLOR_BGR2YCrCb)
    rgb_skin_image = numpy.zeros(rgb_image.shape, dtype=int)
    hsv_skin_image = numpy.zeros(hsv_image.shape, dtype=int)
    ycrcb_skin_image = numpy.zeros(ycrcb_image.shape, dtype=int)
    for line in range(image.shape[0]):
        for column in range(image.shape[1]):
            red, green, blue = rgb_image[line][column][0], rgb_image[line][column][1], rgb_image[line][column][2]
            hue, saturation, value = hsv_image[line][column][0], hsv_image[line][column][1], hsv_image[line][column][2]
            saturation /= 255
            value /= 255
            luma, red_difference, blue_difference = \
                ycrcb_image[line][column][0], ycrcb_image[line][column][1], ycrcb_image[line][column][2]
            if red > 95 and green > 40 and blue > 20 and max(red, green, blue) - min(red, green, blue) > 15 \
                    and abs(int(red) - int(green)) > 15 and red > green and red > blue:
                rgb_skin_image[line][column][0] = rgb_skin_image[line][column][1] = \
                    rgb_skin_image[line][column][2] = 255
            if 0 <= hue <= 50 and 0.23 <= saturation <= 0.68 and 0.35 <= value <= 1:
                hsv_skin_image[line][column][0] = hsv_skin_image[line][column][1] = \
                    hsv_skin_image[line][column][2] = 255
            if luma > 80 and 85 < blue_difference < 135 and 135 < red_difference < 180:
                ycrcb_skin_image[line][column][0] = ycrcb_skin_image[line][column][1] = \
                    ycrcb_skin_image[line][column][2] = 255
    file = os.path.basename(original_path)
    cv2.imwrite(os.path.join(predictions_directory, "rgb", file), rgb_skin_image)
    cv2.imwrite(os.path.join(predictions_directory, "hsv", file), hsv_skin_image)
    cv2.imwrite(os.path.join(predictions_directory, "ycrcb", file), ycrcb_skin_image)
    print(original_path)


def create_directory(path):
    if not os.path.exists(path):
        os.makedirs(path)


def create_images(originals_path):
    print("Create images")
    for root, directories, files in os.walk(originals_path):
        predictions_directory = root.replace("originals", "predictions")
        for directory in directories:
            create_directory(os.path.join(predictions_directory, directory, "rgb"))
            create_directory(os.path.join(predictions_directory, directory, "hsv"))
            create_directory(os.path.join(predictions_directory, directory, "ycrcb"))
        for file in files:
            detect_skin(os.path.join(root, file), predictions_directory)


def evaluate_method(ground_truth_path, prediction_path, evaluations_directory):
    confusion_matrix = numpy.zeros(shape=(2, 2), dtype=int)
    ground_truth_image = cv2.imread(ground_truth_path, cv2.IMREAD_GRAYSCALE)
    prediction_image = cv2.imread(prediction_path, cv2.IMREAD_GRAYSCALE)
    for line in range(ground_truth_image.shape[0]):
        for column in range(ground_truth_image.shape[1]):
            if ground_truth_image[line][column] == 255 and prediction_image[line][column] == 255:
                confusion_matrix[0][0] += 1
            if ground_truth_image[line][column] == 255 and prediction_image[line][column] == 0:
                confusion_matrix[0][1] += 1
            if ground_truth_image[line][column] == 0 and prediction_image[line][column] == 255:
                confusion_matrix[1][0] += 1
            if ground_truth_image[line][column] == 0 and prediction_image[line][column] == 0:
                confusion_matrix[1][1] += 1
    file = Path(ground_truth_path).stem
    numpy.savetxt(os.path.join(evaluations_directory, f"{file}.txt"), confusion_matrix, fmt="%d")
    print(prediction_path)
    return (confusion_matrix[0][0] + confusion_matrix[1][1]) / confusion_matrix.sum()


def evaluate_methods(ground_truth_path):
    for root, directories, files in os.walk(ground_truth_path):
        predictions_directory = root.replace("ground_truth", "predictions")
        evaluations_directory = root.replace("ground_truth", "evaluations")
        for directory in directories:
            create_directory(os.path.join(evaluations_directory, directory, "rgb"))
            create_directory(os.path.join(evaluations_directory, directory, "hsv"))
            create_directory(os.path.join(evaluations_directory, directory, "ycrcb"))
        if files:
            print(f"Evaluate methods for: {root}")
            rgb_accuracies = []
            hsv_accuracies = []
            ycrcb_accuracies = []
            for file in files:
                method = "rgb"
                rgb_accuracies += [evaluate_method(os.path.join(root, file),
                                                   os.path.join(predictions_directory, method, file),
                                                   os.path.join(evaluations_directory, method))]
                method = "hsv"
                hsv_accuracies += [evaluate_method(os.path.join(root, file),
                                                   os.path.join(predictions_directory, method, file),
                                                   os.path.join(evaluations_directory, method))]
                method = "ycrcb"
                ycrcb_accuracies += [evaluate_method(os.path.join(root, file),
                                                     os.path.join(predictions_directory, method, file),
                                                     os.path.join(evaluations_directory, method))]
            rgb_accuracies += [numpy.mean(rgb_accuracies)]
            hsv_accuracies += [numpy.mean(rgb_accuracies)]
            ycrcb_accuracies += [numpy.mean(rgb_accuracies)]
            numpy.savetxt(os.path.join(evaluations_directory, "rgb.txt"), rgb_accuracies, fmt="%.2f")
            numpy.savetxt(os.path.join(evaluations_directory, "hsv.txt"), rgb_accuracies, fmt="%.2f")
            numpy.savetxt(os.path.join(evaluations_directory, "ycrcb.txt"), rgb_accuracies, fmt="%.2f")
            print("RGB accuracy: %.2f" % rgb_accuracies[-1])
            print("HSV accuracy: %.2f" % hsv_accuracies[-1])
            print("YCrCb accuracy: %.2f" % ycrcb_accuracies[-1])


def main():
    create_images(r"D:\Master\Anul 1\Semestrul I\ProcesareaImaginilor\Teme\Tema1\originals")
    evaluate_methods(r"D:\Master\Anul 1\Semestrul I\ProcesareaImaginilor\Teme\Tema1\ground_truth")


if __name__ == '__main__':
    main()
