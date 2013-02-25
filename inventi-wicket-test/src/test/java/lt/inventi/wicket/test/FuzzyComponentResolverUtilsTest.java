package lt.inventi.wicket.test;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.apache.wicket.Component;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.IFormSubmittingComponent;
import org.apache.wicket.util.tester.WicketTester;
import org.junit.Test;

public class FuzzyComponentResolverUtilsTest {

    @SuppressWarnings("unused")
    private WicketTester tester = new WicketTester();

    private WebMarkupContainer parent = setUpRoot();

    @Test
    public void findsComponentsWithFuzzyPath() {
        WebMarkupContainer result = FuzzyComponentResolverUtils.findComponent(parent, "root", WebMarkupContainer.class);
        assertThat(result, is(parent.get("root")));

        result = FuzzyComponentResolverUtils.findComponent(parent, "root:child1", WebMarkupContainer.class);
        assertThat(result, is(parent.get("root:child1")));

        result = FuzzyComponentResolverUtils.findComponent(parent, "child1", WebMarkupContainer.class);
        assertThat(result, is(parent.get("root:child1")));

        result = FuzzyComponentResolverUtils.findComponent(parent, "child1:child", WebMarkupContainer.class);
        assertThat(result, is(parent.get("root:child1:child")));

        result = FuzzyComponentResolverUtils.findComponent(parent, "child12", WebMarkupContainer.class);
        assertThat(result, is(parent.get("root:child1:child12")));

        result = FuzzyComponentResolverUtils.findComponent(parent, "child12:child:child", WebMarkupContainer.class);
        assertThat(result, is(parent.get("root:child1:child12:child:child")));

        result = FuzzyComponentResolverUtils.findComponent(parent, "root:child12", WebMarkupContainer.class);
        assertThat(result, is(parent.get("root:child1:child12")));

        result = FuzzyComponentResolverUtils.findComponent(parent, "root:child122", WebMarkupContainer.class);
        assertThat(result, is(parent.get("root:child1:child12:child122")));
    }

    @Test(expected = IllegalArgumentException.class)
    public void doesntFindIncompleteMatch() {
        FuzzyComponentResolverUtils.findComponent(parent, "root:child1:non-existent", WebMarkupContainer.class);
    }

    @Test
    public void cannotDecideBetweenMultiplePrimaryChoices() {
        // Primary choice is a choice which ends with the search path or consists of the search path entirely
        try {
            FuzzyComponentResolverUtils.findComponent(parent, "a", WebMarkupContainer.class);
        } catch (IllegalArgumentException e) {
            assertThat(e.getMessage(), containsString("root:child2:a"));
            assertThat(e.getMessage(), containsString("root:child1:child12:child122:a"));
        }
    }

    @Test
    public void resolvesComponentByType() {
        Component result = FuzzyComponentResolverUtils.findComponent(parent, "submit", Form.class);
        assertThat(result, is(parent.get("root:submit")));

        result = (Component) FuzzyComponentResolverUtils.findComponent(parent, "submit", IFormSubmittingComponent.class);
        assertThat(result, is(parent.get("root:submit:submit")));
    }

    private static WebMarkupContainer setUpRoot() {
        WebMarkupContainer result = new WebMarkupContainer("parent");
        WebMarkupContainer r = new WebMarkupContainer("root");
        result.add(r);

        WebMarkupContainer child1 = new WebMarkupContainer("child1");
        // root
        r.add(child1);

        WebMarkupContainer child12 = new WebMarkupContainer("child12");
        // first level
        child1.add(new WebMarkupContainer("child"), child12);

        WebMarkupContainer child121 = new WebMarkupContainer("child");
        WebMarkupContainer child122 = new WebMarkupContainer("child122");
        // second level
        child12.add(child121, child122);

        // third level
        child122.add(new WebMarkupContainer("a"), new WebMarkupContainer("b"));

        // third level
        child121.add(new WebMarkupContainer("child"));

        WebMarkupContainer child2 = new WebMarkupContainer("child2");
        // root
        r.add(child2);

        WebMarkupContainer child21 = new WebMarkupContainer("child");
        WebMarkupContainer child22 = new WebMarkupContainer("child22");
        // first level
        child2.add(new WebMarkupContainer("a"), child21, child22);

        WebMarkupContainer child221 = new WebMarkupContainer("child");
        // second level
        child22.add(child221);

        // third level
        child221.add(new WebMarkupContainer("child"));

        Form<?> child3 = new Form<Void>("submit");
        // root
        r.add(child3);
        // first level
        child3.add(new Button("submit"));

        return result;
    }
}
