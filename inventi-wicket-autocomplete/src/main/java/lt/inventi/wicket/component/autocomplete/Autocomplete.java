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

import lt.inventi.wicket.js.JavaScriptSettings;

/**
 * Provides autocomplete functionality, based on jquery autocomplete plugin. This class adds some
 * extra functionality, which is not available in default implementation, such as expand
 * autocomplete results with mouse click, and create new item.
 *
 * <p/>
 * If you want to updated autocomplete model via ajax, use AutocompleteAjaxBehaviour for that.
 *
 * @param <T>
 */
public class Autocomplete<ID extends Serializable, T> extends FormComponentPanel<T> implements IQueryListener {

    /**
     * Json parameters
     */
    public static final String VALUE_PARAM = "value";
    public static final String LABEL_PARAM = "label";
    public static final String ID_PARAM = "id";

    private static final long serialVersionUID = 2405491595411442878L;

    private boolean addNewLinkEnabled = true;
    private SubmitLink addNewLink;

    private ValueField valueField;
    private HiddenField<ID> idField;

    private AutocompleteDataProvider<T> dataProvider;

    private boolean labelWasSet;

    /**
     * Creates new instance
     *
     * @param id
     *            component id
     * @param model
     *            model, to which this component must be bound to
     * @param dataProvider
     *            autcomplete items provider, or null if you want to pass it later through
     *            setDataProvider method.
     */
    public Autocomplete(String id, IModel<T> model, AutocompleteDataProvider<T> dataProvider) {

        super(id, model);
        this.dataProvider = dataProvider;
        init();
    }

    /**
     * Creates new instance
     *
     * @param id
     *            component id
     * @param dataProvider
     *            autcomplete items provider, or null if you want to pass it later through
     *            setDataProvider method.
     */
    public Autocomplete(String id, AutocompleteDataProvider<T> dataProvider) {
        super(id);
        this.dataProvider = dataProvider;
        init();
    }

    /**
     * Use this method if your data provider is inner class, in which case you cant provide it as
     * constructor parameter.
     *
     * @param dataProvider
     */
    protected void setDataProvider(AutocompleteDataProvider<T> dataProvider) {
        this.dataProvider = dataProvider;
    }

    private void init() {
        valueField = new ValueField("value");
        add(valueField);
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
     * Allows to provide extra JS function which will be invoked when item is selected. This is
     * usefull to process extra attirbutes. For example: function(event, ui){ extraVar =
     * ui.item.extraAttr; }
     *
     * @return
     */
    protected String getSelectFunction() {
        return null;
    }

    /**
     * Invoked when user clicks on NewItem link. It is assumed that overriding method should set
     * next response page.
     */
    protected void onNewItem(String newName, NewEntityCallback<T> callback) {
    }

    @Override
    protected void onInitialize() {
        if (dataProvider == null) {
            throw new IllegalStateException("Date provider can't be null");
        }

        if (!labelWasSet) {
            valueField.setLabel(new StringResourceModel(getId(), this.getParent(), null));
        }

        String styleClass = (String) getMarkupAttributes().get("class");
        valueField.add(AttributeModifier.replace("class", styleClass));
        valueField.setOutputMarkupId(true);
        valueField.setModel(new ValueModel());

        idField = new HiddenField<ID>("id", new IdModel(getIdModel()));
        add(idField);

        addNewLink = new SubmitLink("addNew") {
            @Override
            public void onSubmit() {

                NewEntityCallback<T> entityCallback = new NewEntityCallback<T>(){
                    @Override
                    public void saved(T newEntity) {
                        Autocomplete.this.saved(newEntity);
                    }
                };

                onNewItem(getValueField().getInput(), entityCallback);
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
            }
        });
    }

    @Override
    public void renderHead(IHeaderResponse response) {

        response.render(JavaScriptHeaderItem.forReference(JavaScriptSettings.get().jqueryUi.uiWidgetAutocomplete));
        response.render(JavaScriptHeaderItem.forReference(new JavaScriptResourceReference(Autocomplete.class, "jquery.ui.subclass.js")));
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
        List<T> result = dataProvider.searchItems(criteria, size);
        if (result != null) {
            for (T item : result) {
                JSONObject obj = new JSONObject();
                Map<String, String> params = dataProvider.getJsonParameters(item);
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
        tag.put("class", "autocomplete");
        tag.remove("type");
        super.onComponentTag(tag);
    }

    public boolean isAddNewLinkEnabled() {
        return addNewLinkEnabled;
    }

    public void setAddNewLinkEnabled(boolean addNewLinkEnabled) {
        this.addNewLinkEnabled = addNewLinkEnabled;
    }

    public ValueField getValueField() {
        return valueField;
    }

    public HiddenField<ID> getIdField() {
        return idField;
    }

    public class ValueField extends TextField<String> {

        private static final long serialVersionUID = 8603979096217702996L;

        public ValueField(String id) {
            super(id, new Model<String>());
        }

        @Override
        public String getValidatorKeyPrefix() {
            return Autocomplete.this.getId();
        }
    }

    @Override
    protected T convertValue(String[] value) throws ConversionException {

        String key = idField.getInput();
        if (!hasText(key)) {
            return null;
        }

        T oldObject = getModelObject();
        if(oldObject != null){
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
            super();
            this.parentModel = parentModel;
        }

        @Override
        public void detach() {
        }

        @Override
        public ID getObject() {
            return parentModel.getObject();
        }

        @Override
        public void setObject(ID object) {
            // parentModel.setObject(object);
        }
    }

    private class ValueModel implements IModel<String> {

        @Override
        public String getObject() {
            T object = Autocomplete.this.getModelObject();
            if(object != null){
                return dataProvider.getValue(object);
            }
            return null;
        }

        @Override
        public void detach() {
        }

        @Override
        public void setObject(String object) {
        }
    }
}