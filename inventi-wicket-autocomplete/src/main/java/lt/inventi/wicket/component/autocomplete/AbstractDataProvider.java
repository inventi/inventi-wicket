package lt.inventi.wicket.component.autocomplete;

import org.apache.commons.lang.StringUtils;

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
        if (!StringUtils.isEmpty(id)) {
            return doLoadById(id);
        }
        if (!StringUtils.isEmpty(value)) {
            return findExactByValue(value);
        }
        return null;
    }

    protected T findExactByValue(String value) {
        return null;
    }

    protected abstract T doLoadById(String id);
}