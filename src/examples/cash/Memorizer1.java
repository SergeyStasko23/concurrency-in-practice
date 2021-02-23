package examples.cash;

import java.util.HashMap;
import java.util.Map;

/*
    Для обеспечения потокобезопасности метод compute должен быть synchronized.

    Здесь обеспечен плохой пример параллелизма,
    т.к. в один момент времени может работать только один поток,
    а остальные будут простаивать в очереди, ожидая освобождения блокировки.
 */
public class Memorizer1<A, V> implements Computable<A, V> {
    private final Map<A, V> cache = new HashMap<>();
    private final Computable<A, V> computable;

    public Memorizer1(Computable<A, V> computable) {
        this.computable = computable;
    }

    @Override
    public synchronized V compute(A arg) throws InterruptedException {
        V result = cache.get(arg);
        if (result == null) {
            result = computable.compute(arg);
            cache.put(arg, result);
        }
        return result;
    }
}
