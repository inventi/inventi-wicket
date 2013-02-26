package lt.inventi.wicket.component.breadcrumb;

import java.lang.annotation.Annotation;

import org.apache.wicket.Application;
import org.apache.wicket.Component;
import org.apache.wicket.MetaDataKey;
import org.apache.wicket.Page;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.request.component.IRequestablePage;

import lt.inventi.wicket.component.breadcrumb.collapse.IBreadcrumbCollapser;
import lt.inventi.wicket.component.breadcrumb.collapse.NoopCollapser;
import lt.inventi.wicket.component.breadcrumb.collapse.RepeatingBreadcrumbCollapser;


public final class BreadcrumbsSettings {

    private static final MetaDataKey<BreadcrumbsSettings> KEY = new MetaDataKey<BreadcrumbsSettings>(){ /* empty */ };

    /**
     * PRIVATE API
     */
    static boolean useStatefulBreadcrumbLinks() {
        return Application.get().getMetaData(KEY).useStatefulBreadcrumbLinks;
    }

    static IBreadcrumbCollapser getBreadcrumbsCollapser() {
        return Application.get().getMetaData(KEY).collapser;
    }

    private IBreadcrumbPageFilter pageFilter = new IBreadcrumbPageFilter() {
        @Override
        public boolean shouldCreateBreadcrumbFor(IRequestablePage page) {
            if (page instanceof Page) {
                return ((Page) page).visitChildren(BreadcrumbsPanel.class).hasNext();
            }
            return false;
        }
    };

    private Class<? extends BookmarkablePageLink<?>> linkTypeToDecorate = null;

    private boolean useStatefulBreadcrumbLinks;

    private Integer timesToRepeatBeforeCollapse;
    private IBreadcrumbCollapser collapser;

    private IComponentBreadcrumbTitleProvider localizedTitleProvider = new LocalizedTitleProvider("breadcrumb");

    /**
     * Will create breadcrumbs only for pages annotated with the specified
     * annotation type.
     * <p>
     * By default breadcrumbs will be created for all of the pages containing an
     * instance of {@link BreadcrumbsPanel}.
     * <p>
     * Useful if you already use an annotation to distinguish pages which
     * require bound sessions (like {@code RequiresAuthentication}).
     *
     * @param annotation
     *            type of the annotation
     * @return current settings for chaining
     */
    public BreadcrumbsSettings forPagesAnnotatedWith(final Class<? extends Annotation> annotation) {
        this.pageFilter = new IBreadcrumbPageFilter() {
            @Override
            public boolean shouldCreateBreadcrumbFor(IRequestablePage page) {
                return page.getClass().getAnnotation(annotation) != null;
            }
        };
        return this;
    }

    /**
     * Will create breadcrumbs only for pages assignable to the specified type.
     * <p>
     * By default breadcrumbs will be created for all of the pages containing an
     * instance of {@link BreadcrumbsPanel}.
     * <p>
     * Useful if you have a common base class for pages requiring breadcrumbs.
     * You also may use {@code IBreadcrumbsOperations} as a parameter.
     *
     * @param type
     *            type of the page
     * @return current settings for chaining
     */
    public BreadcrumbsSettings forInstancesOf(final Class<?> type) {
        this.pageFilter = new IBreadcrumbPageFilter() {
            @Override
            public boolean shouldCreateBreadcrumbFor(IRequestablePage page) {
                return type.isAssignableFrom(page.getClass());
            }
        };
        return this;
    }

