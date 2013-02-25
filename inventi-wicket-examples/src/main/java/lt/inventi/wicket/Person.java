package lt.inventi.wicket;

import java.io.Serializable;

public class Person implements Serializable {

    public String name;
    public Integer age;

    @Override
    public String toString() {
        return "(" + name + ", " + age + ")";
    }
}
