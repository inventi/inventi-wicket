package lt.inventi.wicket.component;

import org.apache.wicket.Component;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.head.OnDomReadyHeaderItem;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.request.resource.JavaScriptResourceReference;

import lt.inventi.wicket.resource.ResourceSettings;

/**
 * Makes field hidden behind the link until user clicks it. Should be used for
 * input fields, which are optional and will be left blank most of the time.
 */
public class BlankField extends Behavior {

    private Component focusComponent;

    @Override
    public void bind(Component component) {
        component.setOutputMarkupId(true);
        if (focusComponent == null) {
            focusComponent = component;
        }
    }

    /**
     * Specifies component which must be set in focus whenever field is
     * revealed. This is useful when hiding entire panels or forms.
     */
    public BlankField focusComponent(Component component) {
        this.focusComponent = component;
        return this;
    }

    @Override
    public void renderHead(Component component, IHeaderResponse response) {
        response.render(JavaScriptHeaderItem.forReference(ResourceSettings.get().js().jqueryUi.uiCoreWidget));
        response.render(JavaScriptHeaderItem.forReference(new JavaScriptResourceReference(BlankField.class, "BlankField.js")));

        StringResourceModel model = new StringResourceModel("blankLabel", component, null);
        String id = component.getMarkupId();

        response.render(OnDomReadyHeaderItem.forScript("$(\"#" + id + "\").blankInput({id: '" + id + "', label:\""
                + model.getString() + "\", focus: \"" + focusComponent.getMarkupId() + "\"});"));
    }
}
