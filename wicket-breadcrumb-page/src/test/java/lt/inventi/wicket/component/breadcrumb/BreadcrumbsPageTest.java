package lt.inventi.wicket.component.breadcrumb;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static junit.framework.Assert.fail;

import java.util.Date;
import java.util.List;

import junit.framework.Assert;
import lt.inventi.wicket.test.BaseNonInjectedTest;

import org.apache.wicket.core.request.handler.IPageRequestHandler;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.SubmitLink;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
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

public class BreadcrumbsPageTest extends BaseNonInjectedTest {


    @Test
    public void testStatelessBreadcrumbs(){
        BreadcrumbsPage page = tester
                .startPage(StatelessPage.class);
        tester.clickLink("back");

        //same page created twice
        //should be only one breadcrumb
        page = tester.startPage(StatelessPage.class);
        page = tester.startPage(StatelessPage.class);
        assertEquals(1, page.getBreadcrumbs().size());
        assertTrue("Page must be bookmarkable", page.isBookmarkable());

        //go next, should be 2 breadcrumbs
        tester.clickLink("nextPage");
        page = getLastRenderedPage();
        assertEquals(2, page.getBreadcrumbs().size());

        tester.clickLink("back");
        page = getLastRenderedPage();
        assertEquals(page.getClass(), tester.getLastRenderedPage().getClass());
        assertEquals(1, page.getBreadcrumbs().size());
    }

    @Test
    public void testSameParametersInstanceException() {
        BreadcrumbsPage page = tester.startPage(StatelessPage.class);
        try {
            page.nextPageLink("link", StatelessPage.class, page.getPageParameters());
            fail("Should not allow to use same page parameters instance");
        } catch (IllegalStateException e) {
        }
    }

    @Test
    public void testStatelessDeeperNesting(){

        tester.startPage(StatelessPage.class);
        tester.clickLink("nextPage");
        BreadcrumbsPage page = getLastRenderedPage();
        assertEquals("testValue", page.getPageParameters().get("testKey").toString());
        assertEquals(2, page.getBreadcrumbs().size());

        tester.clickLink("nextPage2");
        page = getLastRenderedPage();
        List<Breadcrumb> breadcrumbList = page.getBreadcrumbs();
        assertEquals("testValue2", page.getPageParameters().get("testKey2").toString());
        assertEquals(3, breadcrumbList.size());

        tester.assertComponent("breadcrumbs:0:breadcrumb", Link.class);
        tester.assertComponent("breadcrumbs:1:breadcrumb", Link.class);
        tester.assertComponent("breadcrumbs:2:breadcrumb", Link.class);


        tester.clickLink("back");
        Class<?> prevPageClass = page.getClass();
        assertEquals(prevPageClass, tester.getLastRenderedPage().getClass());
        breadcrumbList = getLastRenderedPage().getBreadcrumbs();
        assertEquals(2, breadcrumbList.size());


        BookmarkablePageLink<?> backLink = (BookmarkablePageLink<?>)page.get("back");
        Assert.assertNotNull("Param with previous page ID doesnt exist",
                backLink.getPageParameters().get("brd").toString());

        tester.clickLink("back");
        breadcrumbList = getLastRenderedPage().getBreadcrumbs();
        assertEquals(1, breadcrumbList.size());
    }


    @Test
    public void testStatefullBreadcrumbs(){
        tester.startPage(StatelessPage.class);
        tester.clickLink("goStatefull");
        BreadcrumbsPage page = getLastRenderedPage();
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
        page = (BreadcrumbsPage) formTest.getForm().getPage();
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

    private BreadcrumbsPage getLastRenderedPage() {
        return (BreadcrumbsPage) tester.getLastRenderedPage();
    }

    public static class StatefullPage extends BreadcrumbsPage {

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
                    goNextPage(new StatefullPage(new WebMarkupContainer("panel")));
                }
            });

            add(new Link<Void>("back"){
                @Override
                public void onClick() {
                    goPreviousPage();
                }
            });
        }

        @Override
        protected void onBeforeRender(){
            hashCode = new Date().hashCode();
            super.onBeforeRender();
        }

        @Override
        public IModel<String> getTitleModel() {
            return new Model("title");
        }
    }

    public static class StatelessPage extends BreadcrumbsPage {
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
            Link<?> link
            = nextPageLink("nextPage", StatelessPage.class, params);
            add(link);


            params = new PageParameters();
            params.set("testKey2", "testValue2");
            link
            = nextPageLink("nextPage2", StatelessPage.class, params);
            add(link);

            add(new StatelessLink("goStatefull"){
                @Override
                public void onClick() {
                    goNextPage(new StatefullPage(new WebMarkupContainer("panel")));
                }
            });

            add(previousPageLink("back"));
        }

        @Override
        public IModel<String> getTitleModel() {
            return new Model("");
        }
    }

}
