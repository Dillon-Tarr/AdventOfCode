package year2023.day10.part2;

public class LoopTile {
    private final int y;
    private final int x;
    private final char character;
    private final char orientationWhenSteppingOnto;

    LoopTile(int y, int x, char character, char orientationWhenSteppingOnto) {
        this.y = y;
        this.x = x;
        this.character = character;
        this.orientationWhenSteppingOnto = orientationWhenSteppingOnto;
    }

    public int getY() {return y;}
    public int getX() {return x;}
    public char getCharacter() {return character;}
    public char getOrientationWhenSteppingOnto() {return orientationWhenSteppingOnto;}
}
