package lt.inventi.wicket.component.datepicker.references;

import static org.apache.wicket.markup.head.JavaScriptHeaderItem.forReference;

import java.util.Arrays;

import org.apache.wicket.markup.head.HeaderItem;
import org.apache.wicket.request.resource.JavaScriptResourceReference;

public class BootstrapDatepickerLangJsResourceReference extends JavaScriptResourceReference {
    public BootstrapDatepickerLangJsResourceReference(String language) {
        super(BootstrapDatepickerLangJsResourceReference.class, "js/lang/bootstrap-datepicker." + language + ".js");
    }

    @Override
    public Iterable<? extends HeaderItem> getDependencies() {
        return Arrays.asList(forReference(BootstrapDatepickerJsResourceReference.get()));
    }
}
