package year2018;

import shared.CardinalDirection;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import static shared.CardinalDirection.*;

class Day13 {
    static private final int DAY = 13;
    static private final File INPUT_FILE = new File("input-files/2018/"+DAY+".txt");
    static private final ArrayList<char[]> inputChars = new ArrayList<>();
    static private final ArrayList<Cart> carts = new ArrayList<>();

    static void main() {
        long startTime = System.nanoTime();

        getAndProcessInputData();
        solve();

        System.out.println("\nExecution time in seconds: "+((double) (System.nanoTime()-startTime)/1000000000));
    }

    private static void getAndProcessInputData() {
        try (BufferedReader br = new BufferedReader(new FileReader(INPUT_FILE))) {
            String s = br.readLine();
            while (s != null) {
                inputChars.add(s.toCharArray());
                s = br.readLine();
            }
        } catch (IOException e) {throw new RuntimeException(e);}
        for (int y = 0; y < inputChars.size(); y++) {
            char[] row = inputChars.get(y);
            for (int x = 0; x < row.length; x++) switch (row[x]) {
                case '<' -> { carts.add(new Cart(y, x, WEST)); row[x] = '-'; }
                case '^' -> { carts.add(new Cart(y, x, NORTH)); row[x] = '|'; }
                case '>' -> { carts.add(new Cart(y, x, EAST)); row[x] = '-'; }
                case 'v' -> { carts.add(new Cart(y, x, SOUTH)); row[x] = '|'; }
            }
        }
    }

    private static void solve() {
        int y, x; CardinalDirection orientation; String firstCollisionCoordinates = null;
        while (carts.size() > 1) {
            carts.sort(Cart::compareTo);
            cartLoop: for (int c1 = 0; c1 < carts.size(); c1++) {
                Cart cart = carts.get(c1);
                y = cart.y; x = cart.x;
                orientation = cart.orientation;
                switch (inputChars.get(y)[x]) { // get new orientation
                    case '+' -> {
                        switch (cart.intersectionEncounters++ % 3) {
                            case 0 -> orientation = orientation.left();
                            case 2 -> orientation = orientation.right();
                        }
                    }
                    case '/' -> {
                        switch (orientation) {
                            case NORTH, SOUTH -> orientation = orientation.right();
                            case EAST, WEST -> orientation = orientation.left();
                        }
                    }
                    case '\\' -> {
                        switch (orientation) {
                            case NORTH, SOUTH -> orientation = orientation.left();
                            case EAST, WEST -> orientation = orientation.right();
                        }
                    }
                }
                switch (orientation) { // get new position
                    case NORTH -> y--;
                    case SOUTH -> y++;
                    case EAST -> x++;
                    case WEST -> x--;
                }
                for (int c2 = 0; c2 < carts.size(); c2++) { // check for collision
                    Cart otherCart = carts.get(c2);
                    if (otherCart != cart && otherCart.y == y && otherCart.x == x) {
                        if (firstCollisionCoordinates == null) firstCollisionCoordinates = x+","+y;
                        carts.remove(cart); carts.remove(otherCart);
                        if (c2 < c1) c1-=2; else c1--;
                        continue cartLoop;
                    }
                }
                cart.orientation = orientation; // update cart
                switch (orientation) {
                    case NORTH, SOUTH -> cart.y = y;
                    case EAST, WEST -> cart.x = x;
                }
            }
        }
        Cart lastCart = carts.getFirst();
        String lastCartCoordinates = lastCart.x+","+ lastCart.y;
        System.out.println("\nCoordinates of first collision (part 1 answer): "+firstCollisionCoordinates);
        System.out.println("\nCoordinates of last remaining cart (part 2 answer): "+lastCartCoordinates);
    }

    private static class Cart implements Comparable<Cart> {
        int y, x, intersectionEncounters = 0;
        CardinalDirection orientation;

        Cart(int y, int x, CardinalDirection orientation) {
            this.y = y;
            this.x = x;
            this.orientation = orientation;
        }

        @Override
        public int compareTo(Cart o) {
            if (y != o.y) return Integer.compare(y, o.y);
            else return Integer.compare(x, o.x);
        }
    }

}
