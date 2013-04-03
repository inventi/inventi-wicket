package lt.inventi.wicket;

import java.util.List;

import org.apache.wicket.Session;
import org.apache.wicket.model.IModel;

import lt.inventi.wicket.component.autocomplete.AbstractDataProvider;
import lt.inventi.wicket.component.autocomplete.AbstractSearchProvider;
import lt.inventi.wicket.component.autocomplete.Autocomplete;

public class PersonAutocomplete extends Autocomplete<String, Person, Person> {

    public PersonAutocomplete(String id) {
        this(id, null);
    }

    public PersonAutocomplete(String id, IModel<Person> model) {
        super(id, model);

        setDataProvider(new PersonProvider());
        PersonSearchProvider searchProvider = new PersonSearchProvider();
        setDataValueProvider(searchProvider);
        setSearchProvider(searchProvider);
    }

    private static class PersonProvider extends AbstractDataProvider<Person> {
        @Override
        public String getId(Person object) {
            return object.id;
        }

        @Override
        protected Person doLoadById(String id) {
            return PersonDatabase.findById(id);
        }
    }

    private static class PersonSearchProvider extends AbstractSearchProvider<Person> {
        @Override
        public List<Person> searchItems(String query, int size) {
            return PersonDatabase.findByNameLike(query, Session.get().getLocale());
        }

        @Override
        public String extractValue(Person item) {
            return item.name;
        }

        @Override
        protected String extractSearchLabel(Person item) {
            return item.name + " (" + item.age + ")";
        }

        @Override
        protected String extractId(Person item) {
            return item.id;
        }
    }
}
