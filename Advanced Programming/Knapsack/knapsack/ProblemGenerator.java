package knapsack;

import java.util.Random;

public class ProblemGenerator extends Problem {
    private Random random;
    private int maxItemNumber;
    private int maxItemValue;
    private int maxItemWeight;

    public ProblemGenerator(int maxItemNumber, int maxItemValue, int maxItemWeight) {
        random = new Random();
        this.maxItemNumber = maxItemNumber;
        this.maxItemValue = maxItemValue;
        this.maxItemWeight = maxItemWeight;
    }

    public int getMaxItemNumber() { return maxItemNumber; }

    public void setMaxItemNumber(int maxItemNumber) {
        this.maxItemNumber = maxItemNumber;
    }

    public int getMaxItemValue() { return maxItemValue; }

    public void setMaxItemValue(int maxItemValue) {
        this.maxItemValue = maxItemValue;
    }

    public int getMaxItemWeight() { return maxItemWeight; }

    public void setMaxItemWeight(int maxItemWeight) {
        this.maxItemWeight = maxItemWeight;
    }

    public void generate() {
        availableItems.clear();
        generateBooks();
        generateFoods();
        generateWeapons();
    }

    private void generateBooks() {
        String[] bookNames = { "To kill a Mockingbird", "1984", "The Great Gatsby",
                "Pride and Prejudice", "The Book Thief", "The Catcher in the Rye" };
        int bookNumber = random.nextInt(maxItemNumber) + 1;
        for (int index = 1; index <= bookNumber; ++index) {
            availableItems.add(new Book("book" + index, bookNames[random.nextInt(bookNames.length)],
                    random.nextInt(maxItemValue) + 1, (random.nextInt(maxItemWeight) + 1) * 100));
        }
    }

    private void generateFoods() {
        String[] foodNames = { "Carrot", "Potato", "Bread", "Cake", "Apple", "Watermelon" };
        int foodNumber = random.nextInt(maxItemNumber) + 1;
        for (int index = 1; index <= foodNumber; ++index) {
            availableItems.add(new Food("food" + index, foodNames[random.nextInt(foodNames.length)],
                    random.nextInt(maxItemWeight) + 1));
        }
    }

    private void generateWeapons() {
        WeaponType[] weaponTypes = WeaponType.values();
        int weaponNumber = random.nextInt(maxItemNumber) + 1;
        for (int index = 1; index <= weaponNumber; ++index) {
            availableItems.add(new Weapon("weapon" + index, weaponTypes[random.nextInt(weaponTypes.length)],
                    random.nextInt(maxItemValue) + 1, random.nextInt(maxItemWeight) + 1));
        }
    }
}
