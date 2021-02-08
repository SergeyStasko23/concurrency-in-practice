package examples;

/*
    Простой потокобезопасный счётчик с использованием паттерна "Монитор"
    Инкапсулируется переменная состояния "value"
    Весь доступ к этой переменной состояния осуществляется через синхронизированные методы класса
*/
public class Counter {
    private long value = 0;

    public synchronized long getValue() {
        return value;
    }

    public synchronized long increment() {
        if (value == Long.MAX_VALUE) {
            throw new IllegalStateException("counter overflow");
        }
        return ++value;
    }
}
