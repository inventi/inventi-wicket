package lt.inventi.wicket.resource;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import org.apache.wicket.mock.MockApplication;
import org.junit.Test;

public class ResourceSettingsTest {

    @Test
    public void shouldRegisterResourceSettings() {
        MockApplication app = new MockApplication();
        assertThat(ResourceSettings.get(app), is(nullValue()));

        ResourceSettings.installEmpty(app);
        assertThat(ResourceSettings.get(app).js(), not(nullValue()));
        assertThat(ResourceSettings.get(app).themeSettings(), not(nullValue()));
    }
}
