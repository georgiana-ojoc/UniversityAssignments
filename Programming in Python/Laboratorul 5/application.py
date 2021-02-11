from utility import process_item


def main():
    while True:
        number = input("Enter number or 'q' if quit: ")
        if number == 'q':
            break
        try:
            print(process_item(int(number)))
        except ValueError:
            print("Enter numbers or 'q' if quit.")


if __name__ == '__main__':
    main()
