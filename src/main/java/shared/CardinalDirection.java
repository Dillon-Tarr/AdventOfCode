package shared;

public enum CardinalDirection {
    NORTH {
        public CardinalDirection left() { return WEST; }
        public CardinalDirection right() { return EAST; }
        public CardinalDirection reverse() { return SOUTH; }
    }, SOUTH {
        public CardinalDirection left() { return EAST; }
        public CardinalDirection right() { return WEST; }
        public CardinalDirection reverse() { return NORTH; }
    }, WEST {
        public CardinalDirection left() { return SOUTH; }
        public CardinalDirection right() { return NORTH; }
        public CardinalDirection reverse() { return EAST; }
    }, EAST {
        public CardinalDirection left() { return NORTH; }
        public CardinalDirection right() { return SOUTH; }
        public CardinalDirection reverse() { return WEST; }
    }, NONE {
        public CardinalDirection left() { return NONE; }
        public CardinalDirection right() { return NONE; }
        public CardinalDirection reverse() { return NONE; }
    };

    /**Returns the direction which would result from a 90-degree left turn from {@code this} direction:
     * EAST > NORTH > WEST > SOUTH > EAST; NONE > NONE;**/
    public abstract CardinalDirection left();
    /**Returns the direction which would result from a 90-degree right turn from {@code this} direction:
     * EAST > SOUTH > WEST > NORTH > EAST; NONE > NONE;**/
    public abstract CardinalDirection right();
    /**Returns the direction which would result from a 180-degree turn from {@code this} direction:
     * EAST >< WEST; NORTH >< SOUTH; NONE >< NONE;**/
    public abstract CardinalDirection reverse();

}
