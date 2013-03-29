package lt.inventi.wicket.component.autocomplete;

import java.io.Serializable;

public interface AutocompleteDataValueProvider<T> extends Serializable {

    String extractValue(T item);

}
