package introduction;

import utility.Efficiency;

/**
 * @author Georgiana Ojoc
 */
public class Introduction {
    private static int checkSum(int number, Efficiency efficiency) {
        if (efficiency == Efficiency.FAST) {
            if (number % 9 == 0) {
                return 9;
            }
            return number % 9;
        }
        else if (efficiency == Efficiency.SLOW) {
            while (number > 9) {
                int sum = 0;
                while (number > 0) {
                    sum += number % 10;
                    number /=  10;
                }
                number = sum;
            }
            return number;
        }
        return 0;
    }
    public static void main(String[] args) {
        Efficiency efficiency = Efficiency.FAST;
        if (args.length > 0) {
            if (args[0].equals("slow")) {
                efficiency = Efficiency.SLOW;
            }
        }

        System.out.println("Hello, World!");
        System.out.println();

        String[] languages = new String[]{"C", "C++", "C#", "Python", "Go", "Rust", "JavaScript", "PHP", "Swift", "Java"};

        int n = (int) (Math.random() * 1_000_000);

        n *= 3;
        n += 0b10101;
        n += 0xFF;
        n *= 6;

        n = checkSum(n, efficiency);

        System.out.println("Willy-nilly, this semester I will learn " + languages[n] + ".");
        System.out.println();
    }
}
