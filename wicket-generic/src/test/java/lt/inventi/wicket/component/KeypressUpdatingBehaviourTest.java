package lt.inventi.wicket.component;

import static junit.framework.Assert.assertEquals;
import lt.inventi.wicket.test.BaseNonInjectedTest;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestHandler;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.util.tester.DummyHomePage;
import org.junit.Before;
import org.junit.Test;

public class KeypressUpdatingBehaviourTest extends BaseNonInjectedTest {

    private KeypressUpdatingBehaviour behaviour;
    private TextField<?> field;

    @Before
    public void before(){
        DummyHomePage page = new DummyHomePage();
        Component field1 = new TextField<String>("update1").setOutputMarkupId(true);
        Component field2 = new TextField<String>("update2").setOutputMarkupId(true);
        page.add(field1, field2);

        behaviour = new KeypressUpdatingBehaviour(field1, field2);

        field = new TextField<String>("id");
        field.add(behaviour);
        page.add(field);
    }

    @Test
    public void testOnUpdate(){
        AjaxRequestTarget target = new AjaxRequestHandler(new DummyHomePage());
        behaviour.onUpdate(target);

        assertEquals(2, target.getComponents().size());
    }

}
