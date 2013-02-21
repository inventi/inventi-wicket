package lt.inventi.wicket.component.breadcrumb.h;

import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.IModel;

/**
 * This is kind of a hack and doesn't work with WicketTester. Tester resolves
 * the URL for the link with a {@code PageInstanceMapper} as this type of link
 * is an instance of a {@link Link} which is stateful by default;
 *
 * @author zhilvis, vplatonov
 *
 */
public class StatelessBreadcrumbLink extends Link<IBreadcrumbUrlProvider> {

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public StatelessBreadcrumbLink(String id, IModel<? extends IBreadcrumbUrlProvider> model) {
        super(id, (IModel) model);
    }

    @Override
    public void onClick() {
        // this wont be invoked at all
    }

    @Override
    protected CharSequence getURL() {
        return getModelObject().getURL(getRequestCycle());
    }

    @Override
    protected boolean getStatelessHint() {
        return true;
    }
}
