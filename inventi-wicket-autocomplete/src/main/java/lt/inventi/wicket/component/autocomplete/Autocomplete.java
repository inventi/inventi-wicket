package lt.inventi.wicket.component.autocomplete;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONFunction;
import net.sf.json.JSONObject;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.head.OnDomReadyHeaderItem;
import org.apache.wicket.markup.html.form.FormComponentPanel;
import org.apache.wicket.markup.html.form.HiddenField;
import org.apache.wicket.markup.html.form.SubmitLink;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.parser.XmlTag.TagType;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.request.IRequestCycle;
import org.apache.wicket.request.IRequestHandler;
import org.apache.wicket.request.IRequestParameters;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.request.http.WebResponse;
import org.apache.wicket.request.resource.JavaScriptResourceReference;
import org.apache.wicket.util.convert.ConversionException;

import lt.inventi.wicket.resource.ResourceSettings;

/**
 * Provides autocomplete functionality, based on jquery autocomplete plugin.
 * <p>
 * This class adds some extra functionality, which is not available in the
 * default implementation, such as autocomplete result expansion with mouse
 * click or keypress and creation of new autocompleted items.
 *
 * <p>
 * To update via Ajax use {@link AutocompleteAjaxUpdateBehaviour}.
 *
 * @param <T>
 */
public class Autocomplete<ID extends Serializable, T, S> extends FormComponentPanel<T> implements IQueryListener {

    private static final long serialVersionUID = 1L;

    /**
     * Json parameters
     */
    public static final String VALUE_PARAM = "value";
    public static final String LABEL_PARAM = "label";
    public static final String ID_PARAM = "id";

    private boolean addNewLinkEnabled = false;
    private SubmitLink addNewLink;

    private ValueField valueField;
    private HiddenField<ID> idField;

    private AutocompleteDataProvider<T> dataProvider;
    private AutocompleteDataLabelProvider<T> dataLabelProvider;
    private AutocompleteSearchProvider<S> searchProvider;
    private AddNewItemHandler<T> newItemHandler;

    private boolean labelWasSet;

    public Autocomplete(String id) {
        this(id, null);
    }

    public Autocomplete(String id, IModel<T> model) {
        super(id, model);

        valueField = new ValueField("value");
        add(valueField);
    }

    protected void setDataProvider(AutocompleteDataProvider<T> dataProvider) {
        this.dataProvider = dataProvider;
    }

    protected void setSearchProvider(AutocompleteSearchProvider<S> searchProvider) {
        this.searchProvider = searchProvider;
    }

    protected void setDataLabelProvider(AutocompleteDataLabelProvider<T> dataLabelProvider) {
        this.dataLabelProvider = dataLabelProvider;
    }

    protected void setNewItemHandler(AddNewItemHandler<T> newItemHandler) {
        if (newItemHandler == null) {
            throw new IllegalArgumentException("New item handler cannot be null!");
        }
        this.addNewLinkEnabled = true;
        this.newItemHandler = newItemHandler;
    }

    /**
     * Dynamic control of allowing/disallowing new item addition.
     */
    public Autocomplete<ID, T, S> disallowAddingNewItems() {
        this.addNewLinkEnabled = false;
        return this;
    }

    /**
     * Dynamic control of allowing/disallowing new item addition.
     */
    public Autocomplete<ID, T, S> allowAddingNewItems() {
        this.addNewLinkEnabled = false;
        return this;
    }

    /**
     * This binds id field to the specific atribute. Override this to provide specific model.
     * Default implementation uses "id" as attribute name.
     *
     * @return
     */
    protected IModel<ID> getIdModel() {
        return new PropertyModel<ID>(getDefaultModel(), "id");
    }

    /**
     * Allows to provide extra JS function which will be invoked when item is
     * selected. This is useful to process extra attributes. For example:
     *
     * <pre>
     * function(event, ui){ extraVar = ui.item.extraAttr; }
     * </pre>
     *
     * @return
     */
    protected String getSelectFunction() {
        return null;
    }

    @Override
    protected void onInitialize() {
        if (dataProvider == null) {
            throw new IllegalStateException(this.getClass() + " data provider can't be null");
        }
        if (dataLabelProvider == null) {
            throw new IllegalStateException(this.getClass() + " data label provider can't be null");
        }
        if (searchProvider == null) {
            throw new IllegalStateException(this.getClass() + " search provider can't be null");
        }

        if (!labelWasSet) {
            valueField.setLabel(new StringResourceModel(getId(), this.getParent(), null));
        }

        String styleClass = (String) getMarkupAttributes().get("class");
        valueField.add(AttributeModifier.replace("class", styleClass));
        valueField.setOutputMarkupId(true);
        valueField.setModel(new ValueModel());

        idField = new HiddenField<ID>("id", new IdModel(getIdModel())) {
            @Override
            public void updateModel() {
                // do nothing
            }
        };
        add(idField);

        addNewLink = new SubmitLink("addNew") {
            @Override
            public void onSubmit() {
                NewAutocompleteItemCallback<T> callback = new NewAutocompleteItemCallback<T>() {
                    @Override
                    public void saved(T newEntity) {
                        Autocomplete.this.saved(newEntity);
                    }
                };

                newItemHandler.onNewItem(getValueField().getInput(), callback);
            }

            @Override
            public boolean isVisible() {
                return addNewLinkEnabled;
            }
        };
        add(addNewLink);
        addNewLink.setDefaultFormProcessing(false);
        addNewLink.setOutputMarkupId(true);

        super.onInitialize();
    }

    protected void saved(T newEntity) {
        if (newEntity != null) {
            Autocomplete.this.setModelObject(newEntity);
            Autocomplete.this.clearAllInput();
        }
    }

