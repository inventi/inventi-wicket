package lt.inventi.wicket.component;

import org.apache.wicket.request.resource.JavaScriptResourceReference;
import org.apache.wicket.request.resource.ResourceReference;

public class ThrottleDebounceResourceReference extends JavaScriptResourceReference {

    private static ResourceReference instance = new ThrottleDebounceResourceReference();

    protected ThrottleDebounceResourceReference() {
        super(ThrottleDebounceResourceReference.class, "jquery.ba-throttle-debounce.js");
    }

    public static ResourceReference get() {
        return instance;
    }

}
