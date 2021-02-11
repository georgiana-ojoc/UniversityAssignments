import collections
import os


def characters_or_extensions(path):
    if os.path.isfile(path):
        read_characters = min(20, os.stat(path).st_size)
        with open(path, "rb") as file:
            file.seek(-read_characters, os.SEEK_END)
            return file.read().decode()
    extensions = collections.defaultdict(lambda: 0)
    for (root, _, filenames) in os.walk(path):
        for filename in filenames:
            extension = os.path.splitext(filename)[-1].strip('.')
            extensions[extension] += 1
    return sorted(extensions.items(), key=lambda item: 1 / item[1])


if __name__ == '__main__':
    print(characters_or_extensions(r"D:\Program Files\Python\LICENSE.txt"))
    print(characters_or_extensions(r"D:\Program Files\Python"))
