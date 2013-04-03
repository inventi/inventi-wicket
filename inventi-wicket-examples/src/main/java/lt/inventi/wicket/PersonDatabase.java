package lt.inventi.wicket;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public abstract class PersonDatabase {

    private static final Map<String, Person> DATABASE = new ConcurrentHashMap<String, Person>();

    static {
        DATABASE.put("1", new Person("1", "John", 10));
        DATABASE.put("2", new Person("2", "Ed", 20));
        DATABASE.put("3", new Person("3", "Rick", 22));
        DATABASE.put("4", new Person("4", "Mike", 50));
        DATABASE.put("5", new Person("5", "Tim", 54));
    }

    public static Person findById(String id) {
        return DATABASE.get(id);
    }

    public static List<Person> findByNameLike(String query, Locale locale) {
        List<Person> result = new ArrayList<Person>();
        for (Person p : DATABASE.values()) {
            if (p.name.toLowerCase(locale).startsWith(query.toLowerCase(locale))) {
                result.add(p);
            }
        }
        return result;
    }
}
