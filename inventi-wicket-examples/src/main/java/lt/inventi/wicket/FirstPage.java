package lt.inventi.wicket;

import org.apache.wicket.markup.html.link.BookmarkablePageLink;

import lt.inventi.wicket.component.breadcrumb.PreviousPageLink;

public class FirstPage extends BaseAuthenticatedPage {

    public FirstPage() {
        add(new BookmarkablePageLink<FormPage>("registerPerson", FormPage.class));
        add(new PreviousPageLink("back"));
    }
}
