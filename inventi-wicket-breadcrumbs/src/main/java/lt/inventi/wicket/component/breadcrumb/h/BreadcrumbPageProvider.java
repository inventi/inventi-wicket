package lt.inventi.wicket.component.breadcrumb.h;

import java.io.Serializable;

import org.apache.wicket.Page;
import org.apache.wicket.core.request.handler.BookmarkablePageRequestHandler;
import org.apache.wicket.core.request.handler.IPageProvider;
import org.apache.wicket.core.request.handler.PageProvider;
import org.apache.wicket.core.request.handler.RenderPageRequestHandler;
import org.apache.wicket.core.request.mapper.StalePageException;
import org.apache.wicket.request.IRequestHandler;
import org.apache.wicket.request.component.IRequestablePage;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.request.mapper.parameter.PageParameters;

public class BreadcrumbPageProvider implements Serializable {

    private final int pageId;
    private final int renderCount;
    private final Class<? extends IRequestablePage> pageClass;
    private final PageParameters params;
    private final boolean isBookmarkable;

    private transient IRequestablePage webPage;

    public BreadcrumbPageProvider(IRequestablePage page) {
        this.webPage = page;
        this.pageId = page.getPageId();
        this.renderCount = page.getRenderCount();
        this.pageClass = (page instanceof Page) ? ((Page) page).getPageClass() : page.getClass();
        this.params = page.getPageParameters();
        this.isBookmarkable = page.isBookmarkable();
    }

    public CharSequence getURL(RequestCycle rc) {
        try {
            return rc.urlFor(getHandler());
        } catch (StalePageException e) {
            if (isBookmarkable) {
                return rc.urlFor(new BookmarkablePageRequestHandler(new PageProvider(pageClass, params)));
            }
            throw new RuntimeException("Non-bookmarkable page " + pageClass + " could not be found for " + getProvider());
        }
    }

    IRequestHandler getHandler() {
        return new RenderPageRequestHandler(getProvider());
    }

    private IPageProvider getProvider() {
        if (webPage != null) {
            return new PageProvider(webPage);
        }
        return new PageProvider(pageId, pageClass, params, renderCount);
    }

}
