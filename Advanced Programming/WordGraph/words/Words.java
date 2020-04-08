package words;

import utility.Efficiency;

/**
 * @author Georgiana Ojoc
 */
public class Words {
    public static void main(String[] args) {
        long startTime = System.nanoTime();

        if (args.length < 4) {
            System.out.println("Usage: efficiency, number, number, one or more characters");
            System.exit(-1);
        }

        Efficiency efficiency = Efficiency.FAST;
        if (args[0].equals("slow")) {
            efficiency = Efficiency.SLOW;
        }

        int n = Integer.parseInt(args[1]);
        int k = Integer.parseInt(args[2]);

        int m = args.length - 3;
        char[] alphabet = new char[m];
        for (int i = 0; i < m; ++i) {
            alphabet[i] = args[i + 3].charAt(0);
        }

        RandomWords words = new RandomWords(n, k, alphabet);
        words.createWordsArray();

        if (n < 1_000) {
            words.printWords();
        }

        ConnectedWordGraph graph = new ConnectedWordGraph(words.getWords(), efficiency);
        graph.setMatrix();

        if (n < 1_000) {
            graph.printMatrix();
        }

        if (efficiency == Efficiency.FAST) {
            graph.computeRangeNeighbourWords();
        }
        else if (efficiency == Efficiency.SLOW) {
            graph.setNeighbourNoArray();
            graph.computeMinNeighbourNo();
            graph.computeMaxNeighbourNo();
        }

        int minNeighbourNo = graph.getMinNeighbourNo();
        int maxNeighbourNo = graph.getMaxNeighbourNo();

        if (minNeighbourNo == maxNeighbourNo) {
            System.out.println("All the words have " + minNeighbourNo + " neighbors.");
            if (n < 1_1000) {
                if (efficiency == Efficiency.FAST) {
                    graph.printMinNeighbourWords();
                }
                else if (efficiency == Efficiency.SLOW) {
                    graph.printWords(minNeighbourNo);
                }
            }
        }
        else {
            System.out.println("Minimum number of neighbors = " + minNeighbourNo);
            if (n < 1_1000) {
                if (efficiency == Efficiency.FAST) {
                    graph.printMinNeighbourWords();
                }
                else if (efficiency == Efficiency.SLOW) {
                    graph.printWords(minNeighbourNo);
                }
            }
            System.out.println("Maximum number of neighbors = " + maxNeighbourNo);
            if (n < 1_1000) {
                if (efficiency == Efficiency.FAST) {
                    graph.printMaxNeighbourWords();
                }
                else if (efficiency == Efficiency.SLOW) {
                    graph.printWords(maxNeighbourNo);
                }
            }
        }

        long finalTime = System.nanoTime();
        System.out.println("Running time in seconds = " + (finalTime - startTime) / 1_000_000_000.);
        System.out.println();


        if (n < 1_000) {
            graph.setListArray();
            graph.connectedComponents();

            System.out.println("Running time in seconds = " + (System.nanoTime() - finalTime) / 1_000_000_000.);
        }
    }
}
