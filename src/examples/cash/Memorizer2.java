package examples.cash;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/*
    Класс Memorizer2 улучшает ужасное параллельное поведение Memorizer1
    путём замены класса HashMap на класс ConcurrentHashMap.

    Класс ConcurrentHashMap потокобезопасен, поэтому теперь нет необходимости
    обеспечивать синхронизацию при доступе к кэширующему объекту Map.

    Здесь обеспечен хороший уровень параллелизма,
    т.к. в один момент времени может работать несколько потоков.

    Недостаток реализации - два потока, одновременно вызывающие метод compute, могут вычислить одно и то же значение.
    В случае использования принципа меморизации это неэффективно, т.к.
    предназначение кэша состоит в том, чтобы предотвратить многократное вычисление одних и тех же данных.

    Проблема с классом Memorizer2 заключается в том, что если один поток начинает выполнение дорогостоящего вычисления,
    другие потоки не знают, что вычисление выполняется, и поэтому могут начать то же самое вычисление.
 */
public class Memorizer2<A, V> implements Computable<A, V> {
    private final Map<A, V> cache = new ConcurrentHashMap<>();
    private final Computable<A, V> computable;

    public Memorizer2(Computable<A, V> computable) {
        this.computable = computable;
    }

    @Override
    public V compute(A arg) throws InterruptedException {
        V result = cache.get(arg);
        if (result == null) {
            result = computable.compute(arg);
            cache.put(arg, result);
        }
        return result;
    }
}
