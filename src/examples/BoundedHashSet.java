package examples;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Semaphore;

import static java.util.Collections.synchronizedSet;

/*
    Если разрешение недоступно, то метод acquire блокируется до тех пор, пока не получит его
    (или до тех пор, пока не будет прерван или пока не истечёт время выполнения операции).

    Метод release возвращает разрешение семафору.

    Вы можете думать о методе:
    1) acquire как о потреблении разрешения
    2) release как о создании разрешения
*/

/*
    Блокирующая ограниченная коллекция
*/
public class BoundedHashSet<T> {
    private final Set<T> set;
    private final Semaphore semaphore;

    public BoundedHashSet(int bound) {
        this.set = synchronizedSet(new HashSet<>());
        semaphore = new Semaphore(bound);
    }

    /*
        Операция add приобретает разрешение перед добавлением элемента в базовую коллекцию.
        Если базовая операция add фактически ничего не добавляет, она немедленно освобождает разрешение.
     */
    public boolean add(T t) throws InterruptedException {
        semaphore.acquire();
        boolean wasAdded = false;
        try {
            wasAdded = set.add(t);
            return wasAdded;
        } finally {
            if (!wasAdded) {
                semaphore.release();
            }
        }
    }

    /*
        Успешное выполнение операции remove освобождает разрешение, позволяя добавлять дополнительные элементы.
     */
    public boolean remove(T t) {
        boolean wasRemoved = set.remove(t);
        if (wasRemoved) {
            semaphore.release();
        }
        return wasRemoved;
    }
}
