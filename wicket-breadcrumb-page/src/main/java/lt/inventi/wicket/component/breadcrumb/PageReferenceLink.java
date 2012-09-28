package lt.inventi.wicket.component.breadcrumb;

import org.apache.wicket.core.request.mapper.StalePageException;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.IModel;

public class PageReferenceLink extends Link<Breadcrumb>{

    public PageReferenceLink(String id, IModel<Breadcrumb> model) {
        super(id, model);
    }

    @Override
    public void onClick() {
        // this wont be invoked at all
    }

    @Override
    protected CharSequence getURL() {
        CharSequence url = null;
        try {
            url = urlFor(getModelObject().getTarget());
        } catch (StalePageException e) {
            url = urlFor(getModelObject().getStatelessTarget());
        }
        return url;
    }

    @Override
    protected boolean getStatelessHint(){
        return true;
    }
}
