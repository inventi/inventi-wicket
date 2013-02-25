package lt.dm3;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.protocol.http.WebApplication;

import lt.inventi.wicket.component.breadcrumb.BreadcrumbsSettings;

public class WicketApplication extends WebApplication {

	@Override
    public Class<? extends WebPage> getHomePage() {
        return LoginPage.class;
	}

	@Override
    public void init() {
		super.init();

        getMarkupSettings().setStripWicketTags(true);
        new BreadcrumbsSettings().withDecoratedBookmarkableLinks().install(this);
        getStoreSettings().setInmemoryCacheSize(2);
        //getStoreSettings().setMaxSizePerSession(Bytes.bytes(100));
	}

}
