package shared;

public class BitwiseOperations {

    public static char not(char c) {
        return (char) ~c;
    }

    /**
     * @param n the number to not
     * @param bitCount the number of bits to consider, max 32
     * @return the result of performing an unsigned bitwise not operation on the int
     * @throws IllegalArgumentException if bitCount is greater than 32
     */
    public static int not(int n, int bitCount) {
        if (bitCount > 32) throw new IllegalArgumentException("bitCount is too high: "+bitCount);
        else if (bitCount == 16) return not((char)n);
        long longN = ~n; var sb = new StringBuilder(Long.toUnsignedString(longN, 2));
        sb.reverse().setLength(bitCount); sb.reverse();
        long result = Long.parseUnsignedLong(sb.toString(), 2);
        return result > Integer.MAX_VALUE ? Integer.MAX_VALUE : (int)result;
    }

    public static char or(char c1, char c2) {
        return (char) (c1 | c2);
    }

    public static int or(int n1, int n2) {
        return (int)(Integer.toUnsignedLong(n1) | Integer.toUnsignedLong(n2));
    }

    public static char xor(char c1, char c2) {
        return (char) (c1 ^ c2);
    }

    public static int xor(int n1, int n2) {
        return (int)(Integer.toUnsignedLong(n1) ^ Integer.toUnsignedLong(n2));
    }

    public static char lshift(char c, int i) {
        return (char) (c << i);
    }

    public static int lshift(int n, int distance) {
        return (int)(Integer.toUnsignedLong(n) << distance);
    }

    public static char and(char c1, char c2) {
        return (char) (c1 & c2);
    }

    public static int and(int n1, int n2) {
        return (int)(Integer.toUnsignedLong(n1) & Integer.toUnsignedLong(n2));
    }

    public static char rshift(char c, int i) {
        return (char) (c >> i);
    }

    public static int rshift(int n, int distance) {
        return (int)(Integer.toUnsignedLong(n) >> distance);
    }

}
