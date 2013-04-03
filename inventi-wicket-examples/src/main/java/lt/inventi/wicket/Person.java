package lt.inventi.wicket;

import java.io.Serializable;

public class Person implements Serializable {

    public String id;
    public String name;
    public Integer age;
    public Person bestFriend;

    public Person() {
        // empty
    }

    public Person(String id, String name, Integer age) {
        this.id = id;
        this.name = name;
        this.age = age;
    }

    @Override
    public String toString() {
        return "(" + id + ", " + name + ", " + age + ")" + (bestFriend == null ? "" : ", best friend: " + bestFriend.name);
    }
}
