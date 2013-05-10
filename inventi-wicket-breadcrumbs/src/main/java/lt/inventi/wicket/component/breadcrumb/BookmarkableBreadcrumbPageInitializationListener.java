package lt.inventi.wicket.component.breadcrumb;

import java.lang.reflect.Field;
import java.security.AccessController;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;

import org.apache.wicket.Component;
import org.apache.wicket.application.IComponentInitializationListener;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


class BookmarkableBreadcrumbPageInitializationListener implements IComponentInitializationListener {

    private static final Logger logger = LoggerFactory.getLogger(BookmarkableBreadcrumbPageInitializationListener.class);

    private final Class<? extends BookmarkablePageLink<?>> linkTypeToDecorate, linkTypeToNotDecorate;
    private final IBreadcrumbPageFilter pageFilter;

    BookmarkableBreadcrumbPageInitializationListener(IBreadcrumbPageFilter pageFilter,
        Class<? extends BookmarkablePageLink<?>> linkTypeToDecorate, Class<? extends BookmarkablePageLink<?>> linkTypeToNotDecorate) {

        this.pageFilter = pageFilter;
        this.linkTypeToDecorate = linkTypeToDecorate;
        this.linkTypeToNotDecorate = linkTypeToNotDecorate;
    }

    @Override
    public void onInitialize(Component component) {
        if (!linkTypeToNotDecorate.isAssignableFrom(component.getClass()) &&
            linkTypeToDecorate.isAssignableFrom(component.getClass()) &&
            pageFilter.shouldCreateBreadcrumbFor(component.getPage())) {

            BookmarkablePageLink<?> link = linkTypeToDecorate.cast(component);
            if (link.getPage().getPageParameters() == link.getPageParameters()) {
                cloneParameters(link, link.getPageParameters());
                logger.trace("Cloned page parameters for a bookmarkable link {} on page {}.", link, link.getPage());
            }
            BreadcrumbPageParameters.setTrailTo(link.getPageParameters(), link.getPage());
        }
    }

    private static void cloneParameters(final BookmarkablePageLink<?> link, final PageParameters pageParameters) {
        /*
         * Cloning is needed because we cannot modify the page parameters of a page containing a
         * bookmarkable link as they may already point to an existing breadcrumb trail. By modifying page's parameters
         * we would make the instance of the page point to itself in the breadcrumb trail.
         */
        try {
            AccessController.doPrivileged(new PrivilegedExceptionAction<Void>() {
                @Override
                public Void run() throws Exception {
                    Field f = BookmarkablePageLink.class.getDeclaredField("parameters");
                    f.setAccessible(true);
                    f.set(link, new PageParameters(pageParameters));
                    return null;
                }
            });
        } catch (PrivilegedActionException e) {
            throw new RuntimeException(e);
        }
    }

}
