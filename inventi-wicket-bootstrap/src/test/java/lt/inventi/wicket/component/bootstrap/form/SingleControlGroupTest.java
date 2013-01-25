package lt.inventi.wicket.component.bootstrap.form;

import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.util.tester.WicketTester;
import org.junit.Test;

public class SingleControlGroupTest {

    private final WicketTester tester = new WicketTester();

    @Test
    public void rendersControlGroupWithASingleInput() {
        TestPanel panel = new TestPanel();
        SingleControlGroup group = new SingleControlGroup(new TextField<String>("input"));
        panel.add(group);

        tester.startComponentInPage(panel);

        tester.assertComponent("test:input:input:input_body:input", TextField.class);
    }

    private static class TestPanel extends Panel {
        private TestPanel() {
            super("test");
        }
    }
}
