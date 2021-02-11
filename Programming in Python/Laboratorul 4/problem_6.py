import os


def custom_callback(exception):
    print(f"The exception is:\n{exception}\n")


def contains(filename, token):
    if not isinstance(token, bytes):
        token = token.encode()
    with open(filename, "rb") as file:
        buffer_size = len(token)
        first_buffer = file.read(buffer_size)
        while first_buffer:
            second_buffer = file.read(buffer_size)
            if token in first_buffer + second_buffer:
                return True
            first_buffer = second_buffer


def search(path, token, callback):
    result = []
    try:
        absolute_path = os.path.abspath(path)
        if not (os.path.isfile(absolute_path) or os.path.isdir(absolute_path)):
            raise ValueError("Target is invalid file or folder.")
        if os.path.isfile(path) and contains(absolute_path, token):
            return [absolute_path]
        for (root, directories, files) in os.walk(absolute_path):
            for file in files:
                absolute_path = os.path.join(root, file)
                if contains(absolute_path, token):
                    result.append(absolute_path)
    except Exception as exception:
        callback(exception)
    finally:
        return result


if __name__ == '__main__':
    print(search(r"D:\Program Files\Python\Lib\time", "User-Agent", custom_callback))
