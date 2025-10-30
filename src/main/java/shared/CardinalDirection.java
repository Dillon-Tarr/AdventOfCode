package shared;

public enum CardinalDirection {
    NORTH,
    SOUTH,
    WEST,
    EAST,
    NONE;

    public static CardinalDirection rotateLeft(CardinalDirection direction) {
        return switch (direction) {
            case EAST -> NORTH;
            case NORTH -> WEST;
            case WEST -> SOUTH;
            case SOUTH -> EAST;
            case NONE -> NONE;
        };
    }

}
