package lt.inventi.wicket.component.enums;

import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.EnumChoiceRenderer;
import org.apache.wicket.model.IModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

public class EnumDropDownChoice extends DropDownChoice<Enum<?>> {

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public EnumDropDownChoice(String id, Class<? extends Enum<?>> enumClass) {
        this(id, EnumSet.allOf((Class) enumClass));
    }

    public EnumDropDownChoice(String id, Enum<?>[] choices) {
        this(id, Arrays.asList(choices));
    }

    public EnumDropDownChoice(String id, Set<? extends Enum<?>> choices) {
        this(id, new ArrayList<Enum<?>>(choices));
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public EnumDropDownChoice(String id, List<? extends Enum<?>> choices) {
        super(id, choices);
        setChoiceRenderer(new EnumChoiceRenderer(this));
    }

    public EnumDropDownChoice(String id, IModel<? extends List<? extends Enum<?>>> choices) {
        super(id, choices);
        setChoiceRenderer(new EnumChoiceRenderer(this));
    }

}
