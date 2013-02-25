package lt.dm3;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.protocol.http.WebApplication;

import lt.inventi.wicket.component.breadcrumb.BreadcrumbsSettings;

public class InventiWicketExampleApplication extends WebApplication {

	@Override
    public Class<? extends WebPage> getHomePage() {
        return LoginPage.class;
	}

	@Override
    public void init() {
		super.init();

        new BreadcrumbsSettings()
            .withDecoratedBookmarkableLinks()
            .collapseWhenRepeated(2)
            .install(this);

        getMarkupSettings().setStripWicketTags(true);

        getStoreSettings().setInmemoryCacheSize(2);
        //getStoreSettings().setMaxSizePerSession(Bytes.bytes(100));
	}

}
