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

import lt.inventi.wicket.component.bootstrap.form.BootstrapCheckBoxMultipleChoice.CheckboxType;

public class BootstrapCheckBoxMultipleChoiceTest {

    private final WicketTester tester = new WicketTester();

    @Test
    public void generatesBootstrapCheckBoxMarkup() {
        BootstrapCheckBoxMultipleChoice<String> choice = new BootstrapCheckBoxMultipleChoice<String>("test", Arrays.asList("1"));
        choice.setInputPosition(ChoiceUtils.InputPosition.AFTER_LABEL);

        render(choice, "<div wicket:id=\"test\"><input type=\"checkbox\"></input></div>");

        /* Original:
        assertThat(tester.getResponse().getDocument(),
            equalTo("<input name=\"test\" type=\"checkbox\" value=\"0\" id=\"test1-test_0\"/><label for=\"test1-test_0\">1</label>\n"));*/
        assertThat(tester.getResponse().getDocument(),
            equalTo("<label class=\"checkbox\" for=\"test1-test_0\">1<input name=\"test\" type=\"checkbox\" value=\"0\" id=\"test1-test_0\"/></label>\n"));
    }

    @Test
    public void generatesInlineBootstrapCheckBoxMarkup() {
        BootstrapCheckBoxMultipleChoice<String> choice = new BootstrapCheckBoxMultipleChoice<String>("test", Arrays.asList("1")).setType(CheckboxType.INLINE);
        choice.setInputPosition(ChoiceUtils.InputPosition.AFTER_LABEL);

        render(choice, "<div wicket:id=\"test\"><input type=\"checkbox\"></input></div>");

        assertThat(tester.getResponse().getDocument(),
            equalTo("<label class=\"checkbox inline\" for=\"test1-test_0\">1<input name=\"test\" type=\"checkbox\" value=\"0\" id=\"test1-test_0\"/></label>\n"));
    }

    @Test
    public void generatesBootstrapcheckboxMarkupForMultipleOptions() {
        BootstrapCheckBoxMultipleChoice<String> choice = new BootstrapCheckBoxMultipleChoice<String>("test", Arrays.asList("FEMALE", "MALE", "INDETERMINATE"));
        choice.setInputPosition(ChoiceUtils.InputPosition.AFTER_LABEL);

        render(choice, "<div wicket:id=\"test\"><input type=\"checkbox\"></input></div>");

        assertThat(tester.getResponse().getDocument(),
            equalTo("<label class=\"checkbox\" for=\"test1-test_0\">FEMALE<input name=\"test\" type=\"checkbox\" value=\"0\" id=\"test1-test_0\"/></label>\n" +
                    "<label class=\"checkbox\" for=\"test1-test_1\">MALE<input name=\"test\" type=\"checkbox\" value=\"1\" id=\"test1-test_1\"/></label>\n" +
                    "<label class=\"checkbox\" for=\"test1-test_2\">INDETERMINATE<input name=\"test\" type=\"checkbox\" value=\"2\" id=\"test1-test_2\"/></label>\n"));
    }

    private static void render(BootstrapCheckBoxMultipleChoice<String> choice, String initialMarkup) {
        MarkupStream stream = new MarkupStream(Markup.of(initialMarkup));
        stream.setCurrentIndex(1);
        choice.onComponentTagBody(stream, new ComponentTag("div", TagType.OPEN));
    }
}
