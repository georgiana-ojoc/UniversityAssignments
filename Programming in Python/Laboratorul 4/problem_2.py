import os


def absolute_paths(directory, file_name):
    paths = [item.path for item in os.scandir(directory) if item.is_file() and item.name.lower().startswith('b')]
    with open(file_name, 'w') as file:
        for path in paths:
            file.write(f"{os.path.abspath(path)}\n")


if __name__ == '__main__':
    absolute_paths(r"D:\Program Files\Python\include", "absolute_paths.txt")
