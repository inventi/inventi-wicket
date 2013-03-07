package lt.inventi.wicket.component.autocomplete;

import java.io.Serializable;

public interface AddNewItemHandler<T> extends Serializable {

    void onNewItem(String input, NewAutocompleteItemCallback<T> callback);

}
