package lt.inventi.wicket.component.numeric;

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.assertThat;

import java.math.BigDecimal;

import org.apache.wicket.markup.IMarkupFragment;
import org.apache.wicket.markup.Markup;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.protocol.http.mock.MockHttpServletResponse;
import org.apache.wicket.util.tester.WicketTester;
import org.junit.Test;

public class MonetaryTextFieldTest {

    private WicketTester tester = new WicketTester();

    @Test
    public void formatsNumberAccordingToTheSettings() {
        final IModel<BigDecimal> valueModel = Model.of(BigDecimal.TEN);

        TestPage page = new TestPage() {
            @Override
            protected MonetaryTextField<? extends Number> createMonetaryField(String id) {
                return new MonetaryTextField<BigDecimal>(id, valueModel);
            }
        };
        tester.startPage(page);
        tester.assertRenderedPage(TestPage.class);

        MockHttpServletResponse response = tester.getLastResponse();
        assertThat(response.getDocument(), containsString("$('#" + page.getAmountMarkupId() + "').autoNumeric('init');"));
    }

    private static abstract class TestPage extends WebPage {
        @Override
        protected void onInitialize() {
            super.onInitialize();
            Form<Void> form = new Form<Void>("form");
            form.add(createMonetaryField("amount"));
            add(form);
        }

        public String getAmountMarkupId() {
            return get("form:amount").getMarkupId();
        }

        protected abstract MonetaryTextField<? extends Number> createMonetaryField(String id);

        @Override
        public IMarkupFragment getMarkup() {
            return Markup
                .of("<html><wicket:head></wicket:head><body><form wicket:id=\"form\"><input type=\"text\" wicket:id=\"amount\" /></form></body></html>");
        }
    }
}
