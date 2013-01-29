package lt.inventi.wicket.component.bootstrap.form;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import java.util.Arrays;

import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.Markup;
import org.apache.wicket.markup.MarkupStream;
import org.apache.wicket.markup.parser.XmlTag.TagType;
import org.apache.wicket.util.tester.WicketTester;
import org.junit.Test;

import lt.inventi.wicket.component.bootstrap.form.BootstrapRadioChoice.Type;

public class BootstrapRadioChoiceTest {

    private final WicketTester tester = new WicketTester();

    @Test
    public void generatesBootstrapRadioMarkup() {
        BootstrapRadioChoice<String> choice = new BootstrapRadioChoice<String>("test", Arrays.asList("1"));

        render(choice, "<div wicket:id=\"test\"><input type=\"radio\"></input></div>");

        /* Original:
        assertThat(tester.getResponse().getDocument(),
            equalTo("<input name=\"test\" type=\"radio\" value=\"0\" id=\"test1-0\"/><label for=\"test1-0\">1</label>\n"));*/
        assertThat(tester.getResponse().getDocument(),
            equalTo("<label class=\"radio\" for=\"test1-0\">1<input name=\"test\" type=\"radio\" value=\"0\" id=\"test1-0\"/></label>\n"));
    }
    
    @Test
    public void generatesInlineBootstrapRadioMarkup() {
        BootstrapRadioChoice<String> choice = new BootstrapRadioChoice<String>("test", Arrays.asList("1")).setType(Type.INLINE);

        render(choice, "<div wicket:id=\"test\"><input type=\"radio\"></input></div>");

        assertThat(tester.getResponse().getDocument(),
            equalTo("<label class=\"radio inline\" for=\"test1-0\">1<input name=\"test\" type=\"radio\" value=\"0\" id=\"test1-0\"/></label>\n"));
    }

    @Test
    public void generatesBootstrapRadioMarkupForMultipleOptions() {
        BootstrapRadioChoice<String> choice = new BootstrapRadioChoice<String>("test", Arrays.asList("FEMALE", "MALE", "INDETERMINATE"));

        render(choice, "<div wicket:id=\"test\"><input type=\"radio\"></input></div>");

        assertThat(tester.getResponse().getDocument(),
            equalTo("<label class=\"radio\" for=\"test1-0\">FEMALE<input name=\"test\" type=\"radio\" value=\"0\" id=\"test1-0\"/></label>\n" +
                    "<label class=\"radio\" for=\"test1-1\">MALE<input name=\"test\" type=\"radio\" value=\"1\" id=\"test1-1\"/></label>\n" +
                    "<label class=\"radio\" for=\"test1-2\">INDETERMINATE<input name=\"test\" type=\"radio\" value=\"2\" id=\"test1-2\"/></label>\n"));
    }

    private static void render(BootstrapRadioChoice<String> choice, String initialMarkup) {
        MarkupStream stream = new MarkupStream(Markup.of(initialMarkup));
        stream.setCurrentIndex(1);
        choice.onComponentTagBody(stream, new ComponentTag("div", TagType.OPEN));
    }
}
