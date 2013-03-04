package lt.inventi.wicket.component;

import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.util.tester.WicketTester;
import org.junit.Test;

import lt.inventi.wicket.resource.ResourceSettings;

public class BlankInputHidingBehaviorTest {

    private WicketTester tester = new WicketTester();

    @Test
    public void resolvesLabelResourceRelativeToParentComponent() {
        ResourceSettings.installEmpty(tester.getApplication());

        tester.startComponentInPage(new TestPanel().add(
            new TextField<String>("testField").add(
                new BlankInputHidingBehavior(new ResourceModel("blankInputLabel")))));
        
        tester.assertNoErrorMessage();
    }

    private static class TestPanel extends Panel {
        public TestPanel() {
            super("test");
        }
    }
}
