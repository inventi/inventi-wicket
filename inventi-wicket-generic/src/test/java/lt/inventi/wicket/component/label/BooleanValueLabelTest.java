package lt.inventi.wicket.component.label;

import java.util.Locale;

import org.apache.wicket.Component;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.resource.loader.IStringResourceLoader;
import org.apache.wicket.util.tester.WicketTester;
import org.junit.Before;
import org.junit.Test;

public class BooleanValueLabelTest {
    private WicketTester tester = new WicketTester();

    @Before
    public void setUp() {
        tester.getApplication().getResourceSettings().getStringResourceLoaders().add(new IStringResourceLoader() {
            @Override
            public String loadStringResource(Component component, String key, Locale locale, String style, String variation) {
                if (key.equals("BooleanValueLabel.UNDEFINED")) {
                    return "Unknown";
                } else if (key.equals("BooleanValueLabel.TRUE")) {
                    return "Yes";
                } else if (key.equals("BooleanValueLabel.FALSE")) {
                    return "No";
                }
                throw new IllegalStateException("No value found for key: " + key + ", component: " + component);
            }

            @Override
            public String loadStringResource(Class<?> clazz, String key, Locale locale, String style, String variation) {
                throw new IllegalStateException("No value found for key: " + key);
            }
        });
    }

    @Test
    public void localizesBooleanValue() {
        // We cannot #assertLabel as it calls Label#getDefaultModelObjectAsString
        IModel<Boolean> model = Model.of(true);
        tester.startComponentInPage(new BooleanValueLabel("test", model));
        tester.getResponse().getDocument().contains("Yes");

        model.setObject(false);
        tester.startComponentInPage(new BooleanValueLabel("test", model));
        tester.getResponse().getDocument().contains("No");

        model.setObject((Boolean) null);
        tester.startComponentInPage(new BooleanValueLabel("test", model));
        tester.getResponse().getDocument().contains("Unknown");
    }
}
