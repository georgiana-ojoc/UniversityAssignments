import os


def files(path):
    return [item.path for item in os.scandir(os.path.abspath(path)) if item.is_file()]


if __name__ == '__main__':
    print(files(r"D:\Program Files\Python"))
