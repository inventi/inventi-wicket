package lt.inventi.wicket.component.bootstrap;

import java.util.HashMap;
import java.util.Map;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.head.OnDomReadyHeaderItem;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.request.cycle.RequestCycle;

import de.agilecoders.wicket.core.util.Json;

import lt.inventi.wicket.resource.ResourceSettings;

public class ModalBehavior extends Behavior {

    private final WebMarkupContainer modalContainer;
    private final ModalBehaviorConfig modalConfig;

    private AjaxEventBehavior hideBehavior, showBehavior;

    public ModalBehavior(WebMarkupContainer modalContainer) {
        this(modalContainer, new ModalBehaviorConfig());
    }

    public ModalBehavior(WebMarkupContainer modalContainer, ModalBehaviorConfig modalConfig) {
        this.modalContainer = modalContainer;
        this.modalConfig = modalConfig;
    }

    @Override
    public void bind(Component component) {
        component.setOutputMarkupId(true);
        modalContainer.setOutputMarkupId(true);

        modalContainer.add(new AttributeAppender("class", " modal hide"));
        modalContainer.add(new AttributeAppender("tabindex", "-1"));
        
        component.add(new AttributeModifier("data-toggle", "modal"));
        // markup Id might get modified after #bind (e.g. #onInitialize, #onBeforeRender)
        component.add(new AttributeModifier("data-target", new AbstractReadOnlyModel<String>() {
            @Override
            public String getObject() {
                return "#" + modalContainer.getMarkupId();
            }
        }));

        if (modalConfig.hasVisibilityModel()) {
            hideBehavior = new AjaxEventBehavior("hidden") {
                @Override
                protected void onEvent(AjaxRequestTarget target) {
                    modalConfig.onClose();
                }
            };
            showBehavior = new AjaxEventBehavior("shown") {
                @Override
                protected void onEvent(AjaxRequestTarget target) {
                    modalConfig.onShow();
                }
            };

            modalContainer.add(hideBehavior, showBehavior);
        }
    }

    @Override
    public void onRemove(Component component) {
        AjaxRequestTarget target = RequestCycle.get().find(AjaxRequestTarget.class);
        if (target != null) {
            // Hide the modal if behavior is removed during an Ajax request
            StringBuilder js = new StringBuilder("var m = $('#%s').data('modal');");
            js.append("if (m && m.isShown) { m.hide(); }");
            target.prependJavaScript(String.format(js.toString(), modalContainer.getMarkupId()));
        }
        if (modalConfig.hasVisibilityModel()) {
            modalContainer.remove(hideBehavior, showBehavior);
        }
    }

    @Override
    public void renderHead(Component component, IHeaderResponse response) {
        super.renderHead(component, response);

        response.render(JavaScriptHeaderItem.forReference(ResourceSettings.get().js().bootstrapJs.bsModal));

        String mId = modalContainer.getMarkupId();
        response.render(OnDomReadyHeaderItem.forScript("$('#" + mId + "').modal(" + getOptionsJson() + ")"));

        if (modalConfig.hasVisibilityModel()) {
            response.render(OnDomReadyHeaderItem.forScript(callbackScript(mId, "hidden", hideBehavior)));
            response.render(OnDomReadyHeaderItem.forScript(callbackScript(mId, "shown", showBehavior)));
        }
    }

    private static String callbackScript(String mId, String event, AjaxEventBehavior behavior) {
        return String.format("$('#%s').on('%s',function(){Wicket.Ajax.ajax({'u':'%s','c':'%s'});})",
            mId, event, behavior.getCallbackUrl(), mId);
    }

    private String getOptionsJson() {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("show", modalConfig.isShown());
        params.put("backdrop", modalConfig.backdrop());
        return Json.stringify(params);
    }

}
