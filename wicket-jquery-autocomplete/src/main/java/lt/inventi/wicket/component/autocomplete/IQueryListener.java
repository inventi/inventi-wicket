package lt.inventi.wicket.component.autocomplete;

import org.apache.wicket.IRequestListener;
import org.apache.wicket.RequestListenerInterface;

public interface IQueryListener extends IRequestListener{

    /** Redirect listener interface */
    public static final RequestListenerInterface INTERFACE = new RequestListenerInterface(
            IQueryListener.class);

    public void onQuery();

}