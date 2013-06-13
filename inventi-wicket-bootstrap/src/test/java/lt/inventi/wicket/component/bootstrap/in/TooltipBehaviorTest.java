package lt.inventi.wicket.component.bootstrap.in;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.Model;
import org.apache.wicket.util.tester.WicketTester;
import org.junit.Test;

import de.agilecoders.wicket.core.markup.html.bootstrap.components.TooltipBehavior;

public class TooltipBehaviorTest {

    private final WicketTester tester = new WicketTester();

    @Test
    public void loadsTooltip() {
        tester.startComponentInPage(new Label("test", "HI").add(new TooltipBehavior(Model.of("Hello!"))));

        tester.assertNoErrorMessage();
    }
}
