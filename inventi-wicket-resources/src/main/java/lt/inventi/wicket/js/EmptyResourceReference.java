package lt.inventi.wicket.js;

import org.apache.wicket.request.resource.JavaScriptResourceReference;

public class EmptyResourceReference extends JavaScriptResourceReference {

    public static JavaScriptResourceReference get() {
        return new EmptyResourceReference();
    }

    private EmptyResourceReference() {
        super(EmptyResourceReference.class, "empty");
    }

}