    @Override
    public Component add(Behavior... behaviors) {
        for (Behavior behavior : behaviors) {
            super.add(behavior);
        }
        return this;
    }

    @Override
    public void onQuery() {
        RequestCycle.get().scheduleRequestHandlerAfterCurrent(new IRequestHandler() {

            @Override
            public void respond(IRequestCycle requestCycle) {
                final WebResponse response = (WebResponse) requestCycle.getResponse();
                IRequestParameters params = requestCycle.getRequest().getRequestParameters();
                String criteria = params.getParameterValue("term").toString();
                String limitString = params.getParameterValue("limit").toString();

                int limit;
                try {
                    limit = Integer.valueOf(limitString);
                } catch (NumberFormatException e) {
                    limit = 10;
                }

                response.setContentType("application/json; charset=utf-8");
                // make sure the request is not cached
                response.setHeader("Expires", "Mon, 26 Jul 1997 05:00:00 GMT");
                response.setHeader("Cache-Control", "no-cache, must-revalidate");
                response.setHeader("Pragma", "no-cache");

                CharSequence text = generateResponse(criteria, limit);
                response.write(text);
            }

            @Override
            public void detach(IRequestCycle requestCycle) {
                // do nothing
            }
        });
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        response.render(JavaScriptHeaderItem.forReference(ResourceSettings.get().js().jqueryUi.uiWidgetAutocomplete));
        response.render(JavaScriptHeaderItem.forReference(new JavaScriptResourceReference(Autocomplete.class, "Autocomplete.js")));
        response.render(CssHeaderItem.forReference(AutocompleteCssResourceReference.get()));

        JSONObject cfg = new JSONObject();
        cfg.put("source", urlFor(IQueryListener.INTERFACE, null));

        String selectFunction = getSelectFunction();
        if (selectFunction != null) {
            cfg.put("select", JSONFunction.parse(selectFunction));
        }

        String script = String.format("$('#%s').objectautocomplete(%s)", valueField.getMarkupId(), cfg.toString());
        response.render(OnDomReadyHeaderItem.forScript(script));
    }

    private CharSequence generateResponse(String criteria, int size) {
        JSONArray response = new JSONArray();
        List<S> result = searchProvider.searchItems(criteria, size);
        if (result != null) {
            for (S item : result) {
                JSONObject obj = new JSONObject();
                Map<String, String> params = searchProvider.getJsonParameters(item);
                obj.putAll(params);
                response.add(obj);
            }
        }
        if (addNewLinkEnabled) {
            JSONObject link = new JSONObject();
            link.put("addNew", true);
            link.put("id", addNewLink.getMarkupId());
            response.add(link);
        }
        return response.toString();
    }

    /**
     * Clears input for the value field. Useful if you updated model, and want to reflect its
     * changes in the page.
     */
    public void clearAllInput() {
        idField.clearInput();
        valueField.clearInput();
    }

    /**
     * Sets label model for the value field. It will be used in error messages to replace ${label)
     * property
     *
     * @param labelModel
     */
    public void setLabelModel(IModel<String> labelModel) {
        valueField.setLabel(labelModel);
        labelWasSet = true;
    }

    @Override
    protected void onComponentTag(ComponentTag tag) {
        tag.setType(TagType.OPEN);
        tag.setName("span");
        tag.append("class", "autocomplete", " ");
        tag.remove("type");
        super.onComponentTag(tag);
    }

    public ValueField getValueField() {
        return valueField;
    }

    public HiddenField<ID> getIdField() {
        return idField;
    }

    public class ValueField extends TextField<String> {
        public ValueField(String id) {
            super(id, new Model<String>());
        }

        @Override
        public String getValidatorKeyPrefix() {
            return Autocomplete.this.getId();
        }

        @Override
        public void updateModel() {
            // do nothing
        }
    }

    @Override
    protected void convertInput() {
        setConvertedInput(doConvertValue(getInputAsArray()));
    }

    private T doConvertValue(String[] value) {
        String key = idField.getInput();
        if (!hasText(key)) {
            return null;
        }

        T oldObject = getModelObject();
        if (oldObject != null) {
            String id = dataProvider.getId(oldObject);
            if (key.equals(id)) {
                return oldObject;
            }
        }
        String valueString = valueField.getInput();
        T objectValue = dataProvider.getObject(key, valueString, oldObject);

        if (objectValue == null && hasText(valueString)) {
            throw new ConversionException("Key is not specified");
        }
        return objectValue;
    }

    protected static boolean hasText(String s) {
        return s != null && !s.trim().isEmpty();
    }

    private class IdModel implements IModel<ID> {
        private IModel<ID> parentModel;

        public IdModel(IModel<ID> parentModel) {
            this.parentModel = parentModel;
        }

        @Override
        public void detach() {
            // do nothing
        }

        @Override
        public ID getObject() {
            return parentModel.getObject();
        }

        @Override
        public void setObject(ID object) {
            throw new UnsupportedOperationException("Id model should not be set by the autocomplete!");
        }
    }

    private class ValueModel implements IModel<String> {
        private T lastObject;
        private String lastValue;

        @Override
        public String getObject() {
            T object = Autocomplete.this.getModelObject();
            if (object == null) {
                return null;
            }
            if (object != lastObject) {
                lastObject = object;
                lastValue = dataLabelProvider.extractLabel(object);
            }
            return lastValue;
        }

        @Override
        public void setObject(String object) {
            throw new UnsupportedOperationException("Value model should not be set by the autocomplete!");
        }

        @Override
        public void detach() {
            this.lastObject = null;
        }

    }
}