package lt.inventi.wicket.test;

import static lt.inventi.wicket.test.FuzzyComponentResolverUtils.findComponentPath;

import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.IFormSubmittingComponent;
import org.apache.wicket.util.file.File;
import org.apache.wicket.util.tester.BaseWicketTester;
import org.apache.wicket.util.tester.FormTester;

public class FuzzyFormTester extends FormTester {

    public FuzzyFormTester(String formPath, Form<?> form, BaseWicketTester wicketTester, boolean fillBlankString) {
        super(formPath, form, wicketTester, fillBlankString);
    }

    @Override
    public FormTester setValue(String formComponentId, String value) {
        return super.setValue(resolveFormComponentPath(formComponentId), value);
    }

    @Override
    public FormTester select(String formComponentId, int index) {
        return super.select(resolveFormComponentPath(formComponentId), index);
    }

    @Override
    public FormTester selectMultiple(String formComponentId, int[] indexes, boolean replace) {
        return super.selectMultiple(resolveFormComponentPath(formComponentId), indexes, replace);
    }

    @Override
    public FormTester setFile(String formComponentId, File file, String contentType) {
        return super.setFile(resolveFormComponentPath(formComponentId), file, contentType);
    }

    @Override
    public String getTextComponentValue(String id) {
        return super.getTextComponentValue(resolveFormComponentPath(id));
    }

    @Override
    public FormTester selectMultiple(String formComponentId, int[] indexes) {
        return super.selectMultiple(resolveFormComponentPath(formComponentId), indexes);
    }

    @Override
    public FormTester setValue(String checkBoxId, boolean value) {
        return super.setValue(resolveFormComponentPath(checkBoxId), value);
    }

    @Override
    public FormTester submit(String buttonComponentId) {
        return super.submit(resolveFormSubmittingComponentPath(buttonComponentId));
    }

    private String resolveFormComponentPath(String formComponentId) {
        return findComponentPath(getForm(), formComponentId, FormComponent.class);
    }

    private String resolveFormSubmittingComponentPath(String formComponentId) {
        return findComponentPath(getForm(), formComponentId, IFormSubmittingComponent.class);
    }

}
