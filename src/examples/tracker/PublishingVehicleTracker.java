package examples.tracker;

import examples.entity.SafePoint;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/*
    Трекер транспортных средств, безопасно публикующий базовое состояние

    Используя класс SafePoint, мы можем создать трекер транспортных средств,
    который публикует базовое изменяемое состояние без ущерба для потокобезопасности
 */
public class PublishingVehicleTracker {
    private final Map<String, SafePoint> locations;
    private final Map<String, SafePoint> unmodifiableMap;

    public PublishingVehicleTracker(Map<String, SafePoint> locations) {
        this.locations = new ConcurrentHashMap<>(locations);
        this.unmodifiableMap = Collections.unmodifiableMap(this.locations);
    }

    /*
        Если поток A вызывает метод getAliveLocations(),
        а поток B позже изменяет местоположение некоторых точек,
        то изменения ОТРАЖАЮТСЯ в объекте Map<String, Point>, возвращаемом потоку A
     */
    public Map<String, SafePoint> getAliveLocations() {
        return unmodifiableMap;
    }

    public SafePoint getLocation(String key) {
        return locations.get(key);
    }

    public void setLocation(String key, int x, int y) {
        if (!locations.containsKey(key)) {
            throw new IllegalArgumentException("Invalid vehicle name: " + key);
        }
        locations.get(key).set(x, y);
    }
}
