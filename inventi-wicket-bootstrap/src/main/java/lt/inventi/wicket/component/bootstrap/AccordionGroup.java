package lt.inventi.wicket.component.bootstrap;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
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

    private final Label label;

    public AccordionGroup(String id, IModel<String> labelModel) {
        super(id);
        setRenderBodyOnly(true);
        label = new Label("label", labelModel);
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();

        WebMarkupContainer body = new WebMarkupContainer("body");
        body.setOutputMarkupId(true);

        label.add(new AttributeModifier("data-parent", idModel(getParent())));
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
