package examples.tracker;

import examples.entity.Point;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/*
    Делегирование обеспечения потокобезопасности классу ConcurrentHashMap

    1) Map<String, MutablePoint> и MutablePoint являются неизменяемыми,
    поэтому при получении данных БОЛЬШЕ НЕ ИСПОЛЬЗУЕТСЯ копирование
    за счёт метода deepCopy() и конструктора MutablePoint(MutablePoint mutablePoint) соответственно

    2) Явная синхронизация БОЛЬШЕ НЕ ИСПОЛЬЗУЕТСЯ,
    т.к. весь доступ к состоянию управляется классом ConcurrentHashMap,
    а все ключи и значения объекта Map неизменяемы
 */
public class DelegatingVehicleTracker {
    private final ConcurrentMap<String, Point> locations;
    private final Map<String, Point> unmodifiableMap;

    public DelegatingVehicleTracker(Map<String, Point> points) {
        locations = new ConcurrentHashMap<>(points);
        unmodifiableMap = Collections.unmodifiableMap(locations);
    }

    /*
        Если поток A вызывает метод getAliveLocations(),
        а поток B позже изменяет местоположение некоторых точек,
        то изменения ОТРАЖАЮТСЯ в объекте Map<String, Point>, возвращаемом потоку A
     */
    public Map<String, Point> getAliveLocations() {
        return unmodifiableMap;
    }

    /*
        Если поток A вызывает метод getCopiedLocations(),
        а поток B позже изменяет местоположение некоторых точек,
        то изменения НЕ ОТРАЖАЮТСЯ в объекте Map, возвращаемом потоку A

        Поскольку содержимое объекта Map является неизменяемым,
        необходимо скопировать только структуру объекта Map<String, Point>, а не его содержимое
     */
    public Map<String, Point> getCopiedLocations() {
        return Collections.unmodifiableMap(new HashMap<>(locations));
    }

    public Point getLocation(String key) {
        return locations.get(key);
    }

    public void setLocation(String key, int x, int y) {
        Point replacedPoint = locations.replace(key, new Point(x, y));
        if (replacedPoint == null) {
            throw new IllegalArgumentException("Invalid vehicle name: " + key);
        }
    }
}
