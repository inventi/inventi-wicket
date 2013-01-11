package lt.inventi.wicket.component.bootstrap;

import java.io.Serializable;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.head.OnDomReadyHeaderItem;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;

import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import lt.inventi.wicket.resource.ResourceSettings;

public class Modal extends Behavior {

    private final WebMarkupContainer modalContainer;
    private final Options opts;

    public Modal(WebMarkupContainer modalContainer) {
        this(modalContainer, new Options());
    }

    public Modal(WebMarkupContainer modalContainer, Options options) {
        this.modalContainer = modalContainer;
        this.opts = options;
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

        if (opts.isShown != null) {
            modalContainer.add(new ModalOpenClosedStateBehavior(opts.isShown));
        }
    }

    @Override
    public void renderHead(Component component, IHeaderResponse response) {
        super.renderHead(component, response);

        response.render(JavaScriptHeaderItem.forReference(ResourceSettings.get().js().bootstrapJs.bsModal));
        response.render(OnDomReadyHeaderItem.forScript("$('#" + modalContainer.getMarkupId() + "').modal(" + getOptionsJson() + ")"));
    }

    private String getOptionsJson() {
        JsonObject result = new JsonObject();
        result.add("show", new JsonPrimitive(opts.isShown()));
        result.add("backdrop", opts.backdrop.toJson());
        return result.toString();
    }

    public static class Options implements Serializable {
        public enum Backdrop {
            TRUE, FALSE, STATIC;

            public JsonPrimitive toJson() {
                String name = this.name().toLowerCase();
                return (this == TRUE || this == FALSE) ? new JsonPrimitive(Boolean.valueOf(name)) : new JsonPrimitive(name);
            }
        }

        private final Backdrop backdrop;
        private final IModel<Boolean> isShown;

        Options() {
            this(Backdrop.TRUE);
        }

        public Options(Backdrop backdrop) {
            this.backdrop = backdrop;
            this.isShown = null;
        }

        public Options(IModel<Boolean> isShown) {
            this.backdrop = Backdrop.TRUE;
            this.isShown = isShown;
        }

        boolean isShown() {
            return isShown == null ? false : isShown.getObject();
        }
    }
}