    /**
     * If set, all instances of {@link BookmarkablePageLink} on pages which
     * support breadcrumbs (specified by {@link #forPagesAnnotatedWith(Class)}
     * will be decorated with breadcrumb trail parameter. This way bookmarkable
     * links will automatically extend the breadcrumb trail.
     *
     * @return current settings for chaining
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public BreadcrumbsSettings withDecoratedBookmarkableLinks() {
        this.linkTypeToDecorate = (Class) BookmarkablePageLink.class;
        return this;
    }

    /**
     * If set, stateful links will be used in the {@code BreadcrumbsPanel}.
     * <p>
     * This must be used in tests if you want to click on breadcrumb links using
     * {@code WicketTester}.
     *
     * @return current settings for chaining
     */
    public BreadcrumbsSettings withStatefulBreadcrumbLinks() {
        this.useStatefulBreadcrumbLinks = true;
        return this;
    }

    /**
     * Sets the localization key to be used in order to find breadcrumb title
     * for the page. By default the key is set to <b>breadcrumb</b>.
     * <p>
     * Localized string will be resolved according to the Wicket's localization
     * rules starting from the {@code Page} the breadcrumb points to.
     *
     * @param localizationKey
     *            to be used in order to find breadcrumb title for the page
     * @return current settings for chaining
     */
    public BreadcrumbsSettings useKeyForBreadcrumbTitle(String localizationKey) {
        if (localizationKey == null || localizationKey.isEmpty()) {
            throw new IllegalArgumentException("Localization key for breadcrumbs cannot be empty!");
        }
        this.localizedTitleProvider = new LocalizedTitleProvider(localizationKey);
        return this;
    }

    /**
     * If set to a positive value, will force the {@code BreadcrumbsPanel} to
     * collapse breadcrumbs encountered more than {@code times} times.
     * <p>
     * For example, if {@code times} is 2 and you have a breadcrumb trail
     * consisting of
     *
     * <pre>
     * First / Second / First / Second
     * </pre>
     *
     * and the next page is <b>First</b>, the breadcrumb trail will become
     *
     * <pre>
     * ... / First
     * </pre>
     *
     * Where {@code ...} can be expanded to look at the collapsed part of the
     * trail.
     *
     * @param times
     *            the breadcrumb must be encountered in a single trail in order
     *            to be collapsed
     * @return current settings for chaining
     */
    public BreadcrumbsSettings collapseWhenRepeated(int times) {
        if (times < 1) {
            throw new IllegalArgumentException("Cannot collapse when repeated " + times + " times, must be positive!");
        }
        this.timesToRepeatBeforeCollapse = times;
        return this;
    }

    public void install(Application app) {
        if (this.timesToRepeatBeforeCollapse != null && this.timesToRepeatBeforeCollapse > 0) {
            this.collapser = new RepeatingBreadcrumbCollapser(timesToRepeatBeforeCollapse, new TypeBreadcrumbEquality());
        } else {
            this.collapser = new NoopCollapser();
        }

        app.setMetaData(KEY, this);
        app.getComponentPreOnBeforeRenderListeners().add(
            new BreadcrumbTrailExtendingListener(pageFilter, new DefaultTitleProvider(localizedTitleProvider)));
        if (linkTypeToDecorate != null) {
            app.getComponentInitializationListeners().add(
                new BookmarkableBreadcrumbPageInitializationListener(pageFilter, linkTypeToDecorate));
        }
    }

    private static class DefaultTitleProvider implements IComponentBreadcrumbTitleProvider {
        private IComponentBreadcrumbTitleProvider next;

        DefaultTitleProvider(IComponentBreadcrumbTitleProvider next) {
            this.next = next;
        }

        @Override
        public IModel<String> getBreadcrumbTitle(Component c) {
            if (c instanceof IBreadcrumbTitleProvider) {
                return ((IBreadcrumbTitleProvider) c).getBreadcrumbTitle();
            }
            return next.getBreadcrumbTitle(c);
        }
    }

    private static class LocalizedTitleProvider implements IComponentBreadcrumbTitleProvider {
        private final String key;

        LocalizedTitleProvider(String key) {
            this.key = key;
        }

        @Override
        public IModel<String> getBreadcrumbTitle(Component c) {
            return new StringResourceModel(key, c, c.getDefaultModel());
        }
    }
}
