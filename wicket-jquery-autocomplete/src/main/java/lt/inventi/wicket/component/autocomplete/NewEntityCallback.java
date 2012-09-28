package lt.inventi.wicket.component.autocomplete;

import java.io.Serializable;

public interface NewEntityCallback<T> extends Serializable {

    public void saved(T newEntity);

}