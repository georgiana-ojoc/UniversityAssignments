package knapsack;

public class Main {
    private static void compulsory() {
        System.out.println("Compulsory:");

        Problem problem = new Problem();
        problem.addItems(new Book("book1", "Dragon Rising", 5, 300));
        problem.addItems(new Book("book2", "A Blade in the Dark", 5, 300));
        problem.addItems(new Food("food1", "Cabbage", 2));
        problem.addItems(new Food("food2", "Rabbit", 2));
        problem.addItems(new Weapon("weapon", WeaponType.Sword, 10, 5));
        System.out.println(problem);

        Knapsack knapsack = new Knapsack(10);
        System.out.println(knapsack);

        Greedy greedy = new Greedy(problem, knapsack);
        greedy.solve();
        System.out.println(greedy);

        DynamicProgramming dynamicProgramming = new DynamicProgramming(problem, knapsack);
        dynamicProgramming.solve();
        System.out.println(dynamicProgramming);
    }

    private static void optional() {
        System.out.println("Optional:");

        System.out.println("Test 1:");

        ProblemGenerator randomProblem = new ProblemGenerator(5, 10, 10);
        randomProblem.generate();
        System.out.println(randomProblem);

        KnapsackGenerator randomKnapsack = new KnapsackGenerator(10, 15);
        randomKnapsack.generate();
        System.out.println(randomKnapsack);

        Greedy randomGreedy = new Greedy(randomProblem, randomKnapsack);
        long greedyStartTime = System.nanoTime();
        randomGreedy.solve();
        long greedyFinalTime = System.nanoTime();
        System.out.print(randomGreedy);
        double greedyRunningTime = greedyFinalTime - greedyStartTime;
        System.out.println(String.format("Running time in miliseconds = %.6f\n", greedyRunningTime / 1_000_000.));

        DynamicProgramming randomDynProg = new DynamicProgramming(randomProblem, randomKnapsack);
        long dynProgStartTime = System.nanoTime();
        randomDynProg.solve();
        long dynProgFinalTime = System.nanoTime();
        System.out.print(randomDynProg);
        double dynProgRunningTime = dynProgFinalTime - dynProgStartTime;
        System.out.println(String.format("Running time in miliseconds = %.6f\n", dynProgRunningTime / 1_000_000.));

        if (greedyRunningTime < dynProgRunningTime) {
            System.out.println("Greedy is faster than dynamic programming.");
        }
        else {
            System.out.println("Dynamic programming is faster than greedy.");
        }

        int difference = randomGreedy.getKnapsack().getValue() - randomDynProg.getKnapsack().getValue();
        if (difference > 0) {
            System.out.println("Greedy gives a better result than dynamic programming.");
        }
        else if (difference == 0) {
            System.out.println("Greedy and dynamic programming give the same result.");
        }
        else {
            System.out.println("Dynamic programming gives a better result than greedy.");
        }
        System.out.println();

        for (int i = 2; i <= 5; ++i) {
            System.out.println("Test " + i + ':');

            randomProblem.generate();

            randomKnapsack.generate();
            System.out.println(randomKnapsack);

            randomGreedy.setProblem(randomProblem);
            randomGreedy.setKnapsack(randomKnapsack);
            greedyStartTime = System.nanoTime();
            randomGreedy.solve();
            greedyFinalTime = System.nanoTime();
            greedyRunningTime = greedyFinalTime - greedyStartTime;
            System.out.println("Greedy solution:\nvalue = " + randomGreedy.getKnapsack().getValue()
                    + ", weight = " + randomGreedy.getKnapsack().getWeight());
            System.out.println(String.format("Running time in miliseconds = %.6f\n", greedyRunningTime / 1_000_000.));

            randomDynProg.setProblem(randomProblem);
            randomDynProg.setKnapsack(randomKnapsack);
            dynProgStartTime = System.nanoTime();
            randomDynProg.solve();
            dynProgFinalTime = System.nanoTime();
            dynProgRunningTime = dynProgFinalTime - dynProgStartTime;
            System.out.println("Dynamic programming solution:\nvalue = " + randomDynProg.getKnapsack().getValue()
                    + ", weight = " + randomDynProg.getKnapsack().getWeight());
            System.out.println(String.format("Running time in miliseconds = %.6f\n", dynProgRunningTime / 1_000_000.));

            if (greedyRunningTime < dynProgRunningTime) {
                System.out.println("Greedy is faster than dynamic programming.");
            }
            else {
                System.out.println("Dynamic programming is faster than greedy.");
            }

            difference = randomGreedy.getKnapsack().getValue() - randomDynProg.getKnapsack().getValue();
            if (difference > 0) {
                System.out.println("Greedy gives a better result than dynamic programming.");
            }
            else if (difference == 0) {
                System.out.println("Greedy and dynamic programming give the same result.");
            }
            else {
                System.out.println("Dynamic programming gives a better result than greedy.");
            }
            System.out.println();
        }
    }

    private static void bonus() {
        System.out.println("Bonus:");
        ProblemGenerator randomProblem = new ProblemGenerator(1000, 100, 50);
        randomProblem.generate();

        KnapsackGenerator randomKnapsack = new KnapsackGenerator(50, 500);
        randomKnapsack.generate();
        System.out.println(randomKnapsack);

        LongestPath graph = new LongestPath(randomProblem, randomKnapsack);
        Runtime runtime = Runtime.getRuntime();
        graph.solve();
        System.out.print(graph);
        System.out.println(String.format("Used memory in megabytes = %.3f", (double)(runtime.totalMemory() - runtime.freeMemory()) / 1024L / 1024L));
    }

    public static void main(String[] args) {
        compulsory();
        optional();
        bonus();
    }
}
