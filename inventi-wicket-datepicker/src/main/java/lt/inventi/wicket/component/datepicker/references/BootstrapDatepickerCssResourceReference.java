package lt.inventi.wicket.component.datepicker.references;

import org.apache.wicket.request.resource.CssResourceReference;

public class BootstrapDatepickerCssResourceReference extends CssResourceReference {
    private static CssResourceReference INSTANCE = new BootstrapDatepickerCssResourceReference();

    private BootstrapDatepickerCssResourceReference() {
        super(BootstrapDatepickerCssResourceReference.class, "css/datepicker.css");
    }

    public static CssResourceReference get() {
        return INSTANCE;
    }
}
