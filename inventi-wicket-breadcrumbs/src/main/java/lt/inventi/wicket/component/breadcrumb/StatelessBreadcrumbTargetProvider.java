package lt.inventi.wicket.component.breadcrumb;

import org.apache.wicket.core.request.handler.BookmarkablePageRequestHandler;
import org.apache.wicket.core.request.handler.PageProvider;
import org.apache.wicket.request.IRequestHandler;
import org.apache.wicket.request.component.IRequestablePage;
import org.apache.wicket.request.cycle.RequestCycle;

public class StatelessBreadcrumbTargetProvider implements IBreadcrumbTargetProvider {
    private final Class<? extends IRequestablePage> clazz;

    public StatelessBreadcrumbTargetProvider(Class<? extends IRequestablePage> clazz) {
        this.clazz = clazz;
    }

    @Override
    public IRequestHandler getHandler() {
        return new BookmarkablePageRequestHandler(new PageProvider(clazz));
    }

    @Override
    public CharSequence getURL(RequestCycle rc) {
        return rc.urlFor(getHandler());
    }

}