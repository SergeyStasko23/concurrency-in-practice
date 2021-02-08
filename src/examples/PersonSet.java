package examples;

import examples.entity.Person;

import java.util.HashSet;
import java.util.Set;

// Использование ограничения для обеспечения потокобезопасности,
// несмотря на то, что состояние (поле "set") объекта "PersonSet" непотокобезопасное
public class PersonSet {
    private final Set<Person> set = new HashSet<>();

    public synchronized void addPerson(Person person) {
        set.add(person);
    }

    public synchronized boolean isContain(Person person) {
        return set.contains(person);
    }
}
