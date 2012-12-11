package lt.inventi.wicket.component.breadcrumb;

import java.util.List;

import org.apache.wicket.core.request.handler.PageProvider;
import org.apache.wicket.core.request.handler.RenderPageRequestHandler;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.IRequestHandler;
import org.apache.wicket.request.mapper.parameter.PageParameters;

public abstract class BreadcrumbsPage extends WebPage {

    private Breadcrumbs breadcrumbs;

    public BreadcrumbsPage() {
        super();
        onInitializePage();
    }

    public BreadcrumbsPage(IModel<?> model) {
        super(model);
        onInitializePage();
    }

    public BreadcrumbsPage(PageParameters parameters) {
        super(parameters);
        onInitializePage();
    }

    protected abstract IModel<String> getTitleModel();

    private void onInitializePage() {
        breadcrumbs = new Breadcrumbs();
        add(breadcrumbs);
        super.onInitialize();
    }

    /**
     * Creates a breadcrumb with the same page parameters as used in this page.
     *
     * @param id
     * @param pageClass
     * @return a breadcrumb link
     */
    public Link<?> nextPageLink(String id, Class<? extends BreadcrumbsPage> pageClass) {
        return new NextPageLink(id, pageClass);
    }

    public Link<?> nextPageLink(String id, Class<? extends BreadcrumbsPage> pageClass, PageParameters params) {
        Breadcrumbs.checkParametersInstance(getPage(), params);
        return new NextPageLink(id, pageClass, params);
    }

    public void goNextPage(BreadcrumbsPage page) {
        page.breadcrumbs.setBreadcrumbTrail(this);
        setResponsePage(page);
    }

    public void goNextPage(Class<? extends BreadcrumbsPage> pageClass, PageParameters params) {
        Breadcrumbs.getContainer().storeBreadcrumbTrail(new Breadcrumb(getPage()),
                Breadcrumbs.getBreadcrumbTrail(getPage().getPageParameters()));
        params.set(Breadcrumbs.BRD, new Breadcrumb(getPage()).getId());
        setResponsePage(pageClass, params);
    }

    public void goPreviousPage() {
        IRequestHandler previous = breadcrumbs.getPreviousPageHandler();
        if (previous == null) {
            previous = new RenderPageRequestHandler(
                    new PageProvider(getClass()),
                    RenderPageRequestHandler.RedirectPolicy.ALWAYS_REDIRECT);
        }

        getRequestCycle().scheduleRequestHandlerAfterCurrent(previous);
    }

    public boolean hasPreviousPage() {
        return breadcrumbs.getPreviousPageHandler() != null;
    }

    public Link<?> previousPageLink(String id) {
        Link<?> link = breadcrumbs.getPreviousBookmarkableBreadcrumbLink(id);
        if (link == null) {
            link = new BookmarkablePageLink<Void>(id, getClass(),
                    new PageParameters());
        }
        return link;
    }

    List<Breadcrumb> getBreadcrumbs() {
        return breadcrumbs.getBreadcrumbs();
    }
}
