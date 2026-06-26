package kadra.mapki.graf;

/**
 * Punkt na płaszczyźnie.
 */
public record Punkt(int x, int y) {

    public int[] wspolrzedne() {
        return new int[] {x, y};
    }

    public double odleglosc(Punkt drugi) {
        int deltaX = this.x - drugi.x;
        int deltaY = this.y - drugi.y;
        return Math.sqrt(deltaX * deltaX + deltaY * deltaY);
    }

    @Override
    public String toString() {
        return "Punkt{x=%d, y=%d}".formatted(x, y);
    }
}
