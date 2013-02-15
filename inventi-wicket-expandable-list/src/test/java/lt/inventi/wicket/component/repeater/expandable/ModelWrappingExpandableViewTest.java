package lt.inventi.wicket.component.repeater.expandable;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.MarkupContainer;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.util.tester.WicketTester;
import org.junit.Test;

public class ModelWrappingExpandableViewTest {
    private WicketTester tester = new WicketTester();

    private List<TestObject> viewObjects = new ArrayList<TestObject>();

    @Test
    public void properlyAddsNewItemsWhenModelsAreWrapped() {
        IModel<List<TestObject>> model = PropertyModel.of(this, "viewObjects");
        TestPage page = tester.startPage(new TestPage(model));

        tester.clickLink(page.addLink);
        assertThat(getView().size(), is(1));
        assertThat(getView().get("0:name").getDefaultModelObjectAsString(), equalTo("0"));
        assertThat(viewObjects.size(), is(1));

        tester.clickLink(page.addLink);
        assertThat(getView().size(), is(2));
        assertThat(getView().get("0:name").getDefaultModelObjectAsString(), equalTo("0"));
        assertThat(getView().get("1:name").getDefaultModelObjectAsString(), equalTo("1"));
        assertThat(viewObjects.size(), is(2));

        tester.executeAjaxEvent(getView().get("0:remove"), "click");
        assertThat(getView().size(), is(1));
        assertThat(getView().get("1:name").getDefaultModelObjectAsString(), equalTo("1"));
        assertThat(viewObjects.size(), is(1));
    }

    private MarkupContainer getView() {
        return ((TestPage) tester.getLastRenderedPage()).view;
    }

    @SuppressWarnings("unused")
    private static class TestObject {
        String name;
        String description;

        public TestObject(String name, String description) {
            this.name = name;
            this.description = description;
        }
    }

    private static class TestPage extends WebPage {
        private final AddNewItemLink<TestObject> addLink;
        private final ExpandableView<TestObject> view;

        TestPage(final IModel<List<TestObject>> viewModel) {
            super();

            WebMarkupContainer container1 = new WebMarkupContainer("viewContainer");
            container1.setMarkupId(container1.getId());
            container1.add(view = new ExpandableView<TestObject>("view", viewModel) {
                @Override
                protected void populateItem(Item<TestObject> item) {
                    item.setModel(CompoundPropertyModel.of(item.getModel()));
                    item.add(new Label("name"));
                    item.add(new Label("description"));
                    item.add(new AutoRemoveItemLink<TestObject>("remove", item));
                }
            });
            add(container1);

            add(addLink = new AutoAddNewItemLink<TestObject>("addLink", view) {
                private int counter = 0;
                @Override
                protected TestObject createNewItem() {
                    return new TestObject(String.valueOf(counter++), null);
                }
            });
        }
    }
}
