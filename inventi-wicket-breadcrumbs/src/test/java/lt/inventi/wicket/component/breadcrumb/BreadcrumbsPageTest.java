package lt.inventi.wicket.component.breadcrumb;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.contains;
import static org.junit.Assert.assertThat;

import java.util.Date;
import java.util.List;

import org.apache.wicket.core.request.handler.IPageRequestHandler;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.SubmitLink;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.link.StatelessLink;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.protocol.http.mock.MockHttpServletRequest;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.util.tester.FormTester;
import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.IValidator;
import org.apache.wicket.validation.ValidationError;
import org.junit.Test;

import lt.inventi.wicket.component.breadcrumb.Breadcrumb;
import lt.inventi.wicket.component.breadcrumb.BreadcrumbsOperationsHelper;
import lt.inventi.wicket.component.breadcrumb.BreadcrumbsPanel;
import lt.inventi.wicket.component.breadcrumb.IBreadcrumbTitleProvider;
import lt.inventi.wicket.component.breadcrumb.NextBookmarkablePageLink;
import lt.inventi.wicket.component.breadcrumb.PreviousPageLink;

public class BreadcrumbsPageTest extends BreadcrumbsTests {

    @Test
    public void testStatelessBreadcrumbs(){
        BasePage page = tester
                .startPage(StatelessPage.class);
        assertThat(page.isStateless(), is(true));
        tester.assertDisabled("back");

        //same page created twice
        //should be only one breadcrumb
        page = tester.startPage(StatelessPage.class);
        page = tester.startPage(StatelessPage.class);
        assertThat(breadcrumbTitles(), contains("StatelessPage"));
        assertThat("Page must be bookmarkable", page.isBookmarkable(), is(true));

        //go next, should be 2 breadcrumbs
        tester.clickLink("nextPage");
        page = getLastRenderedPage();
        assertThat(page.isStateless(), is(true));
        assertThat(breadcrumbTitles(), contains("StatelessPage", "StatelessPage"));

        tester.clickLink("back");
        page = getLastRenderedPage();
        assertThat(page.isStateless(), is(true));
        assertThat(page, instanceOf(getLastRenderedPage().getClass()));
        assertThat(breadcrumbTitles(), contains("StatelessPage"));
    }

    @Test
    public void testStatelessDeeperNesting(){

        tester.startPage(StatelessPage.class);
        tester.clickLink("nextPage");
        BasePage page = getLastRenderedPage();
        assertEquals("testValue", page.getPageParameters().get("testKey").toString());
        assertEquals(2, page.getBreadcrumbs().size());

        tester.clickLink("nextPage2");
        page = getLastRenderedPage();
        List<Breadcrumb> breadcrumbList = page.getBreadcrumbs();
        assertEquals("testValue2", page.getPageParameters().get("testKey2").toString());
        assertEquals(3, breadcrumbList.size());

        tester.assertComponent("crumbs:crumbs:0:link", Link.class);
        tester.assertComponent("crumbs:crumbs:1:link", Link.class);
        tester.assertComponent("crumbs:crumbs:2:link", Link.class);


        tester.clickLink("back");
        Class<?> prevPageClass = page.getClass();
        assertEquals(prevPageClass, tester.getLastRenderedPage().getClass());
        breadcrumbList = getLastRenderedPage().getBreadcrumbs();
        assertEquals(2, breadcrumbList.size());


        /*BookmarkablePageLink<?> backLink = (BookmarkablePageLink<?>)page.get("back");
        Assert.assertNotNull("Param with previous page ID doesnt exist",
                backLink.getPageParameters().get("brd").toString());*/

        tester.clickLink("back");
        breadcrumbList = getLastRenderedPage().getBreadcrumbs();
        assertEquals(1, breadcrumbList.size());
    }


