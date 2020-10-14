package agh.cs.lab2;

public enum MapDirection {
    NORTH,
    SOUTH,
    WEST,
    EAST;

    public String toString() {
        switch (this) {
            case EAST: return "Wschód";
            case WEST: return "Zachód";
            case NORTH: return "Północ";
            case SOUTH: return "Południe";

        }
        return null;
    }

    public MapDirection next() {
        switch (this) {
            case EAST: return SOUTH;
            case WEST: return NORTH;
            case NORTH: return EAST;
            case SOUTH: return WEST;
        }
        return null;
    }

    public MapDirection previous() {
        switch (this) {
            case EAST: return NORTH;
            case WEST: return SOUTH;
            case NORTH: return WEST;
            case SOUTH: return EAST;
        }
        return null;
    }

    public String toUnitVector() {
        switch (this) {
            case EAST: return "(1, 0)";
            case WEST: return "(-1, 0)";
            case NORTH: return "(0, 1)";
            case SOUTH: return "(0, -1)";
        }
        return null;
    }

}
