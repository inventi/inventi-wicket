package lt.inventi.wicket.component.autocomplete;

import org.apache.wicket.request.resource.CssResourceReference;

public class AutocompleteCssResourceReference extends CssResourceReference {
    private static final long serialVersionUID = 6795863553105608280L;

    private static AutocompleteCssResourceReference instance = new AutocompleteCssResourceReference("basic");

    public static AutocompleteCssResourceReference get() {
        return instance;
    }

    public AutocompleteCssResourceReference(String theme) {
        super(AutocompleteCssResourceReference.class, theme + "/autocomplete.custom.css");
    }
}
