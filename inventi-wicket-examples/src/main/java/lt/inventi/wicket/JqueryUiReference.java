package lt.inventi.wicket;

import java.util.Arrays;

import org.apache.wicket.Application;
import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.HeaderItem;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;

import de.agilecoders.wicket.webjars.request.resource.WebjarsCssResourceReference;
import de.agilecoders.wicket.webjars.request.resource.WebjarsJavaScriptResourceReference;

public class JqueryUiReference extends WebjarsJavaScriptResourceReference {

    private static final JqueryUiReference INSTANCE = new JqueryUiReference();

    public static JqueryUiReference get() {
        return INSTANCE;
    }

    private JqueryUiReference() {
        super("/jquery-ui/1.9.2/js/jquery-ui-1.9.2.custom.js");
    }

    @Override
    public Iterable<? extends HeaderItem> getDependencies() {
        return Arrays.<HeaderItem> asList(
            CssHeaderItem.forReference(JqueryUiSmoothnessCssReference.CSS_INSTANCE),
            JavaScriptHeaderItem.forReference(Application.get().getJavaScriptLibrarySettings().getJQueryReference()));
    }

    private static class JqueryUiSmoothnessCssReference extends WebjarsCssResourceReference {
        private static final JqueryUiSmoothnessCssReference CSS_INSTANCE = new JqueryUiSmoothnessCssReference();

        private JqueryUiSmoothnessCssReference() {
            super("/jquery-ui/1.9.2/css/smoothness/jquery-ui-1.9.2.custom.css");
        }
    }
}
