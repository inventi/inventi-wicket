package lt.inventi.wicket.test;

import static lt.inventi.wicket.test.FuzzyComponentResolverUtils.findComponent;
import static lt.inventi.wicket.test.FuzzyComponentResolverUtils.findComponentPath;

import javax.servlet.ServletContext;

import org.apache.wicket.Component;
import org.apache.wicket.Page;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.util.tester.FormTester;
import org.apache.wicket.util.tester.WicketTester;

public class FuzzyWicketTester extends WicketTester {

    public FuzzyWicketTester() {
        super();
    }

    public FuzzyWicketTester(Class<? extends Page> homePage) {
        super(homePage);
    }

    public FuzzyWicketTester(WebApplication application, ServletContext servletCtx) {
        super(application, servletCtx);
    }

    public FuzzyWicketTester(WebApplication application, String path) {
        super(application, path);
    }

    public FuzzyWicketTester(WebApplication application) {
        super(application);
    }

    @Override
    public FormTester newFormTester(String path, boolean fillBlankString) {
        Form<?> form = findComponent(getLastRenderedPage(), path, Form.class);
        return new FuzzyFormTester(form.getPageRelativePath(), form, this, fillBlankString);
    }

    @Override
    public Component getComponentFromLastRenderedPage(String path, boolean wantVisibleInHierarchy) {
        String actualPath = findComponentPath(getLastRenderedPage(), path, Component.class);
        return super.getComponentFromLastRenderedPage(actualPath, wantVisibleInHierarchy);
    }
}
