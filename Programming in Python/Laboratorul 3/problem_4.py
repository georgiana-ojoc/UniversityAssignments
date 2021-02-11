def build_xml_element(tag, content, **attributes):
    return "<" + tag + " " + "".join(
        [name + "=" + "\"" + value + "\" "
         for name, value in attributes.items()]).rstrip() + ">" + content + "</" + tag + ">"


def main():
    print(build_xml_element("a", "Hello!", href="http://python.org", identifier="python"))


if __name__ == '__main__':
    main()
