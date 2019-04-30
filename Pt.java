/** An instance is a point (x, y) in the plane. */
public class Pt {
    private int x;
    private int y;

    /** Constructor: an instance for (x, y). */
    public Pt(int x, int y) {
        this.x= x;
        this.y= y;
    }

    /** return true iff if pt is a Pt and
     * this and pt represent the same point. */
    public @Override boolean equals(Object pt) {
        if (!(pt instanceof Pt)) return false;
        return x == ((Pt)pt).x  &&  y == ((Pt)pt).y;
    }

    /** Return an integer that depends on both x and y of this Pt. */
    public @Override int hashCode() {
        return x + y;
    }

}
