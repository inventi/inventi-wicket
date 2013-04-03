package lt.inventi.wicket;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.protocol.http.WebApplication;

import de.agilecoders.wicket.webjars.util.WicketWebjars;

import lt.inventi.apollo.wicket.theme.settings.ThemeSettings;
import lt.inventi.wicket.component.breadcrumb.BreadcrumbsSettings;
import lt.inventi.wicket.js.JavaScriptSettings;
import lt.inventi.wicket.js.JavaScriptSettingsBuilder.JQueryUiVersions;
import lt.inventi.wicket.resource.ResourceSettings;

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

        WicketWebjars.install(this);
        JavaScriptSettings settings = JavaScriptSettings.newBuilder()
            .withJqueryUi(JQueryUiVersions.v1_9_2)
            .withAllUi(JqueryUiReference.get()).endJqueryUi().build();
        ResourceSettings.install(this, new ResourceSettings(new ThemeSettings(), settings));

        getMarkupSettings().setStripWicketTags(true);

        getStoreSettings().setInmemoryCacheSize(2);
        //getStoreSettings().setMaxSizePerSession(Bytes.bytes(100));
	}

}
