package examples.cash;

import java.util.Map;
import java.util.concurrent.*;

import static examples.util.ExceptionUtils.launderThrowable;

/*
    Вызов FutureTask.get немедленно возвращает результат вычисления, если он доступен.
    В противном случае вызов блокируется до тех пор, пока результат не будет вычислен, а затем возвращает его.

    Класс Memorizer3 сначала проверяет, было ли запущено соответствующее вычисление
    (в отличие от принятого по умолчанию как “завершённое”, в классе Memorizer2).

    Если это не так, он создает экземпляр класса FutureTask, регистрирует его в экземпляре Map и запускает вычисление.
    В противном случае он ожидает результата существующего вычисления.

    Данная реализация почти идеальна:
    1) хороший параллелизм (в основном, полученный за счёт превосходной реализации параллелизма в классе ConcurrentHashMap)
    2) результат возвращается эффективно, если он уже известен,
    и если вычисление выполняется другим потоком,
    вновь прибывающие потоки терпеливо ожидают результата.

    Недостаток - всё еще существует небольшое окно уязвимости,
    в котором два потока могли бы начать вычисление одного и того же значения.

    Кэширование экземпляра Future вместо значения создает предпосылки к загрязнению кэша:
    если вычисление отменяется или завершается неудачей, то
    дальнейшие попытки вычислить результат также будут указывать на отмену или сбой.
*/
public class Memorizer3<A, V> implements Computable<A, V> {
    private final Map<A, Future<V>> cache = new ConcurrentHashMap<>();
    private final Computable<A, V> computable;

    public Memorizer3(Computable<A, V> computable) {
        this.computable = computable;
    }

    @Override
    public V compute(final A arg) throws InterruptedException {
        Future<V> future = cache.get(arg);
        if (future == null) {
            Callable<V> result = () -> computable.compute(arg);
            FutureTask<V> futureTask = new FutureTask<>(result);
            future = futureTask;
            cache.put(arg, futureTask);
            futureTask.run();
        }
        try {
            return future.get();
        } catch (ExecutionException e) {
            throw launderThrowable(e.getCause());
        }
    }
}
