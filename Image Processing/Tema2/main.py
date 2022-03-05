import os.path

import cv2
import numpy


def apply_first_method(image, folder_path):
    cv2.imwrite(os.path.join(folder_path, "1.jpg"), numpy.average(image, axis=2))


def apply_second_method(image, folder_path):
    cv2.imwrite(os.path.join(folder_path, "2_1.jpg"), numpy.average(image, axis=2, weights=[0.3, 0.59, 0.11]))
    cv2.imwrite(os.path.join(folder_path, "2_2.jpg"), numpy.average(image, axis=2, weights=[0.2126, 0.7152, 0.0722]))
    cv2.imwrite(os.path.join(folder_path, "2_3.jpg"), numpy.average(image, axis=2, weights=[0.299, 0.587, 0.114]))


def apply_third_method(image, folder_path):
    cv2.imwrite(os.path.join(folder_path, "3.jpg"), (numpy.max(image, axis=2) + numpy.min(image, axis=2)) / 2)


def apply_fourth_method(image, folder_path):
    cv2.imwrite(os.path.join(folder_path, "4_1.jpg"), numpy.max(image, axis=2))
    cv2.imwrite(os.path.join(folder_path, "4_2.jpg"), numpy.min(image, axis=2))


def apply_fifth_method(image, folder_path):
    cv2.imwrite(os.path.join(folder_path, "5_1.jpg"), image[:, :, 0])
    cv2.imwrite(os.path.join(folder_path, "5_2.jpg"), image[:, :, 1])
    cv2.imwrite(os.path.join(folder_path, "5_3.jpg"), image[:, :, 2])


def get_left_index(lefts, grey):
    left = 0
    right = len(lefts) - 1
    result = -1
    while left <= right:
        middle = (left + right) // 2
        if middle == right:
            if lefts[middle] <= grey <= 255:
                return middle
        elif lefts[middle] <= grey <= lefts[middle + 1]:
            return middle
        if lefts[middle] <= grey:
            result = left
            left = middle + 1
        else:
            right = middle - 1
    return result


def apply_sixth_method(image, folder_path, shades):
    image = numpy.average(image, axis=2, weights=[0.299, 0.587, 0.114])
    lefts = [0]
    remainder = 255 % shades
    for _ in range(shades - 1):
        if remainder != 0:
            lefts += [lefts[-1] + 255 // shades + 1]
            remainder -= 1
        else:
            lefts += [lefts[-1] + 255 // shades]
    for line in range(image.shape[0]):
        for column in range(image.shape[1]):
            left_index = get_left_index(lefts, image[line][column])
            if left_index == len(lefts) - 1:
                image[line][column] = (lefts[left_index] + 255) // 2
            else:
                image[line][column] = (lefts[left_index] + lefts[left_index + 1]) // 2
    cv2.imwrite(os.path.join(folder_path, "6.jpg"), image)


def apply_seventh_method(input_image, folder_path, shades):
    input_image = numpy.average(input_image, axis=2, weights=[0.299, 0.587, 0.114])
    output_image = numpy.zeros(shape=input_image.shape)
    lefts = [0]
    remainder = 255 % shades
    for _ in range(shades - 1):
        if remainder != 0:
            lefts += [lefts[-1] + 255 // shades + 1]
            remainder -= 1
        else:
            lefts += [lefts[-1] + 255 // shades]
    for line in range(input_image.shape[0] - 1):
        for column in range(1, input_image.shape[1] - 1):
            left_index = get_left_index(lefts, input_image[line][column])
            if left_index != -1:
                if left_index == len(lefts) - 1:
                    output_image[line][column] = (lefts[left_index] + 255) // 2
                else:
                    output_image[line][column] = (lefts[left_index] + lefts[left_index + 1]) // 2
            error = input_image[line][column] - output_image[line][column]
            input_image[line, column + 1] += error * 7 / 16.0
            input_image[line + 1, column - 1] += error * 3 / 16.0
            input_image[line + 1, column] += error * 5 / 16.0
            input_image[line + 1, column + 1] += error / 16.0
    cv2.imwrite(os.path.join(folder_path, "7.jpg"), output_image)


def apply_eighth_method(input_image, folder_path, shades):
    mask = numpy.array([[2, 4, 8, 4, 2], [1, 2, 4, 2, 1]], dtype=numpy.float32) / 42
    input_image = numpy.average(input_image, axis=2, weights=[0.299, 0.587, 0.114])
    output_image = numpy.zeros(shape=input_image.shape)
    lefts = [0]
    remainder = 255 % shades
    for _ in range(shades - 1):
        if remainder != 0:
            lefts += [lefts[-1] + 255 // shades + 1]
            remainder -= 1
        else:
            lefts += [lefts[-1] + 255 // shades]
    for line in range(input_image.shape[0] - 2):
        for column in range(2, input_image.shape[1] - 2):
            left_index = get_left_index(lefts, input_image[line][column])
            if left_index != -1:
                if left_index == len(lefts) - 1:
                    output_image[line][column] = (lefts[left_index] + 255) // 2
                else:
                    output_image[line][column] = (lefts[left_index] + lefts[left_index + 1]) // 2
            error = input_image[line][column] - output_image[line][column]
            input_image[line][column + 1] += error * 8 / 42.0
            input_image[line][column + 2] += error * 4 / 42.0
            input_image[line + 1:line + 3, column - 2:column + 3] += error * mask
    cv2.imwrite(os.path.join(folder_path, "8.jpg"), output_image)


def main():
    folder_path = r"D:\Master\Anul 1\Semestrul I\ProcesareaImaginilor\Teme\Tema2"
    image = cv2.imread(os.path.join(folder_path, "image.jpg"))
    image = cv2.cvtColor(image, cv2.COLOR_BGR2RGB)
    apply_first_method(image, folder_path)
    apply_second_method(image, folder_path)
    apply_third_method(image, folder_path)
    apply_fourth_method(image, folder_path)
    apply_fifth_method(image, folder_path)
    apply_sixth_method(image, folder_path, shades=16)
    apply_seventh_method(image, folder_path, shades=2)
    apply_eighth_method(image, folder_path, shades=2)


if __name__ == '__main__':
    main()
