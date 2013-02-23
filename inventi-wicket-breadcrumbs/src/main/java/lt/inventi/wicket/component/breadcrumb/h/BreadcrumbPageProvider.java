package lt.inventi.wicket.component.breadcrumb.h;

import java.io.Serializable;
import java.lang.ref.WeakReference;

import org.apache.wicket.Page;
import org.apache.wicket.WicketRuntimeException;
import org.apache.wicket.core.request.handler.BookmarkablePageRequestHandler;
import org.apache.wicket.core.request.handler.IPageProvider;
import org.apache.wicket.core.request.handler.PageProvider;
import org.apache.wicket.core.request.handler.RenderPageRequestHandler;
import org.apache.wicket.request.IRequestHandler;
import org.apache.wicket.request.component.IRequestablePage;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.request.mapper.parameter.PageParameters;

class BreadcrumbPageProvider implements Serializable {

    private final int pageId;
    private final Class<? extends IRequestablePage> pageClass;
    private final PageParameters params;
    private final boolean isBookmarkable;

    private transient WeakReference<IRequestablePage> webPage;

    public BreadcrumbPageProvider(IRequestablePage page) {
        this.webPage = new WeakReference<IRequestablePage>(page);
        this.pageId = page.getPageId();
        this.pageClass = (page instanceof Page) ? ((Page) page).getPageClass() : page.getClass();
        this.params = page.getPageParameters();
        this.isBookmarkable = page.isBookmarkable();
    }

    CharSequence getURL(RequestCycle rc) {
        try {
            return rc.urlFor(getHandler());
        } catch (WicketRuntimeException e) {
            if (isBookmarkable) {
                return rc.urlFor(new BookmarkablePageRequestHandler(new PageProvider(pageClass, params)));
            }
            throw new IllegalStateException("Cannot render link for " + pageClass + ", " + pageId + " !", e);
        }
    }

    IRequestHandler getHandler() {
        return new RenderPageRequestHandler(getProvider());
    }

    private IPageProvider getProvider() {
        if (webPage != null) {
            IRequestablePage page = webPage.get();
            if (page != null) {
                return new PageProvider(page);
            }
            webPage = null;
        }
        // We don't care about render count as it's only used for safeguarding
        // against destructive actions (usually performed with AJAX,
        // e.g. modifying a list of items on a single page in several browser tabs).
        return new PageProvider(pageId, pageClass, params, null);
    }

    void detach() {
        this.webPage = null;
    }

    @Override
    public String toString() {
        return pageClass.getSimpleName() + "@" + pageId +
            (params == null || params.isEmpty() ? "" : "?" + params);
    }
}
