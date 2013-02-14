package lt.inventi.wicket.component.bootstrap;

import java.util.HashMap;
import java.util.Map;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.head.OnDomReadyHeaderItem;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.model.AbstractReadOnlyModel;

import de.agilecoders.wicket.util.Json;

import lt.inventi.wicket.resource.ResourceSettings;

public class ModalBehavior extends Behavior {

    private final WebMarkupContainer modalContainer;
    private final ModalBehaviorConfig modalConfig;

    public ModalBehavior(WebMarkupContainer modalContainer) {
        this(modalContainer, new ModalBehaviorConfig());
    }

    public ModalBehavior(WebMarkupContainer modalContainer, ModalBehaviorConfig modalConfig) {
        this.modalContainer = modalContainer;
        this.modalConfig = modalConfig;
    }

    @Override
    public void bind(Component component) {
        super.bind(component);
        component.setOutputMarkupId(true);
        modalContainer.setOutputMarkupId(true);

        modalContainer.add(new AttributeAppender("class", " modal hide"));

        component.add(new AttributeModifier("data-toggle", "modal"));
        component.add(new AttributeModifier("data-target", new AbstractReadOnlyModel<String>() {
            // StaticIdInitializationListener kicks in after #onInitialize and modifies the markup id, so we cannot just use the
            // modalContainer#getMarkupId during #bind.
            @Override
            public String getObject() {
                return "#" + modalContainer.getMarkupId();
            }
        }));

        if (modalConfig.hasVisibilityModel()) {
            modalContainer.add(new ModalOpenClosedStateBehavior(modalConfig.visibilityModel()));
        }
    }

    @Override
    public void renderHead(Component component, IHeaderResponse response) {
        super.renderHead(component, response);

        response.render(JavaScriptHeaderItem.forReference(ResourceSettings.get().js().bootstrapJs.bsModal));
        response.render(OnDomReadyHeaderItem.forScript("$('#" + modalContainer.getMarkupId() + "').modal(" + getOptionsJson() + ")"));
    }

    private String getOptionsJson() {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("show", modalConfig.isShown());
        params.put("backdrop", modalConfig.backdrop());
        return Json.stringify(params);
    }

}
