package lt.inventi.wicket.component.bootstrap.form;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

import org.apache.wicket.markup.html.form.EnumChoiceRenderer;

public class BootstrapEnumRadioChoice extends BootstrapRadioChoice<Enum<?>> {

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public BootstrapEnumRadioChoice(String id, Class<? extends Enum<?>> enumClass) {
        this(id, EnumSet.allOf((Class<Enum>) enumClass));
    }

    public BootstrapEnumRadioChoice(String id, Enum<?>[] choices) {
        this(id, Arrays.asList(choices));
    }

    public BootstrapEnumRadioChoice(String id, Set<? extends Enum<?>> choices) {
        this(id, new ArrayList<Enum<?>>(choices));
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public BootstrapEnumRadioChoice(String id, List<? extends Enum<?>> choices) {
        super(id, choices);
        setChoiceRenderer(new EnumChoiceRenderer(this));
    }
}
