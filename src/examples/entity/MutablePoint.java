package examples.entity;

/*
    Непотокобезопасный изменяемый класс
 */
public class MutablePoint {
    private int x, y;

    public MutablePoint() {
        this.x = 0;
        this.y = 0;
    }

    public MutablePoint(MutablePoint mutablePoint) {
        this.x = mutablePoint.getX();
        this.y = mutablePoint.getY();
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }
}
