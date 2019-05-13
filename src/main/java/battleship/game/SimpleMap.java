package battleship.game;

public class SimpleMap<A,C> {
    private A left;
    private C right;

    public SimpleMap(A left, C right) {
        this.left = left;
        this.right = right;
    }

    public A getLeft() {
        return left;
    }

    public C getRight() {
        return right;
    }

    public void setLeft(A left) {
        this.left = left;
    }

    public void setRight(C right) {
        this.right = right;
    }
}
