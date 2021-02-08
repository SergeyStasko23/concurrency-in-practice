package examples.tracker;

import examples.entity.MutablePoint;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/*
    Потокобезопасный класс "MonitorVehicleTracker" c использованием патерна "Монитор",
    который использует непотокобезопасный класс "MutablePoint".

    1) Map<String, MutablePoint> и MutablePoint являются изменяемыми,
    поэтому при получении данных ИСПОЛЬЗУЕТСЯ КОПИРОВАНИЕ
    за счёт метода deepCopy() и конструктора MutablePoint(MutablePoint mutablePoint) соответственно

    2) За счёт п.1 реализация поддерживает потокобезопасность ЧАСТИЧНО

    3) Как правило, п.1 не является проблемой в плане производительности,
    но может стать ей, если набор транспортных средств станет очень большим (см. п.4).

    4) Поскольку метод deepCopy() вызывается из синхронизированного метода,
    внутренняя блокировка трекера удерживается на протяжении всего времени выполнения длительной операции копирования,
    что может привести к ухудшению отзывчивости пользовательского интерфейса при отслеживании множества транспортных средств.

    ***
    Паттерн "Монитор" полезен при создании классов с нуля или составлении классов из объектов, которые не являются потокобезопасными
    ***
 */
public class MonitorVehicleTracker {
    private final Map<String, MutablePoint> locations;

    public MonitorVehicleTracker(Map<String, MutablePoint> locations) {
        this.locations = deepCopy(locations);
    }

    public synchronized Map<String, MutablePoint> getLocations() {
        return deepCopy(locations);
    }

    public synchronized MutablePoint getLocation(String id) {
        MutablePoint location = locations.get(id);
        return location == null ? null : new MutablePoint(location);
    }

    public synchronized void setLocation(String id, int x, int y) {
        MutablePoint location = locations.get(id);
        if (location == null) {
            throw new IllegalArgumentException("No such id: " + id);
        }
        location.setX(x);
        location.setY(y);
    }

    private static Map<String, MutablePoint> deepCopy(Map<String, MutablePoint> locations) {
        Map<String, MutablePoint> result = new HashMap<>();
        locations.forEach((key, value) -> result.put(key, new MutablePoint(value)));
        return Collections.unmodifiableMap(result);
    }
}
