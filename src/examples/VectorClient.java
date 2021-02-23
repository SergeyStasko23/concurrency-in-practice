package examples;

import java.util.Vector;
import java.util.function.Consumer;

/*
    Несмотря на то, что Vector является потокобезопасным,
    при использовании его методов через клиентский класс
    необходимы дополнительные блокировки на уровне этого самого клиента
 */
public class VectorClient {
    public static Object getLast(Vector list) {
        synchronized (list) {
            return list.get(list.size() - 1);
        }
    }

    public static void deleteLast(Vector list) {
        synchronized (list) {
            list.remove(list.size() - 1);
        }
    }

    /*
        Блокировка коллекции при итерировании - плохое решение,
        т.к. из-за больших размеров коллекции другие потоки могут очень долго простаивать в ожидании её освобождения,
        что способствует ухудшению параллелизма

        Лучшая альтернатива блокировке коллекции - клонирование коллекции и итерирование копии,
        поскольку в таком случае клон ограничен только одним потоком
     */
    public static void iterator(Vector list, Consumer<Object> consumer) {
        synchronized (list) {
            for (int i = 0; i < list.size() - 1; i++) {
                Object item = list.get(i);
                consumer.accept(item);
            }
        }
    }
}
