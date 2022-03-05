import cv2
import numpy


def get_ellipse(height, width, a=2.0, b=3.0):
    x = numpy.linspace(-2.5, 2.5, width)
    y = numpy.linspace(-2.5, 2.5, height)
    x_grid, y_grid = numpy.meshgrid(x, y)
    ellipse = x_grid ** 2 / a ** 2 + y_grid ** 2 / b ** 2
    grid = numpy.zeros(shape=(height, width), dtype=numpy.int32)
    grid[ellipse < 1.0] = 1
    return grid


def compute_face_pixels(frame):
    ellipse = get_ellipse(*frame.shape)
    return numpy.multiply(frame, ellipse).sum() // 255


def compute_eye_mouth_pixels(frame, a, b):
    ellipse = get_ellipse(*frame.shape, a, b)
    frame[frame == 0] = 1
    frame[frame == 255] = 0
    return numpy.multiply(frame, ellipse).sum()


def get_face_information(image):
    accuracy = 0
    center_x = 0
    center_y = 0
    radius_x = 0
    radius_y = 0
    third_of_width = image.shape[1] // 3
    third_of_height = image.shape[0] // 3
    for y in range(third_of_height, third_of_height * 2, 10):
        for x in range(third_of_width, third_of_width * 2, 10):
            for distance_y in range(third_of_height // 2, third_of_height, 10):
                for distance_x in range(third_of_width // 2, third_of_width, 10):
                    frame = image[y - distance_y:y + distance_y, x - distance_x:x + distance_x]
                    value = compute_face_pixels(frame) / (frame.sum() // 255)
                    if value > accuracy:
                        accuracy = value
                        center_x = x
                        center_y = y
                        radius_x = distance_x
                        radius_y = distance_y
    return center_x, center_y, radius_x, radius_y


def get_left_eye_information(image, face_center_x, face_center_y, face_radius_x, face_radius_y):
    accuracy = 0
    center_x = 0
    center_y = 0
    radius_x = 0
    radius_y = 0
    third_of_height = int(face_radius_y / 1.5)
    fourth_of_width = face_radius_x // 2
    for y in range(face_center_y - face_radius_y + int(third_of_height * 1.25),
                   face_center_y - face_radius_y + third_of_height * 2, 4):
        for x in range(face_center_x - face_radius_x + fourth_of_width,
                       face_center_x - face_radius_x + fourth_of_width * 2, 4):
            for distance_y in range(third_of_height // 5, third_of_height // 4, 4):
                for distance_x in range(int(fourth_of_width * 0.75), fourth_of_width, 4):
                    frame = image[y - distance_y:y + distance_y, x - distance_x:x + distance_x]
                    value = compute_eye_mouth_pixels(frame, a=3.0, b=1.0) / frame.sum()
                    if value > accuracy:
                        accuracy = value
                        center_x = x
                        center_y = y
                        radius_x = distance_x
                        radius_y = distance_y
    return center_x, center_y, radius_x, radius_y


def get_right_eye_information(image, face_center_x, face_center_y, face_radius_x, face_radius_y):
    accuracy = 0
    center_x = 0
    center_y = 0
    radius_x = 0
    radius_y = 0
    third_of_height = int(face_radius_y / 1.5)
    fourth_of_width = face_radius_x // 2
    for y in range(face_center_y - face_radius_y + int(third_of_height * 1.25),
                   face_center_y - face_radius_y + third_of_height * 2, 4):
        for x in range(face_center_x + face_radius_x - fourth_of_width,
                       face_center_x + face_radius_x - fourth_of_width * 2, -4):
            for distance_y in range(third_of_height // 5, third_of_height // 4, 4):
                for distance_x in range(int(fourth_of_width * 0.75), fourth_of_width, 4):
                    frame = image[y - distance_y:y + distance_y, x - distance_x:x + distance_x]
                    value = compute_eye_mouth_pixels(frame, a=3.0, b=1.0) / frame.sum()
                    if value > accuracy:
                        accuracy = value
                        center_x = x
                        center_y = y
                        radius_x = distance_x
                        radius_y = distance_y
    return center_x, center_y, radius_x, radius_y


def get_mouth_information(image, face_center_x, face_center_y, face_radius_x, face_radius_y):
    accuracy = 0
    center_x = 0
    center_y = 0
    radius_x = 0
    radius_y = 0
    third_of_height = int(face_radius_y / 1.5)
    fourth_of_width = face_radius_x // 2
    for y in range(face_center_y + third_of_height // 2, face_center_y + int(third_of_height * 1.5), 4):
        for x in range(face_center_x - fourth_of_width // 16, face_center_x + fourth_of_width // 16):
            for distance_y in range(third_of_height // 8, third_of_height // 4, 2):
                for distance_x in range(fourth_of_width, fourth_of_width * 2, 4):
                    frame = image[y - distance_y:y + distance_y, x - distance_x:x + distance_x]
                    value = compute_eye_mouth_pixels(frame, a=4.0, b=1.0) / frame.sum()
                    if value > accuracy:
                        accuracy = value
                        center_x = x
                        center_y = y
                        radius_x = distance_x
                        radius_y = distance_y
    return center_x, center_y, radius_x, radius_y


def main():
    image_index = 22
    original_path = r"D:\Master\Anul 1\Semestrul I\ProcesareaImaginilor\Teme\Tema1\originals\pratheepan\face_photos" \
                    fr"\{image_index}.png"
    skin_classification_path = r"D:\Master\Anul 1\Semestrul I\ProcesareaImaginilor\Teme\Tema1\ground_truth\pratheepan" \
                               fr"\face_photos\{image_index}.png"
    eye_path = r"D:\Master\Anul 1\Semestrul I\ProcesareaImaginilor\Teme\Tema1\eye.png"
    mouth_path = r"D:\Master\Anul 1\Semestrul I\ProcesareaImaginilor\Teme\Tema1\mouth.png"
    original_image = cv2.imread(original_path)
    skin_classification_image = cv2.imread(skin_classification_path, cv2.IMREAD_GRAYSCALE)
    gray_original_image = cv2.cvtColor(original_image, cv2.COLOR_BGR2GRAY)
    eye_image = cv2.imread(eye_path)
    mouth_image = cv2.imread(mouth_path)
    face_center_x, face_center_y, face_radius_x, face_radius_y = get_face_information(skin_classification_image)
    left_eye_center_x, left_eye_center_y, left_eye_radius_x, left_eye_radius_y = get_left_eye_information(
        skin_classification_image, face_center_x, face_center_y, face_radius_x, face_radius_y)
    right_eye_center_x, right_eye_center_y, right_eye_radius_x, right_eye_radius_y = get_right_eye_information(
        skin_classification_image, face_center_x, face_center_y, face_radius_x, face_radius_y)
    mouth_center_x, mouth_center_y, mouth_radius_x, mouth_radius_y = get_mouth_information(
        skin_classification_image, face_center_x, face_center_y, face_radius_x, face_radius_y)
    cascade_classifier = cv2.CascadeClassifier("haarcascade_frontalface_default.xml")
    faces = cascade_classifier.detectMultiScale(gray_original_image, scaleFactor=1.1, minNeighbors=5)
    skin_classification_image = cv2.imread(skin_classification_path)
    cv2.ellipse(original_image, (face_center_x, face_center_y), (face_radius_x, face_radius_y), angle=0, startAngle=0,
                endAngle=360,
                color=(0, 255, 0), thickness=2)
    cv2.ellipse(skin_classification_image, (face_center_x, face_center_y), (face_radius_x, face_radius_y), angle=0,
                startAngle=0,
                endAngle=360, color=(0, 255, 0), thickness=2)
    for (x, y, width, height) in faces:
        face_radius_x = width // 2
        face_radius_y = height // 2
        cv2.ellipse(original_image, (x + face_radius_x, y + face_radius_y), (face_radius_x, face_radius_y), angle=0,
                    startAngle=0,
                    endAngle=360, color=(0, 0, 255), thickness=2)
    face_radius_y, face_radius_x = original_image.shape[:-1]
    face_radius_x = face_radius_x // 3
    face_radius_y = face_radius_y // 3
    face_center_x = face_radius_x * 3 // 2
    face_center_y = face_radius_y * 3 // 2
    cv2.ellipse(original_image, (face_center_x, face_center_y), (face_radius_x, face_radius_y), angle=0, startAngle=0,
                endAngle=360,
                color=(255, 0, 0), thickness=2)
    cv2.ellipse(skin_classification_image, (face_center_x, face_center_y), (face_radius_x, face_radius_y), angle=0,
                startAngle=0,
                endAngle=360,
                color=(255, 0, 0), thickness=2)
    eye_image = cv2.resize(eye_image, (left_eye_radius_x * 2, left_eye_radius_y * 2), interpolation=cv2.INTER_AREA)
    original_image[left_eye_center_y - left_eye_radius_y:left_eye_center_y + left_eye_radius_y,
    left_eye_center_x - left_eye_radius_x:left_eye_center_x + left_eye_radius_x, :] = eye_image
    eye_image = cv2.resize(eye_image, (right_eye_radius_x * 2, right_eye_radius_y * 2), interpolation=cv2.INTER_AREA)
    original_image[right_eye_center_y - right_eye_radius_y:right_eye_center_y + right_eye_radius_y,
    right_eye_center_x - right_eye_radius_x:right_eye_center_x + right_eye_radius_x, :] = eye_image
    mouth_image = cv2.resize(mouth_image, (mouth_radius_x * 2, mouth_radius_y * 2), interpolation=cv2.INTER_AREA)
    original_image[mouth_center_y - mouth_radius_y:mouth_center_y + mouth_radius_y,
    mouth_center_x - mouth_radius_x:mouth_center_x + mouth_radius_x, :] = mouth_image
    cv2.imwrite(r"D:\Master\Anul 1\Semestrul I\ProcesareaImaginilor\Teme\Tema1\emoji.png", original_image)
    cv2.imshow(original_path, original_image)
    cv2.imshow(skin_classification_path, skin_classification_image)
    cv2.waitKey()


if __name__ == '__main__':
    main()
