package lt.inventi.wicket.component.breadcrumb;

import java.util.List;

import org.apache.wicket.Component;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.AbstractLink;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Fragment;
import org.apache.wicket.markup.html.panel.GenericPanel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.PropertyModel;

import lt.inventi.wicket.component.breadcrumb.IBreadcrumbCollapser.DisplayedBreadcrumb;

public class BreadcrumbsPanel extends GenericPanel<List<DisplayedBreadcrumb>> {

    private final ListView<DisplayedBreadcrumb> breadcrumbs;

    public BreadcrumbsPanel(String id) {
        super(id);

        add(breadcrumbs = new ListView<DisplayedBreadcrumb>("crumbs", new LoadableDetachableModel<List<DisplayedBreadcrumb>>() {
            @Override
            public List<DisplayedBreadcrumb> load() {
                IBreadcrumbCollapser collapser = BreadcrumbsSettings.getBreadcrumbsCollapser();
                return collapser.collapse(getBreadcrumbs());
            }
        }) {
            @Override
            protected void populateItem(ListItem<DisplayedBreadcrumb> item) {
                BreadcrumbsPanel.this.populateBreadcrumb(item);
            }
        });
    }

    // PRIVATE API
    List<Breadcrumb> getBreadcrumbs() {
        return BreadcrumbTrailHistory.getTrail(Breadcrumb.constructIdFrom(getPage()));
    }

    protected void populateBreadcrumb(ListItem<DisplayedBreadcrumb> item) {
        Component title = new Label("title", item.getModelObject().title());

        final Fragment crumb;
        if (item.getParent() == breadcrumbs) {
            if (shouldBeDisabled(item)) {
                crumb = new Fragment("crumb", "singleInactive", this);
            } else {
                crumb = new Fragment("crumb", "single", this);
            }
        } else {
            crumb = new Fragment("crumb", "singleCollapsed", this);
        }

        if (item.getModelObject().isCollapsed()) {
            WebMarkupContainer collapsed = new Fragment("crumb", "collapsed", this);
            IModel<List<DisplayedBreadcrumb>> collapsedCrumbs = PropertyModel.of(item.getModel(), "collapsedCrumbs");
            collapsed.add(new ListView<DisplayedBreadcrumb>("crumbs", collapsedCrumbs) {
                @Override
                protected void populateItem(ListItem<DisplayedBreadcrumb> item1) {
                    BreadcrumbsPanel.this.populateBreadcrumb(item1);
                }
            });
            collapsed.add(crumb.add(new WebMarkupContainer("link").add(title)));

            item.add(collapsed);
        } else {
            AbstractLink link = customize(createBreadcrumbLink("link", item));
            if (shouldBeDisabled(item)) {
                markAsDisabled(link);
            }
            crumb.add(link.add(title));

            item.add(crumb);
        }
    }

    protected AbstractLink customize(AbstractLink link) {
        return link.setBeforeDisabledLink("").setAfterDisabledLink("");
    }

    protected void markAsDisabled(WebMarkupContainer link) {
        link.setEnabled(false);
        link.add(new AttributeAppender("class", " inactive"));
    }

    protected boolean shouldBeDisabled(ListItem<DisplayedBreadcrumb> item) {
        ListView<?> parent = item.findParent(ListView.class);
        if (parent == breadcrumbs) {
            return parent.getViewSize() - 1 == item.getIndex();
        }
        return false;
    }

    protected static final AbstractLink createBreadcrumbLink(String linkId, ListItem<DisplayedBreadcrumb> item) {
        return new BreadcrumbLink(linkId, new PropertyModel<Breadcrumb>(item.getModel(), "crumb"));
    }

}
