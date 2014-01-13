package lt.inventi.wicket.component;

import org.apache.wicket.Component;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.util.tester.WicketTester;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;

import lt.inventi.wicket.resource.ResourceSettings;

public class BlankInputHidingBehaviorTest {

    private WicketTester tester = new WicketTester();

    @Before
    public void setUp() throws Exception {
        ResourceSettings.installEmpty(tester.getApplication());
    }

    @Test
    public void resolvesLabelResourceRelativeToParentComponent() {
        TextField<String> field = new TextField<String>("testField");

        tester.startComponentInPage(new TestPanel().add(
                field.add(new BlankInputHidingBehavior(new ResourceModel("blankInputLabel")))));
        
        tester.assertNoErrorMessage();
        String script = String.format("$('#%s').blankInput({id: '%s', label: 'Blank Input Label', focus: '%s'})",
                field.getMarkupId(), field.getMarkupId(), field.getMarkupId());
        assertThat(tester.getLastResponse().getDocument(), containsString(script));
    }

    @Test
    public void doesNotGenerateJavascriptWhenIsDisabled() {
        BlankInputHidingBehavior behavior = new BlankInputHidingBehavior(new ResourceModel("blankInputLabel")) {
            @Override
            public boolean isEnabled(Component component) {
                return false;
            }
        };

        tester.startComponentInPage(new TestPanel().add(
                new TextField<String>("testField").add(behavior)));

        tester.assertNoErrorMessage();
        assertThat(tester.getLastResponse().getDocument(), not(containsString("blankInput")));
    }

    private static class TestPanel extends Panel {
        public TestPanel() {
            super("test");
        }
    }
}
