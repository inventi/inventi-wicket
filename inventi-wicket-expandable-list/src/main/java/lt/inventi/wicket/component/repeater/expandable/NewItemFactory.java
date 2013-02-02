package lt.inventi.wicket.component.repeater.expandable;

import java.io.Serializable;

public interface NewItemFactory<T> extends Serializable {

    T createNewItem();

}
