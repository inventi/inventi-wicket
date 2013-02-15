package lt.inventi.wicket.component.autocomplete;

/**
 * Provides implementation for some of the methods
 */
public abstract class AbstractDataProvider<T> implements AutocompleteDataProvider<T> {

    /**
     * Adds some extra checking for blank id,
     * and invokes doLoadById if its not blank.
     */
    @Override
    public T getObject(String id, String value, T oldItem) {
        if(id != null){
            return doLoadById(id);
        }
        return null;
    }

    protected abstract T doLoadById(String id);
}