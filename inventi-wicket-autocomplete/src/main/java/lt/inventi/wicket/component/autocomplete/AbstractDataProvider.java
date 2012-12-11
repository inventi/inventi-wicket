package lt.inventi.wicket.component.autocomplete;

import java.util.HashMap;
import java.util.Map;

/**
 * Provides implementation for some of the methods
 *
 */
public abstract class AbstractDataProvider<T> implements AutocompleteDataProvider<T>{

    @Override
    public Map<String, String> getJsonParameters(T item) {
        Map<String, String> map = new HashMap<String, String>();
        map.put(Autocomplete.LABEL_PARAM, getValue(item));
        map.put(Autocomplete.ID_PARAM, getId(item));
        return map;
    }


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