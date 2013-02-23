package lt.inventi.wicket.component.breadcrumb.h;

import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.IModel;

/**
 * This is kind of a hack and doesn't work with WicketTester. Tester resolves
 * the URL for the link with a {@code PageInstanceMapper} as this type of link
 * is an instance of a {@link Link}. Thus, the link becomes stateful.
 * <p>
 * However, this way links become copy-paste'able.
 *
 * @author zhilvis, vplatonov
 *
 */
class BreadcrumbLink extends Link<Breadcrumb> {

    BreadcrumbLink(String id, IModel<Breadcrumb> model) {
        super(id, model);
    }

    @Override
    public void onClick() {
        if (BreadcrumbsSettings.useStatefulBreadcrumbLinks()) {
            getRequestCycle().scheduleRequestHandlerAfterCurrent(getModelObject().getTarget());
        } else {
            throw new IllegalStateException("#onClick invoked for a stateless breadcrumb link " + this + "!");
        }
    }

    @Override
    protected CharSequence getURL() {
        if (BreadcrumbsSettings.useStatefulBreadcrumbLinks()) {
            return super.getURL();
        }
        return getModelObject().getURL(getRequestCycle());
    }

    @Override
    protected boolean getStatelessHint() {
        return !BreadcrumbsSettings.useStatefulBreadcrumbLinks();
    }
}