    @Test
    public void testStatefullBreadcrumbs(){
        tester.startPage(StatelessPage.class);
        tester.clickLink("goStatefull");
        BasePage page = getLastRenderedPage();
        assertEquals(2, getLastRenderedPage().getBreadcrumbs().size());
        IPageRequestHandler handler = (IPageRequestHandler)getLastRenderedPage()
                .getBreadcrumbs().get(1).getTarget();
        assertFalse("Page must NOT be stateless", page.isStateless());
        assertEquals(page, handler.getPage());

        MockHttpServletRequest request = tester.getLastRequest();
        tester.processRequest(request);
        tester.processRequest(request);
        page = getLastRenderedPage();
        assertEquals(2, getLastRenderedPage().getBreadcrumbs().size());

        //same page, should be still 2 breadcrumbs
        tester.clickLink("form:save");
        page = getLastRenderedPage();
        assertEquals(2, page.getBreadcrumbs().size());

        FormTester formTest = tester.newFormTester("form");
        formTest.setValue("input", "error");
        formTest.submitLink("save", false);
        formTest.submitLink("save", false);
        formTest.submitLink("save", false);
        page = (BasePage) formTest.getForm().getPage();
        assertEquals(2, page.getBreadcrumbs().size());

        tester.clickLink("next");
        assertEquals(3, getLastRenderedPage().getBreadcrumbs().size());

        tester.clickLink("next");
        assertEquals(4, getLastRenderedPage().getBreadcrumbs().size());

        tester.clickLink("back");
        assertEquals(3, getLastRenderedPage().getBreadcrumbs().size());

        tester.clickLink("back");
        assertEquals(2, getLastRenderedPage().getBreadcrumbs().size());

    }

    private BasePage getLastRenderedPage() {
        return (BasePage) tester.getLastRenderedPage();
    }

    public static class BasePage extends WebPage implements IBreadcrumbTitleProvider {
        protected BreadcrumbsOperationsHelper helper;

        public BasePage() {
            super();
        }

        public BasePage(IModel<?> model) {
            super(model);
        }

        public BasePage(PageParameters parameters) {
            super(parameters);
        }

        @Override
        protected void onInitialize() {
            super.onInitialize();
            helper = new BreadcrumbsOperationsHelper(this);
            add(new BreadcrumbsPanel("crumbs"));
        }

        @Override
        public IModel<String> getBreadrumbTitle() {
            return Model.of(getClass().getSimpleName());
        }

        public List<Breadcrumb> getBreadcrumbs() {
            return ((BreadcrumbsPanel) get("crumbs")).getBreadcrumbs();
        }

    }

    public static class StatefullPage extends BasePage {

        private int hashCode = 0;
        private Form form;

        public StatefullPage() {
            super();
        }

        public StatefullPage(WebMarkupContainer panel) {
            super();
            add(panel);
        }

        @Override
        protected void onInitialize() {
            super.onInitialize();

            form = new Form<String>("form");
            TextField<String> input = new TextField<String>("input", new Model<String>());
            input.add(new IValidator<String>(){
                @Override
                public void validate(IValidatable<String> validatable) {
                    if("error".equals(validatable.getValue())){
                        validatable.error(new ValidationError().setMessage("error"));
                    }
                }
            });
            form.add(input);

            form.add(new SubmitLink("save", form){
                @Override
                public void onSubmit() {
                    super.onSubmit();
                }
            });

            add(form);

            add(new Link<Void>("next"){
                @Override
                public void onClick() {
                    helper.setNextResponsePage(new StatefullPage(new WebMarkupContainer("panel")));
                }
            });

            add(new Link<Void>("back"){
                @Override
                public void onClick() {
                    helper.setResponseToPreviousPage();
                }
            });
        }

        @Override
        protected void onBeforeRender(){
            hashCode = new Date().hashCode();
            super.onBeforeRender();
        }
    }

    public static class StatelessPage extends BasePage {
        public StatelessPage() {
            super();
        }

        public StatelessPage(PageParameters parameters) {
            super(parameters);
        }


        @Override
        protected void onInitialize() {
            super.onInitialize();

            PageParameters params = new PageParameters();
            params.set("testKey", "testValue");
            Link<?> link = new NextBookmarkablePageLink<StatelessPage>("nextPage", StatelessPage.class, params);
            add(link);


            params = new PageParameters();
            params.set("testKey2", "testValue2");
            Link<?> link2 = new NextBookmarkablePageLink<StatelessPage>("nextPage2", StatelessPage.class, params);
            add(link2);

            add(new StatelessLink<Void>("goStatefull") {
                @Override
                public void onClick() {
                    helper.setNextResponsePage(new StatefullPage(new WebMarkupContainer("panel")));
                }
            });

            add(new PreviousPageLink("back"));
        }

    }

}
