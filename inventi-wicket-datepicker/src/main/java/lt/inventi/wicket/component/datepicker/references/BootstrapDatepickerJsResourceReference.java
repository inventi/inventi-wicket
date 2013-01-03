package lt.inventi.wicket.component.datepicker.references;

import static org.apache.wicket.markup.head.JavaScriptHeaderItem.forReference;

import java.util.Arrays;

import org.apache.wicket.Application;
import org.apache.wicket.markup.head.HeaderItem;
import org.apache.wicket.request.resource.JavaScriptResourceReference;

public class BootstrapDatepickerJsResourceReference extends JavaScriptResourceReference {
    private static JavaScriptResourceReference INSTANCE = new BootstrapDatepickerJsResourceReference();

    private BootstrapDatepickerJsResourceReference() {
        super(BootstrapDatepickerJsResourceReference.class, "js/bootstrap-datepicker.js");
    }

    public static JavaScriptResourceReference get() {
        return INSTANCE;
    }

    @Override
    public Iterable<? extends HeaderItem> getDependencies() {
        return Arrays.asList(forReference(Application.get().getJavaScriptLibrarySettings().getJQueryReference()));
    }
}
