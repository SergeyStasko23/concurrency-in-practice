package examples;

import examples.entity.Widget;

/*
    Защита состояния с помощью приватной блокировки (поле "lock")
    Предпочтительнее использовать собственноручно созданный приватный объект блокирвоки вместо встроенной блокировки объекта
 */
public class PrivateLock {
    private final Object lock = new Object();   // монитор для блокировки
    private Widget widget;

    void method() {
        synchronized (lock) {
            widget = new Widget();
        }
    }
}
