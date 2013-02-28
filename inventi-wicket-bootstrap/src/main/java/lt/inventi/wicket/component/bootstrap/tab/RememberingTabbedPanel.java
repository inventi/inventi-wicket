package lt.inventi.wicket.component.bootstrap.tab;

import java.util.Arrays;
import java.util.List;

import org.apache.wicket.extensions.markup.html.tabs.ITab;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.IModel;
import org.apache.wicket.util.string.StringValue;

import de.agilecoders.wicket.markup.html.bootstrap.tabs.BootstrapTabbedPanel;

public class RememberingTabbedPanel<T extends ITab> extends BootstrapTabbedPanel<T> {

    public RememberingTabbedPanel(String id, List<T> tabs, IModel<Integer> model) {
        super(id, tabs, model);
    }

    public RememberingTabbedPanel(String id, List<T> tabs) {
        super(id, tabs);
    }

    public RememberingTabbedPanel(String id, T... tabs) {
        super(id, Arrays.asList(tabs));
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();

        StringValue selectedTabId = getPage().getPageParameters().get(getId());
        int tab = selectedTabId.toInt(0);
        if (tab >= getTabs().size()) {
            setSelectedTab(0);
        } else {
            setSelectedTab(tab);
        }
    }

    @Override
    protected WebMarkupContainer newLink(String linkId, final int index) {
        return new Link<Void>(linkId) {
            @Override
            public void onClick() {
                setSelectedTab(index);
                getPage().getPageParameters().set(RememberingTabbedPanel.this.getId(), String.valueOf(index));
            }
        };
    }
}
