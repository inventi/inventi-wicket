package lt.inventi.wicket.component.breadcrumb.h;

import java.lang.annotation.Annotation;

import org.apache.wicket.Application;
import org.apache.wicket.Page;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.request.component.IRequestablePage;
import org.apache.wicket.request.cycle.PageRequestHandlerTracker;

public final class BreadcrumbsSettings {

    private IBreadcrumbPageFilter pageFilter = new IBreadcrumbPageFilter() {
        @Override
        public boolean shouldCreateBreadcrumbFor(IRequestablePage page) {
            if (page instanceof Page) {
                return ((Page) page).visitChildren(BreadcrumbsPanel.class).hasNext();
            }
            return false;
        }
    };

    @SuppressWarnings("unchecked")
    private Class<? extends BookmarkablePageLink<?>> linkTypeToDecorate = (Class<? extends BookmarkablePageLink<?>>) BookmarkablePageLink.class;

    /**
     * Will create breadcrumbs only for pages annotated with the specified
     * annotation type.
     * <p>
     * By default breadcrumbs will be created for all of the pages containing an
     * instance of {@link BreadcrumbsPanel}.
     * <p>
     * Useful if you already use an annotation to distinguish pages which
     * require bound sessions (like {@code RequiresAuthentication}).
     *
     * @param annotation
     *            type of the annotation
     * @return these settings for chaining
     */
    public BreadcrumbsSettings forPagesAnnotatedWith(final Class<? extends Annotation> annotation) {
        this.pageFilter = new IBreadcrumbPageFilter() {
            @Override
            public boolean shouldCreateBreadcrumbFor(IRequestablePage page) {
                return page.getClass().getAnnotation(annotation) != null;
            }
        };
        return this;
    }

    public <T extends BookmarkablePageLink<?>> BreadcrumbsSettings decorateLinksOfType(Class<T> type) {
        this.linkTypeToDecorate = type;
        return this;
    }

    public void install(Application app) {
        app.getComponentPreOnBeforeRenderListeners().add(new BreadcrumbTrailOnBeforeRenderListener(pageFilter));
        app.getComponentInitializationListeners().add(new BookmarkableBreadcrumbPageInitializationListener(linkTypeToDecorate));

        app.getRequestCycleListeners().add(new PageRequestHandlerTracker());
    }

}
