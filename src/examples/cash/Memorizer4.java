package examples.cash;

import java.util.concurrent.*;

import static examples.util.ExceptionUtils.launderThrowable;

/*
    Самая идеальная реализация из всех четырёх.

    Здесь не рассматриваются вопросы:
    1) устаревания значений кэша, но это может быть реализовано с помощью подкласса FutureTask,
    который связывает время устаревания с каждым результатом и периодически просматривает кэш на наличие устаревших записей.

    2) вытеснения значений кэша, при котором старые записи удаляются,
    чтобы освободить место для новых, чтобы кэш не потреблял слишком много памяти.
*/
public class Memorizer4<A, V> implements Computable<A, V> {
    private final ConcurrentMap<A, Future<V>> cache = new ConcurrentHashMap<>();
    private final Computable<A, V> computable;

    public Memorizer4(Computable<A, V> computable) {
        this.computable = computable;
    }

    @Override
    public V compute(A arg) throws InterruptedException {
        while (true) {
            Future<V> future = cache.get(arg);
            if (future == null) {
                Callable<V> result = () -> computable.compute(arg);
                FutureTask<V> futureTask = new FutureTask<>(result);
                future = cache.putIfAbsent(arg, futureTask);
                if (future == null) {
                    future = futureTask;
                    futureTask.run();
                }
            }
            try {
                return future.get();
            } catch (CancellationException e) {
                // Удаляется экземпляр Future из кэша, если обнаруживается, что вычисление было отменено.
                cache.remove(arg, future);
            } catch (ExecutionException e) {
                throw launderThrowable(e.getCause());
            }
        }
    }
}
