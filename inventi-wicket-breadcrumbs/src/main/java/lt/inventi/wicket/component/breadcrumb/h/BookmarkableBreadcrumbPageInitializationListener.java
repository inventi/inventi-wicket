package lt.inventi.wicket.component.breadcrumb.h;

import org.apache.wicket.Component;
import org.apache.wicket.application.IComponentInitializationListener;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BookmarkableBreadcrumbPageInitializationListener implements IComponentInitializationListener {

    private static final Logger logger = LoggerFactory.getLogger(BookmarkableBreadcrumbPageInitializationListener.class);

    private final Class<? extends BookmarkablePageLink<?>> linkTypeToDecorate;

    public BookmarkableBreadcrumbPageInitializationListener(Class<? extends BookmarkablePageLink<?>> linkTypeToDecorate) {
        this.linkTypeToDecorate = linkTypeToDecorate;
    }

    @Override
    public void onInitialize(Component component) {
        if (linkTypeToDecorate.isAssignableFrom(component.getClass())) {
            BookmarkablePageLink<?> link = linkTypeToDecorate.cast(component);
            if (link.getPage().getPageParameters() == link.getPageParameters()) {
                logger.warn("Modifying parameters of the {} page!", link.getPage());
            }
            BreadcrumbPageParameters.setTrailTo(link.getPageParameters(), link.getPage());
        }
    }

}
