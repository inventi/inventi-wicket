package lt.inventi.wicket.component.autocomplete;

import java.io.Serializable;

public interface AutocompleteDataLabelProvider<T> extends Serializable {

    String extractLabel(T item);

}
