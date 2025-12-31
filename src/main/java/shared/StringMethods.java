package shared;

public class StringMethods {

    public static String hexToBinary(String hexString) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < hexString.length(); i++) sb.append(switch (hexString.charAt(i)) {
            case '0' -> "0000";
            case '1' -> "0001";
            case '2' -> "0010";
            case '3' -> "0011";
            case '4' -> "0100";
            case '5' -> "0101";
            case '6' -> "0110";
            case '7' -> "0111";
            case '8' -> "1000";
            case '9' -> "1001";
            case 'a', 'A' -> "1010";
            case 'b', 'B' -> "1011";
            case 'c', 'C' -> "1100";
            case 'd', 'D' -> "1101";
            case 'e', 'E' -> "1110";
            case 'f', 'F' -> "1111";
            default -> throw new IllegalStateException("Unexpected value: " + hexString.charAt(i));
        });
        return sb.toString();
    }

    public static String toBinaryString(boolean[] booleanArray) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < booleanArray.length; i++) sb.append(booleanArray[i] ? '1' : '0');
        return sb.toString();
    }

}
