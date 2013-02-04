package lt.inventi.wicket.component.enums;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

import org.apache.wicket.markup.html.form.CheckBoxMultipleChoice;
import org.apache.wicket.markup.html.form.EnumChoiceRenderer;

public class EnumCheckBoxMultipleChoice extends CheckBoxMultipleChoice<Enum<?>> {

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public EnumCheckBoxMultipleChoice(String id, Class<? extends Enum<?>> enumClass) {
        this(id, EnumSet.allOf((Class) enumClass));
    }

    public EnumCheckBoxMultipleChoice(String id, Enum<?>[] choices) {
        this(id, Arrays.asList(choices));
    }

    public EnumCheckBoxMultipleChoice(String id, Set<Enum<?>> choices) {
        this(id, new ArrayList<Enum<?>>(choices));
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public EnumCheckBoxMultipleChoice(String id, List<Enum<?>> choices) {
        super(id, choices);
        setChoiceRenderer(new EnumChoiceRenderer(this));
    }
}
