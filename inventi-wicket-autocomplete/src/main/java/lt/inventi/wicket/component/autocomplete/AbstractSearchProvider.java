package lt.inventi.wicket.component.autocomplete;

import java.util.HashMap;
import java.util.Map;

/**
 * Provides an id and a label parameter.
 */
public abstract class AbstractSearchProvider<S> implements AutocompleteSearchProvider<S>, AutocompleteDataValueProvider<S> {

    @Override
    public Map<String, String> getJsonParameters(S item) {
        Map<String, String> map = new HashMap<String, String>();
        map.put(Autocomplete.LABEL_PARAM, extractSearchLabel(item));
        map.put(Autocomplete.ID_PARAM, extractId(item));
        map.put(Autocomplete.VALUE_PARAM, extractValue(item));
        return map;
    }

    protected abstract String extractSearchLabel(S item);

    protected abstract String extractId(S item);

}