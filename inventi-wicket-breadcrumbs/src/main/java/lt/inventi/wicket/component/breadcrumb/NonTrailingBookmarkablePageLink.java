package lt.inventi.wicket.component.breadcrumb;

import org.apache.wicket.Page;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.request.mapper.parameter.PageParameters;

/**
 * {@link BookmarkablePageLink} which doesn't generate a breadcrumb
 * automatically if {@link BreadcrumbsSettings#withDecoratedBookmarkableLinks()}
 * is turned on.
 * <p>
 * If you do not use the
 * {@link BreadcrumbsSettings#withDecoratedBookmarkableLinks()} option, you do
 * not need to use this link type in your pages.
 *
 * @author vplatonov
 *
 * @param <T>
 */
public class NonTrailingBookmarkablePageLink<T> extends BookmarkablePageLink<T> {

    public <C extends Page> NonTrailingBookmarkablePageLink(String id, Class<C> pageClass, PageParameters parameters) {
        super(id, pageClass, parameters);
    }

    public <C extends Page> NonTrailingBookmarkablePageLink(String id, Class<C> pageClass) {
        super(id, pageClass);
    }

}
