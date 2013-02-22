package lt.dm3;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.protocol.http.WebApplication;

import lt.inventi.wicket.component.breadcrumb.h.BreadcrumbsSettings;

public class WicketApplication extends WebApplication {

	@Override
    public Class<? extends WebPage> getHomePage() {
        return LoginPage.class;
	}

	@Override
    public void init() {
		super.init();

        new BreadcrumbsSettings().install(this);
	}

}
