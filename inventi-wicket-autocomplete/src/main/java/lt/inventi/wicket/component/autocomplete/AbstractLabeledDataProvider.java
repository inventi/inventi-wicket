package lt.inventi.wicket.component.autocomplete;

import java.util.HashMap;
import java.util.Map;

/**
 * Provides an id and a label parameter.
 */
public abstract class AbstractLabeledDataProvider<T> implements AutocompleteDataProvider<T>{

    @Override
    public Map<String, String> getJsonParameters(T item) {
        Map<String, String> map = new HashMap<String, String>();
        map.put(Autocomplete.LABEL_PARAM, getLabel(item));
        map.put(Autocomplete.ID_PARAM, getId(item));
        return map;
    }

    public String getLabel(T item) {
        return getValue(item);
    }

}