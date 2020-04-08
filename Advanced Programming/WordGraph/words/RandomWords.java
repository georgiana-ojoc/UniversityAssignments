package words;

import java.util.Arrays;
import java.util.Random;

/**
 * @author Georgiana Ojoc
 */
public class RandomWords {
    private int wordNo;
    private int wordLength;
    private char[] alphabet;
    private String[] words;

    public RandomWords(int wordNo, int wordLength, char[] alphabet) {
        this.wordNo = wordNo;
        this.wordLength = wordLength;
        this.alphabet = Arrays.copyOf(alphabet, alphabet.length);
        words = new String[this.wordNo];
    }

    public String createWord() {
        StringBuilder word = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < wordLength; ++i) {
            word.append(alphabet[random.nextInt(alphabet.length)]);
        }
        return word.toString();
    }

    public void createWordsArray() {
        for (int i = 0; i < wordNo; ++i) {
            words[i] = createWord();
        }
    }

    public String[] getWords() {
        return words;
    }

    public void printWords() {
        for (int i = 0; i < wordNo; ++i) {
            System.out.println(i + 1 + ": " + words[i]);
        }
        System.out.println();
    }
}
