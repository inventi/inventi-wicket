package lt.inventi.wicket.component.numeric.references;

import static java.util.Collections.singleton;
import static org.apache.wicket.markup.head.JavaScriptHeaderItem.forReference;

import org.apache.wicket.Application;
import org.apache.wicket.markup.head.HeaderItem;
import org.apache.wicket.request.resource.JavaScriptResourceReference;

public class AutoNumericJavaScriptResourceReference extends JavaScriptResourceReference {

    private static final AutoNumericJavaScriptResourceReference INSTANCE = new AutoNumericJavaScriptResourceReference();

    public static JavaScriptResourceReference get() {
        return INSTANCE;
    }

    private AutoNumericJavaScriptResourceReference() {
        super(AutoNumericJavaScriptResourceReference.class, "autoNumeric-1.8.0.js");
    }

    @Override
    public Iterable<? extends HeaderItem> getDependencies() {
        return singleton(forReference(Application.get().getJavaScriptLibrarySettings().getJQueryReference()));
    }
}
