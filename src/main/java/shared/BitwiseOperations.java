package shared;

public class BitwiseOperations {

    public static char not(char c) {
        return (char) ~c;
    }

    public static int not(int n) {
        String binaryString = Integer.toUnsignedString(n, 2);
        var sb = new StringBuilder();
        for (int i = 0; i < binaryString.length(); i++) sb.append(binaryString.charAt(i) == '0' ? '1' : '0');
        return Integer.parseUnsignedInt(sb.toString(), 2);
    }

    public static char or(char c1, char c2) {
        return (char) (c1 | c2);
    }

    public static int or(int n1, int n2) {
        var bs1 = new StringBuilder(Integer.toUnsignedString(n1, 2));
        var bs2 = new StringBuilder(Integer.toUnsignedString(n2, 2));
        normalizeBinaryStrings(bs1, bs2);
        var sb = new StringBuilder();
        for (int i = 0; i < bs1.length(); i++) sb.append((bs1.charAt(i) == '1' || bs2.charAt(i) == '1') ? '1' : '0');
        return Integer.parseUnsignedInt(sb.toString(), 2);
    }

    public static char xor(char c1, char c2) {
        return (char) (c1 ^ c2);
    }

    public static int xor(int n1, int n2) {
        var bs1 = new StringBuilder(Integer.toUnsignedString(n1, 2));
        var bs2 = new StringBuilder(Integer.toUnsignedString(n2, 2));
        normalizeBinaryStrings(bs1, bs2);
        var sb = new StringBuilder();
        for (int i = 0; i < bs1.length(); i++) {
            char c1 = bs1.charAt(i), c2 = bs2.charAt(i);
            sb.append(((c1 == '1' && c2 == '0') || (c1 == '0' && c2 == '1')) ? '1' : '0');
        }
        return Integer.parseUnsignedInt(sb.toString(), 2);
    }

    public static char lshift(char c, int i) {
        return (char) (c << i);
    }

    public static int lshift(int n, int distance) {
        if (distance < 0) throw new IllegalArgumentException("Illegal shift distance: "+distance);
        var sb = new StringBuilder(Integer.toUnsignedString(n, 2));
        sb.repeat('0', distance);
        return Integer.parseUnsignedInt(sb.toString(), 2);
    }

    public static char and(char c1, char c2) {
        return (char) (c1 & c2);
    }

    public static int and(int n1, int n2) {
        var bs1 = new StringBuilder(Integer.toUnsignedString(n1, 2));
        var bs2 = new StringBuilder(Integer.toUnsignedString(n2, 2));
        normalizeBinaryStrings(bs1, bs2);
        var sb = new StringBuilder();
        for (int i = 0; i < bs1.length(); i++) sb.append((bs1.charAt(i) == '1' && bs2.charAt(i) == '1') ? '1' : '0');
        return Integer.parseUnsignedInt(sb.toString(), 2);
    }

    public static char rshift(char c, int i) {
        return (char) (c >> i);
    }

    public static int rshift(int n, int distance) {
        if (distance < 0) throw new IllegalArgumentException("Illegal shift distance: "+distance);
        var sb = new StringBuilder(Integer.toUnsignedString(n, 2));
        sb.setLength(Math.max(sb.length()-distance, 0));
        return Integer.parseUnsignedInt(sb.toString(), 2);
    }

    private static void normalizeBinaryStrings(StringBuilder bs1, StringBuilder bs2) {
        int bs1Length = bs1.length(), bs2Length = bs2.length();
        if (bs1Length > bs2Length) bs2.insert(0, "0".repeat(bs1Length-bs2Length));
        else if (bs2Length > bs1Length) bs1.insert(0, "0".repeat(bs2Length-bs1Length));
    }

}
