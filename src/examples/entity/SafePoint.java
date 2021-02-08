package examples.entity;

/*
    Потокобезопасный изменяемый класс
 */
public class SafePoint {
    private int x, y;

    /*
        Приватный конструктор существует, чтобы избежать условия гонки,
        которое возникло бы, если бы копирующий конструктор был реализован как this(p.x, p.y).

        Это пример идиомы захвата приватного конструктора.
     */
    private SafePoint(int[] a) {
        this(a[0], a[1]);
    }

    public SafePoint(SafePoint safePoint) {
        this(safePoint.get());
    }

    public SafePoint(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /*
        Если бы мы предоставили отдельные геттеры для x и y,
        то значения могли бы измениться в промежутке времени
        между моментом получения одной координаты и моментом получения другой
     */
    public synchronized int[] get() {
        return new int[] {x, y};
    }

    public synchronized void set(int x, int y) {
        this.x = x;
        this.y = y;
    }
}
