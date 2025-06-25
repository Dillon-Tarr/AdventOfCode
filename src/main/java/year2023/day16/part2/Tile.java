package year2023.day16.part2;

class Tile {
    char character;
    boolean isEnergized;
    boolean hasBeenApproachedWithUpwardMotion;
    boolean hasBeenApproachedWithDownwardMotion;
    boolean hasBeenApproachedWithLeftwardMotion;
    boolean hasBeenApproachedWithRightwardMotion;

    Tile (char character) {
        this.character = character;
    }

    boolean firstPassInThisDirection(char directionOfMotion) {
        isEnergized = true;
        switch (directionOfMotion) {
            case 'u' -> {if (hasBeenApproachedWithUpwardMotion) return false;
                hasBeenApproachedWithUpwardMotion = true; return true;}
            case 'd' -> {
                if (hasBeenApproachedWithDownwardMotion) return false;
                hasBeenApproachedWithDownwardMotion = true; return true;}
            case 'l' -> {
                if (hasBeenApproachedWithLeftwardMotion) return false;
                hasBeenApproachedWithLeftwardMotion = true; return true;}
            case 'r' -> {
                if (hasBeenApproachedWithRightwardMotion) return false;
                hasBeenApproachedWithRightwardMotion = true; return true;}
        }
        return false;
    }
}