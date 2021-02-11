import os


def information(path):
    return {
        "absolute path": os.path.abspath(path),
        "size": os.stat(path).st_size,
        "extension": os.path.splitext(path)[-1].strip('.'),
        "can read": os.access(path, os.R_OK),
        "can write": os.access(path, os.W_OK)
    }


if __name__ == '__main__':
    print(information("absolute_paths.txt"))
