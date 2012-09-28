package lt.inventi.wicket.component.autocomplete;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.attributes.AjaxRequestAttributes;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.feedback.FeedbackMessages;

/**
 * Updates autocomplete model via ajax. This behavior submits not only input field value, but also
 * hidden id, which makes it different from standart wicket behaviours.
 */
public abstract class AutocompleteAjaxUpdateBehaviour extends AjaxFormComponentUpdatingBehavior {

    public AutocompleteAjaxUpdateBehaviour(String event) {
        super(event);
    }

    @Override
    protected void updateAjaxAttributes(AjaxRequestAttributes attributes) {
        super.updateAjaxAttributes(attributes);
        String hiddenId = getComponent().get("id").getMarkupId();
        attributes.getDynamicExtraParameters().add("" +
                "return Wicket.Form.serializeElement('"+hiddenId+"')");
    }

    /**
     * Listener invoked on the ajax request. This listener is invoked after the component's model
     * has been updated.
     *
     * @param target
     */
    @Override
    protected abstract void onUpdate(AjaxRequestTarget target);

    /**
     * Called to handle any error resulting from updating form component. Errors thrown from
     * {@link #onUpdate(AjaxRequestTarget)} will not be caught here.
     *
     * The RuntimeException will be null if it was just a validation or conversion error of the
     * FormComponent
     *
     * @param target
     * @param e
     */
    @Override
    protected void onError(AjaxRequestTarget target, RuntimeException e) {
        if (e != null) {
            throw e;
        }

        FeedbackMessages messages = getComponent().getFeedbackMessages();
        if (!messages.isEmpty()) {
            //TODO FIX
            // it is possible that behaviour will submit incorrect value
            // so we will mark message as rendered to avoid warning in the log.
            //messages.;
        }
    }

}