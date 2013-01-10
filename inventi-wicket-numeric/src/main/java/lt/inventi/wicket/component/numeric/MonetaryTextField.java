package lt.inventi.wicket.component.numeric;

import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.head.OnDomReadyHeaderItem;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.IModel;

import lt.inventi.wicket.component.numeric.references.AutoNumericJavaScriptResourceReference;

public class MonetaryTextField<T extends Number> extends TextField<T> {

    public MonetaryTextField(String id, Class<T> type) {
        super(id, type);
    }

    public MonetaryTextField(String id, IModel<T> model, Class<T> type) {
        super(id, model, type);
    }

    public MonetaryTextField(String id, IModel<T> model) {
        super(id, model);
    }

    public MonetaryTextField(String id) {
        super(id);
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();

        setOutputMarkupId(true);
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);

        response.render(JavaScriptHeaderItem.forReference(AutoNumericJavaScriptResourceReference.get()));
        response.render(OnDomReadyHeaderItem.forScript("$('#" + getMarkupId() + "').autoNumeric('init')"));
    }

}
