package lt.dm3;

import org.apache.wicket.markup.html.link.BookmarkablePageLink;

import lt.inventi.wicket.component.breadcrumb.h.PreviousPageLink;

public class FirstPage extends BaseAuthenticatedPage {

    public FirstPage() {
        add(new BookmarkablePageLink<FormPage>("registerPerson", FormPage.class));
        add(new PreviousPageLink("back"));
    }
}
