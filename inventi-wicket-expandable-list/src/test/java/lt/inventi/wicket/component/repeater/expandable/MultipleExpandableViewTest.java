package lt.inventi.wicket.component.repeater.expandable;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.protocol.http.mock.MockHttpServletResponse;
import org.apache.wicket.util.tester.WicketTester;
import org.junit.Test;

public class MultipleExpandableViewTest {

    private WicketTester tester = new WicketTester();

    private List<String> view1Objects = new ArrayList<String>();
    private List<String> view2Objects = new ArrayList<String>();

    @Test
    public void addsItemsToSeparateViews() {
        IModel<List<String>> model1 = PropertyModel.of(this, "view1Objects");
        IModel<List<String>> model2 = PropertyModel.of(this, "view2Objects");
        MultipleViewTestPage page = new MultipleViewTestPage(model1, model2);
        tester.startPage(page);

        tester.clickLink(page.view1Link);
        checkScripts("var item=document.createElement('span');item.id='id03';$('#view1container').append(item);");
        assertThat(getView1().size(), is(1));
        assertThat(view1Objects.size(), is(1));
        assertThat(getView2().size(), is(0));
        assertThat(view2Objects.size(), is(0));

        tester.clickLink(page.view2Link);
        checkScripts("var item=document.createElement('span');item.id='id04';$('#view2container').append(item);");
        assertThat(getView1().size(), is(1));
        assertThat(view1Objects.size(), is(1));
        assertThat(getView2().size(), is(1));
        assertThat(view2Objects.size(), is(1));

        tester.clickLink(page.view2Link);
        checkScripts("var item=document.createElement('span');item.id='id15';$('#view2container').append(item);");
        assertThat(getView1().size(), is(1));
        assertThat(view1Objects.size(), is(1));
        assertThat(getView2().size(), is(2));
        assertThat(view2Objects.size(), is(2));
    }

    private ExpandableView<String> getView1() {
        return ((MultipleViewTestPage) tester.getLastRenderedPage()).view1;
    }

    private ExpandableView<String> getView2() {
        return ((MultipleViewTestPage) tester.getLastRenderedPage()).view2;
    }

    private void checkScripts(String... scripts) {
        MockHttpServletResponse response = tester.getLastResponse();
        for (String s : scripts) {
            assertThat(response.getDocument(), containsString(s));
        }
    }

    private class MultipleViewTestPage extends WebPage {
        private final AddNewItemLink<String> view1Link, view2Link;
        private final ExpandableView<String> view1, view2;

        MultipleViewTestPage(final IModel<List<String>> view1Model, final IModel<List<String>> view2Model) {
            super();

            WebMarkupContainer container1 = new WebMarkupContainer("view1container");
            container1.setMarkupId(container1.getId());
            container1.add(view1 = new ExpandableView<String>("view1", view1Model) {
                @Override
                protected void populateItem(Item<String> item) {
                    item.add(new Label("name", item.getModel()));
                }
            });
            add(container1);

            WebMarkupContainer container2 = new WebMarkupContainer("view2container");
            container2.setMarkupId(container2.getId());
            container2.add(view2 = new ExpandableView<String>("view2", view2Model) {
                @Override
                protected void populateItem(Item<String> item) {
                    item.add(new Label("name", item.getModel()));
                }
            });
            add(container2);

            add(view1Link = new AutoAddNewItemLink<String>("addView1", view1));
            add(view2Link = new AutoAddNewItemLink<String>("addView2", view2));
        }
    }
}
