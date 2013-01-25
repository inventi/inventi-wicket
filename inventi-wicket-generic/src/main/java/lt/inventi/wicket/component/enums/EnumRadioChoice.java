package lt.inventi.wicket.component.enums;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

import org.apache.wicket.markup.html.form.EnumChoiceRenderer;
import org.apache.wicket.markup.html.form.RadioChoice;

/**
 * Radio component that displays localized values of certain enumeration.
 */
public class EnumRadioChoice extends RadioChoice<Enum<?>> {

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public EnumRadioChoice(String id, Class<? extends Enum<?>> enumClass) {
        this(id, EnumSet.allOf((Class<Enum>) enumClass));
    }

    public EnumRadioChoice(String id, Enum<?>[] choices) {
        this(id, Arrays.asList(choices));
    }

    public EnumRadioChoice(String id, Set<? extends Enum<?>> choices) {
        this(id, new ArrayList<Enum<?>>(choices));
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public EnumRadioChoice(String id, List<? extends Enum<?>> choices) {
        super(id, choices);
        setChoiceRenderer(new EnumChoiceRenderer(this));
    }
}
