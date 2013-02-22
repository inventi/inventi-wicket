package lt.dm3;

import org.apache.wicket.markup.html.link.BookmarkablePageLink;

public class FirstPage extends BaseAuthenticatedPage {

    public FirstPage() {
        add(new BookmarkablePageLink<FormPage>("registerPerson", FormPage.class));
    }
}
