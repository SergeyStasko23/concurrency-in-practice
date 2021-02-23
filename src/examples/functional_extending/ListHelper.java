package examples.functional_extending;

import java.util.ArrayList;
import java.util.List;

import static java.util.Collections.synchronizedList;

/*
    ListHelper - менее надёжная версия, чем ImprovedList
 */
public class ListHelper<E> {
    private final List<E> list = synchronizedList(new ArrayList<>());

    /*
        Непотокобезопасно, т.к. для синхронизации используется неверная блокировка
     */
    public synchronized boolean putIfAbsentAsNotThreadSafe(E e) {
        boolean absent = !list.contains(e);
        if (absent) {
            list.add(e);
        }
        return absent;
    }

    /*
        Потокобезопасно, т.к. для синхронизации используется собственная блокировка
     */
    public boolean putIfAbsentAsThreadSafe(E e) {
        synchronized (list) {
            boolean absent = !list.contains(e);
            if (absent) {
                list.add(e);
            }
            return absent;
        }
    }
}
