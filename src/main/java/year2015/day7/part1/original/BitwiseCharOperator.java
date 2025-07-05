package year2015.day7.part1.original;

class BitwiseCharOperator {
    static char not(char c) {
        return (char) ~c;
    }

    static char or(char c1, char c2) {
        return (char) (c1 | c2);
    }

    static char lshift(char c, int i) {
        return (char) (c << i);
    }

    static char and(char c1, char c2) {
        return (char) (c1 & c2);
    }

    static char rshift(char c, int i) {
        return (char) (c >> i);
    }

}
