package lt.inventi.wicket.component.datepicker;

import java.util.Date;

import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.head.OnDomReadyHeaderItem;
import org.apache.wicket.model.IModel;

import lt.inventi.wicket.component.datepicker.references.BootstrapDatepickerCssResourceReference;
import lt.inventi.wicket.component.datepicker.references.BootstrapDatepickerJsResourceReference;
import lt.inventi.wicket.component.datepicker.references.BootstrapDatepickerLangJsResourceReference;

/**
 * A TextField that is mapped to a <code>java.util.Date</code> object.
 * <p/>
 * If no date pattern is explicitly specified, the default
 * <code>DateFormat.SHORT</code> pattern for the current locale will be used.
 *
 * @author miha, vplatonov
 */
public class DateTextField extends org.apache.wicket.extensions.markup.html.form.DateTextField {
    private final DateTextFieldConfig config;

    /**
     * Construct.
     *
     * @param markupId
     *            The id of the text field
     */
    public DateTextField(String markupId) {
        this(markupId, new DateTextFieldConfig());
    }

    /**
     * Construct.
     *
     * @param markupId
     *            The id of the text field
     * @param datePattern
     *            The format of the date
     */
    public DateTextField(String markupId, String datePattern) {
        this(markupId, new DateTextFieldConfig().withFormat(datePattern));
    }

    /**
     * Construct.
     *
     * @param markupId
     *            The id of the text field
     * @param model
     *            The date model
     */
    public DateTextField(String markupId, IModel<Date> model) {
        this(markupId, model, new DateTextFieldConfig());
    }

    /**
     * Construct.
     *
     * @param markupId
     *            The id of the text field
     * @param model
     *            The date model
     * @param dateFormat
     *            The format of the date
     */
    public DateTextField(String markupId, IModel<Date> model, String dateFormat) {
        this(markupId, model, new DateTextFieldConfig().withFormat(dateFormat));
    }

    /**
     * Construct.
     *
     * @param markupId
     *            The id of the text field
     * @param model
     *            The date model
     * @param config
     *            The configuration of this field
     */
    public DateTextField(String markupId, IModel<Date> model, DateTextFieldConfig config) {
        super(markupId, model, config.getFormat());

        this.config = config;
    }

    /**
     * Construct.
     *
     * @param markupId
     *            The id of the text field
     * @param config
     *            The configuration of this field
     */
    public DateTextField(String markupId, DateTextFieldConfig config) {
        super(markupId, config.getFormat());

        this.config = config;
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();
        setOutputMarkupId(true);
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);

        response.render(CssHeaderItem.forReference(BootstrapDatepickerCssResourceReference.get()));

        if (!config.isDefaultLanguageSet()) {
            response.render(JavaScriptHeaderItem.forReference(new BootstrapDatepickerLangJsResourceReference(config.getLanguage())));
        } else {
            response.render(JavaScriptHeaderItem.forReference(BootstrapDatepickerJsResourceReference.get()));
        }

        response.render(OnDomReadyHeaderItem.forScript(createScript()));
    }

    /**
     * @return the initializer script
     */
    private CharSequence createScript() {
        return "$('#" + this.getMarkupId() + "').datepicker(" + config.toJson() + ")";
    }

}
