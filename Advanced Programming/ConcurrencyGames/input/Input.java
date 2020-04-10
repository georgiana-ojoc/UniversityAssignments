package input;

import java.util.Scanner;

public class Input {
    public static int readNumber(Scanner scanner, String errorMessage) {
        while (true) {
            try {
                int number = scanner.nextInt();
                if (number < 1) {
                    System.out.println(errorMessage);
                }
                else {
                    return number;
                }
            }
            catch (Exception exception) {
                exception.printStackTrace();
            }
        }
    }
}
