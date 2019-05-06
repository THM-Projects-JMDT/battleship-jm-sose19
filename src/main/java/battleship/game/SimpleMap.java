package battleship.game;

public class SimpleMap<A,B,C> {
    private A left;
    private B middle;
    private C right;

    public SimpleMap(A left, B middle, C right) {
        this.left = left;
        this.middle = middle;
        this.right = right;
    }

    public A getLeft() {
        return left;
    }
    public B getMiddle() {
        return middle;
    }

    public C getRight() {
        return right;
    }
}
