package words;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import utility.Efficiency;
import utility.Pair;

/**
 * @author Georgiana Ojoc
 */
public class WordGraph {
    protected int size;
    protected String[] words;
    protected boolean[][] adjMatrix;

    private int minNeighbourNo;
    private int maxNeighbourNo;
    private int[] neighbourNoArray;
    private Vector<Pair<Integer, String>> minNeighbourWords;
    private Vector<Pair<Integer, String>> maxNeighbourWords;

    private Efficiency efficiency;

    public WordGraph(String[] words, Efficiency efficiency) {
        this.size = words.length;
        this.words = Arrays.copyOf(words, words.length);
        adjMatrix = new boolean[size][size];

        this.efficiency = efficiency;

        this.minNeighbourNo = size;
        this.maxNeighbourNo = 0;
        if (this.efficiency == Efficiency.FAST) {
            maxNeighbourWords = new Vector<>();
            minNeighbourWords = new Vector<>();
        }
        else if (this.efficiency == Efficiency.SLOW) {
            neighbourNoArray = new int[size];
        }
    }

    private boolean haveCommonLetter(int first, int second, Efficiency efficiency) {
        if (efficiency == Efficiency.FAST) {
            int firstLength = words[first].length();
            Map<Character, Boolean> firstFreq = new HashMap<>();
            for (int i = 0; i < firstLength; ++i) {
                if (words[first].charAt(i) == words[second].charAt(i)) {
                    return true;
                }
                firstFreq.put(words[first].charAt(i), true);
            }
            for (int i = 0; i < firstLength; ++i) {
                if (firstFreq.get(words[second].charAt(i)) != null) {
                    return true;
                }
            }
        }
        else if (efficiency == Efficiency.SLOW) {
            int firstLength = words[first].length();
            int secondLength = words[second].length();
            for (int i = 0; i < firstLength; ++i) {
                for (int j = 0; j < secondLength; ++j) {
                    if (words[first].charAt(i) == words[second].charAt(j)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public void setMatrix() {
        for (int i = 0; i < size - 1; ++i) {
            for (int j = i + 1; j < size; ++j) {
                adjMatrix[i][j] = adjMatrix[j][i] = haveCommonLetter(i, j, efficiency);
            }
        }
    }

    private int getNeighbourNo(int index) {
        int count = 0;
        for (boolean bool : adjMatrix[index]) {
            if (bool) {
                ++count;
            }
        }
        return count;
    }

    public void computeRangeNeighbourWords() {
        for (int i = 0; i < size; ++i) {
            int neighbourNo = getNeighbourNo(i);
            if (neighbourNo < minNeighbourNo) {
                minNeighbourNo = neighbourNo;
                minNeighbourWords.clear();
                minNeighbourWords.add(new Pair<>(i, words[i]));
            }
            else if (neighbourNo == minNeighbourNo) {
                minNeighbourWords.add(new Pair<>(i, words[i]));
            }

            if (neighbourNo == maxNeighbourNo) {
                maxNeighbourWords.add(new Pair<>(i, words[i]));
            }
            else if (neighbourNo > maxNeighbourNo) {
                maxNeighbourNo = neighbourNo;
                maxNeighbourWords.clear();
                maxNeighbourWords.add(new Pair<>(i, words[i]));
            }
        }
    }

    public void setNeighbourNoArray() {
        for (int i = 0; i < size; ++i) {
            neighbourNoArray[i] = getNeighbourNo(i);
        }
    }

    public void computeMinNeighbourNo() {
        for (int i = 0; i < size; ++i) {
            if (neighbourNoArray[i] < minNeighbourNo) {
                minNeighbourNo = neighbourNoArray[i];
            }
        }
    }

    public void computeMaxNeighbourNo() {
        for (int i = 0; i < size; ++i) {
            if (neighbourNoArray[i] > maxNeighbourNo) {
                maxNeighbourNo = neighbourNoArray[i];
            }
        }
    }

    public int getMinNeighbourNo() {
        return minNeighbourNo;
    }

    public int getMaxNeighbourNo() {
        return maxNeighbourNo;
    }

    public Vector<utility.Pair<Integer, String>> getMinNeighbourWords() {
        return minNeighbourWords;
    }

    public Vector<utility.Pair<Integer, String>> getMaxNeighbourWords() {
        return maxNeighbourWords;
    }

    public void printMatrix() {
        for (int i = 0; i < size; ++i) {
            System.out.print(i + 1 + ": ");
            for (int j = 0; j < size; ++j) {
                System.out.print(adjMatrix[i][j]);
                System.out.print(' ');
            }
            System.out.println();
        }
        System.out.println();
    }

    public void printMinNeighbourWords() {
        for (Pair<Integer, String> pair : minNeighbourWords) {
            System.out.println(pair.first + 1 + ": " + pair.second + ' ');
        }

        System.out.println("Number of nodes: " + minNeighbourWords.size());
        System.out.println();
    }

    public void printMaxNeighbourWords() {
        for (Pair<Integer, String> pair : maxNeighbourWords) {
            System.out.println(pair.first + 1 + ": " + pair.second + ' ');
        }

        System.out.println("Number of nodes: " + maxNeighbourWords.size());
        System.out.println();
    }

    public void printWords(int neighbourNo) {
        int count = 0;
        for (int i = 0; i < size; ++i) {
            if (neighbourNoArray[i] == neighbourNo) {
                ++count;
                System.out.println(i + 1 + ": " + words[i] + ' ');
            }
        }

        System.out.println("Number of nodes: " + count);
        System.out.println();
    }
}
