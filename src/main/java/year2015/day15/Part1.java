package year2015.day15;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

class Part1 {
    static private final int DAY = 15;
    static private final File INPUT_FILE = new File("input-files/2015/"+DAY+".txt");
    static private final Ingredient[] ingredients = new Ingredient[4];
    static private int highestCookieScore = Integer.MIN_VALUE;

    public static void main(String[] args) {
        long startTime = System.nanoTime();

        getAndProcessInputData();
        testCombinations();

        System.out.println("Highest-scoring cookie's score: "+highestCookieScore);

        System.out.println("\nExecution time in seconds: "+((double) (System.nanoTime()-startTime)/1000000000));
    }

    private static void getAndProcessInputData() {
        try (BufferedReader br = new BufferedReader(new FileReader(INPUT_FILE))) {
            for (int i = 0; i < 4; i++) ingredients[i] = new Ingredient(br.readLine());
        } catch (IOException e) {throw new RuntimeException(e);}
    }

    private static void testCombinations() {
        int capacity, durability, flavor, texture, score;
        for (int i0 = 0; i0 <= 100; i0++) for (int i1 = 0; i1 <= 100; i1++) {
            for (int i2 = 0; i2 <= 100; i2++) for (int i3 = 0; i3 <= 100; i3++) {
                if (i0+i1+i2+i3 != 100) continue;
                capacity = ingredients[0].capacity*i0 + ingredients[1].capacity*i1 + ingredients[2].capacity*i2 + ingredients[3].capacity*i3;
                if (capacity < 1) continue;
                durability = ingredients[0].durability*i0 + ingredients[1].durability*i1 + ingredients[2].durability*i2 + ingredients[3].durability*i3;
                if (durability < 1) continue;
                flavor = ingredients[0].flavor*i0 + ingredients[1].flavor*i1 + ingredients[2].flavor*i2 + ingredients[3].flavor*i3;
                if (flavor < 1) continue;
                texture = ingredients[0].texture*i0 + ingredients[1].texture*i1 + ingredients[2].texture*i2 + ingredients[3].texture*i3;
                if (texture < 1) continue;
                score = capacity*durability*flavor*texture;
                if (score > highestCookieScore) highestCookieScore = score;
            }
        }
    }

}
