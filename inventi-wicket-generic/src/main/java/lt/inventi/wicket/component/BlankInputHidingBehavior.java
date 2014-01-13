package lt.inventi.wicket.component;

import org.apache.wicket.Component;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.core.util.string.JavaScriptUtils;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.head.OnDomReadyHeaderItem;
import org.apache.wicket.model.IComponentAssignedModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.resource.JavaScriptResourceReference;

import lt.inventi.wicket.resource.ResourceSettings;

/**
 * If input field is blank, hides it behind a link until the user clicks it.
 * Should be used for optional fields which will be left blank most of the time.
 */
public class BlankInputHidingBehavior extends Behavior {

    private Component focusComponent;
    private IModel<String> linkLabel;

    public BlankInputHidingBehavior(IModel<String> linkLabel) {
        this.linkLabel = linkLabel;
    }

    @Override
    public void bind(Component component) {
        component.setOutputMarkupId(true);
        if (focusComponent == null) {
            focusComponent = component;
        }
        if (linkLabel instanceof IComponentAssignedModel) {
            linkLabel = ((IComponentAssignedModel<String>) linkLabel).wrapOnAssignment(component);
        }
    }

    /**
     * Specifies component which must be set in focus whenever field is
     * revealed. This is useful when hiding entire panels or forms.
     */
    public BlankInputHidingBehavior focusComponent(Component component) {
        this.focusComponent = component;
        return this;
    }

    @Override
    public void renderHead(Component component, IHeaderResponse response) {
        response.render(JavaScriptHeaderItem.forReference(ResourceSettings.get().js().jqueryUi.uiCoreWidget));
        response.render(JavaScriptHeaderItem.forReference(new JavaScriptResourceReference(BlankInputHidingBehavior.class, "BlankInputHidingBehavior.js")));

        String id = component.getMarkupId();
        String label = JavaScriptUtils.escapeQuotes(linkLabel.getObject()).toString();

        response.render(OnDomReadyHeaderItem.forScript(
            String.format("$('#%s').blankInput({id: '%s', label: '%s', focus: '%s'});",
                id, id, label, focusComponent.getMarkupId())));
    }

}
