package lt.inventi.wicket.component.breadcrumb;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.MetaDataKey;
import org.apache.wicket.Page;
import org.apache.wicket.Session;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.IRequestHandler;
import org.apache.wicket.request.mapper.parameter.PageParameters;

public class Breadcrumbs extends ListView<Breadcrumb>{


    private static final MetaDataKey<BreadcrumbContainer> CONTAINER = new MetaDataKey<BreadcrumbContainer>(){};
    protected static final String BRD="brd";

    public Breadcrumbs() {
        super("breadcrumbs");

        setModel(new AbstractReadOnlyModel<List<Breadcrumb>>(){
            @Override
            public List<Breadcrumb> getObject() {
                return getBreadcrumbs();
            }
        });
    }

    private Breadcrumb getActiveBreadcrumb(){
        return new Breadcrumb(getPage());
    }

    @Override
    protected void onBeforeRender(){
        getContainer().storeBreadcrumbTrail(getActiveBreadcrumb(), getBreadcrumbTrail());
        super.onBeforeRender();
    }


    protected static BreadcrumbContainer getContainer() {
        Session session = Session.get();
        BreadcrumbContainer container = session.getMetaData(CONTAINER);
        if(container == null){
            container = new BreadcrumbContainer();
            session.setMetaData(CONTAINER, container);
        }

        if(session.isTemporary()){
            session.bind();
        }
        return container;
    }

    @Override
    protected void populateItem(ListItem<Breadcrumb> item) {

        final IModel<Breadcrumb> model = item.getModel();

        Link<?> link = new PageReferenceLink("breadcrumb", model);
        link.add(new Label("label", model.getObject().getTitleModel()));

        if(item.getModelObject().getId().equals(getActiveBreadcrumb().getId())){
            item.add(AttributeModifier.append("class", "active"));
        }

        item.add(link);
    }

    public List<Breadcrumb> getBreadcrumbs() {
        List<Breadcrumb> list = new ArrayList<Breadcrumb>(getBreadcrumbTrail());
        list.add(getActiveBreadcrumb());
        return list;
    }

    public List<Breadcrumb> getBreadcrumbTrail() {
        return getBreadcrumbTrail(getPage().getPageParameters());
    }

    public static List<Breadcrumb> getBreadcrumbTrail(PageParameters parameters) {
        List<Breadcrumb> breadcrumbTrail = null;
        if(parameters != null){
            String id = parameters.get(BRD).toString();
            if(id != null){
                breadcrumbTrail = getContainer().getBreadcrumbs(id);
            }
        }
        if(breadcrumbTrail == null){
            breadcrumbTrail = new ArrayList<Breadcrumb>();
        }
        return new ArrayList<Breadcrumb>(breadcrumbTrail);
    }

    public void setBreadcrumbTrail(BreadcrumbsPage page) {
        getPage().getPageParameters().set(BRD, new Breadcrumb(page).getId());
    }

    private Breadcrumb getPreviousBreadcrumb(){
        Breadcrumb previous = null;
        List<Breadcrumb> trail = getBreadcrumbTrail();
        if(trail.size() > 0){
            previous = trail.get(trail.size()-1);
        }
        return previous;
    }

    public Link<?> getPreviousBookmarkableBreadcrumbLink(String id) {
        Link<?> link = null;
        Breadcrumb previous = getPreviousBreadcrumb();
        if(previous != null){
            link = createLink(id, previous);
            if(previous.isStateless()){
                List<Breadcrumb> trail = getBreadcrumbTrail();
                if(trail.size() > 1){
                    Breadcrumb trailing = trail.get(trail.size()-2);
                    decorateParameters((BookmarkablePageLink<?>) link, trailing);
                }
            }
        }
        return link;
    }

    public Link<?> getNextBookmarkableBreadcrumbLink(String id, Breadcrumb breadcrumb) {
        Link<?> link = createLink(id, breadcrumb);
        if(breadcrumb.isStateless()){
            decorateParameters((BookmarkablePageLink<?>)link, breadcrumb);
        }
        return link;
    }

    private Link<?> createLink(String id, Breadcrumb breadcrumb) {
        Link<?> link = null;
        if (breadcrumb.isStateless()) {
            link = new BookmarkablePageLink<Void>(id, breadcrumb.getPageClass(), breadcrumb.getParams());
        } else {
            link = new PageReferenceLink(id, new Model<Breadcrumb>(breadcrumb));
        }
        return link;
    }

    private void decorateParameters(BookmarkablePageLink<?> link, Breadcrumb breadcrumb){
        checkParametersInstance(link.getPageParameters());
        if(breadcrumb.getId() != null){
            link.getPageParameters().set(BRD, breadcrumb.getId());
        }else{
            link.getPageParameters().set(BRD, getActiveBreadcrumb().getId());
        }
    }

    public IRequestHandler getPreviousPageHandler() {
        IRequestHandler handler = null;
        Breadcrumb previous = getPreviousBreadcrumb();
        if(previous != null){
            handler = previous.getTarget();
        }
        return handler;
    }

    private void checkParametersInstance(PageParameters params) {
        checkParametersInstance(getPage(), params);
    }

    protected static void checkParametersInstance(Page page, PageParameters params) {
        if (params == page.getPageParameters()) {
            throw new IllegalStateException(
                    "Original page parameters have been passed to decorate. "
                            + "Only copy of page parameters should be passed as otherwse it can cause problems with breadcrumbs");
        }
    }
}
