package shared;

public class BitwiseOperations {

    public static char not(char c) {
        return (char) ~c;
    }

    public static char or(char c1, char c2) {
        return (char) (c1 | c2);
    }

    public static char xor(char c1, char c2) {
        return (char) (c1 ^ c2);
    }

    public static char lshift(char c, int i) {
        return (char) (c << i);
    }

    public static char and(char c1, char c2) {
        return (char) (c1 & c2);
    }

    public static char rshift(char c, int i) {
        return (char) (c >> i);
    }

}
