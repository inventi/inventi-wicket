package lt.inventi.wicket.component.enums;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.EnumChoiceRenderer;

public class EnumDropDownChoice extends DropDownChoice<Enum<?>> {

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public EnumDropDownChoice(String id, Class<? extends Enum<?>> enumClass) {
        this(id, EnumSet.allOf((Class) enumClass));
    }

    public EnumDropDownChoice(String id, Enum<?>[] choices) {
        this(id, Arrays.asList(choices));
    }

    public EnumDropDownChoice(String id, Set<Enum<?>> choices) {
        this(id, new ArrayList<Enum<?>>(choices));
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public EnumDropDownChoice(String id, List<Enum<?>> choices) {
        super(id, choices);
        setChoiceRenderer(new EnumChoiceRenderer(this));
    }

}
