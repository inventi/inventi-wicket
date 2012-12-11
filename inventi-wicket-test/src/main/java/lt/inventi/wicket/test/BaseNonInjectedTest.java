package lt.inventi.wicket.test;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Locale;
import java.util.Properties;

import org.apache.wicket.Component;
import org.apache.wicket.Page;
import org.apache.wicket.Session;
import org.apache.wicket.resource.XmlFilePropertiesLoader;
import org.apache.wicket.resource.loader.IStringResourceLoader;
import org.apache.wicket.resource.loader.PackageStringResourceLoader;
import org.apache.wicket.util.tester.DummyHomePage;
import org.apache.wicket.util.tester.WicketTester;
import org.junit.After;
import org.junit.Before;

/**
 * Base test using Wicket's tester.
 * 
 * @author vplatonov
 * @author mjurkus
 * 
 */
public abstract class BaseNonInjectedTest {

    protected WicketTester tester;

    @Before
    public void startTester() {
        tester = new WicketTester() {
            @Override
            protected Page createPage() {
                Page testPage = createTestPage();
                return testPage == null ? super.createPage() : testPage;
            }
        };
        tester.startPage(DummyHomePage.class);

        tester.getApplication().getResourceSettings().getStringResourceLoaders().clear();
        tester.getApplication().getResourceSettings().getStringResourceLoaders().add(new TestResourceLoader());
    }

    @After
    public void destroyTester() {
        tester.destroy();
    }

    /**
     * Enable resource loading until next test. Should be used before render
     * takes its place, after that its to late as resources will be in cache
     * already
     */
    protected void enableResourceLoading() {
        Session.get().setLocale(new Locale("lt", "LT"));
        tester.getApplication().getResourceSettings().getStringResourceLoaders().clear();
        tester.getApplication().getResourceSettings().getStringResourceLoaders().add(new PackageStringResourceLoader());
    }

    protected Page createTestPage() {
        return null;
    }

    public static class TestResourceLoader implements IStringResourceLoader {

        private final Properties props;

        public TestResourceLoader() {
            URL url = getResource("test_properties.xml");
            if (url == null) {
                props = new Properties();
                return;
            }

            InputStream is = null;
            try {
                is = url.openStream();
                props = new XmlFilePropertiesLoader(".xml").loadJavaProperties(is);
            } catch (IOException e) {
                throw new RuntimeException(e);
            } finally {
                if (is != null) {
                    try {
                        is.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        @Override
        public String loadStringResource(Class<?> clazz, String key, Locale locale, String style, String variation) {
            return key;
        }

        @Override
        public String loadStringResource(Component component, String key, Locale locale, String style, String variation) {
            String prop = props.getProperty(key);
            return prop == null ? key : prop;
        }

    }

    private static URL getResource(String resourceName) {
        return BaseNonInjectedTest.class.getClassLoader().getResource(resourceName);
    }
}
