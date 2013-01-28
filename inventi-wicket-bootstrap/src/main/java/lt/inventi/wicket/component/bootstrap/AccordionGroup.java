package lt.inventi.wicket.component.bootstrap;

import java.io.Serializable;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.border.Border;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;

import lt.inventi.wicket.resource.ResourceSettings;

/**
 * Groups used inside of bootstrap accordion (Collapse).
 *
 * @author vplatonov
 * @see Accordion
 *
 */
public class AccordionGroup extends Border {

    public static class Options implements Serializable {
        private boolean collapsed = true;
        private boolean hasParent = true;

        public static Options collapsed() {
            return new Options().setCollapsed(true);
        }

        public static Options unCollapsed() {
            return new Options().setCollapsed(false);
        }

        public Options withoutParent() {
            return setHasParent(false);
        }

        public Options setHasParent(boolean hasParent) {
            this.hasParent = hasParent;
            return this;
        }

        public Options setCollapsed(boolean collapsed) {
            this.collapsed = collapsed;
            return this;
        }
    }

    private final Label label;
    private final Options options;

    public AccordionGroup(String id, IModel<String> labelModel) {
        this(id, labelModel, new Options());
    }

    public AccordionGroup(String id, IModel<String> labelModel, Options options) {
        super(id);
        setRenderBodyOnly(true);
        label = new Label("label", labelModel);
        this.options = options;
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();

        WebMarkupContainer body = new WebMarkupContainer("body");
        body.setOutputMarkupId(true);

        if (options.collapsed) {
            body.add(new AttributeAppender("class", " collapse"));
        } else {
            body.add(new AttributeAppender("class", " in"));
        }
        if (options.hasParent) {
            label.add(new AttributeModifier("data-parent", idModel(getParent())));
        }
        label.add(new AttributeModifier("href", idModel(body)));

        addToBorder(label, body);
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
        response.render(JavaScriptHeaderItem.forReference(ResourceSettings.get().js().bootstrapJs.bsCollapse));
    }

    private static IModel<String> idModel(final Component cmp) {
        return new AbstractReadOnlyModel<String>() {
            @Override
            public String getObject() {
                return "#" + cmp.getMarkupId();
            }
        };
    }

}
