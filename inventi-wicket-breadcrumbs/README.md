# Breadcrumbs

According to [ux-patterns](http://ui-patterns.com/patterns/Breadcrumbs) and
[Quince](http://quince.infragistics.com/Patterns/Breadcrumbs.aspx), breadcrumbs
pattern has two primary concepts:

1. Structural - represents the hierarchy of the application
2. Historical - represents the historic trail of the user using the application

The goal of the implementation is to support both usage patterns in separate
and mixed modes. For example, imagine a simple CRM which lets you create new
customers, create relationships between the customers and edit customer
details. The information structure of such an application could look the following way:

    *
    `- Add new customer
    `- Customer [X] *
                    `- Add a relationship
                    `- Update contact details

Now imagine that during `Add a relationship` you could create a new customer
and add it as a relationship to the existing one in one step. Then, your
expected breadcrumb trail while adding a new customer would look like the
following:

    Customer [Existing] / Add a relationship / Add new customer

## Breadcrumbs in Wicket

Wicket has a Breadcrumb implementation which allows tracking historical trail
with great flexibility, although it only handles panels in its default
implementation. There are also numerous threads on the wicket-user mailing list
which usually conclude in someone coming up with a quick and dirty breadcrumbs
implementation suitable for their current application (like
[this one](http://www.mail-archive.com/wicket-user@lists.sourceforge.net/msg18012.html)).
That's ok, and the quick-and-dirty approach is actually better than producing
gargantuan configurable and extensible implementations which are painfully hard
to use and adapt to different needs.

Possible approach:

Each Session contains a `Map[Id -> Trail]` of breadcrumb trails for each
visited page where `Id` is unique for every visited page (might be derived from
the `(pageId, pageRenderCount, pageClass)` tuple).

When an `IRequestHandler` is resolved and is about to be rendered
(`IRequestCycleListener#onRequestHandlerResolved`), if the handler is a
`RenderPageRequestHandler` we

1. Get the current breadcrumb `Id` from the page parameters (this way we keep
   the Back button working).
2. Create a new `Breadcrumb` and add it into the `Trail` under the extracted
   `Id` if such a trail exists.
3. If no trail is found or no `Id` is present in the page parameters, a new
   trail is created using any static hierarchy metadata present in the
   breadcrumbs configuration for the current page and the current breadcrumb is
   appended (if needed).

`IRequestHandler` can point to:
1. A bookmarkable page with no page parameters
2. A bookmarkable page with page parameters
3. A stateful page with no live instances (all stale)
4. A stateful page which can be retrieved from the page manager
5. A page instance which is attached to the `PageProvider` (e.g. by `setResponsePage(new MyPage())`)
