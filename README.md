# Overview

This is set of wicket components, which we are using in our day-to-day projects.
Components are working ok, but you will have to dive into source to fully understand how they works.

Autocomplete
------------

Based on jquery autocomplete with few handy extenstions.
* Enables autocomplete to select objects
* Adds option "Create New" to the end of the list. It allows to create new item on the fly if it doesnt exist. For this to work you have to implement your own page , which will collect data and will create the item.
* Icon for expanding autocomplete with initial options. For users such autocompletes feels more convinient. 


Expandable List
---------------

Allows to add/remove items in the list without refreshing entire list view.


Breadcrumbs
-----------

Tracks user navigation and enables you to show breadcrumbs in the page. Also helps to navigate, for example to return to previous page you can just do goPreviousPage();


Misc components and behaviours
------------------------------

* Clickable row behaviour - makes entire table row clickable
* Static id initialization listener - generates static IDs for every component in the page. Usefull for UI testing.
* KeypressUpdatingBehaviour - uses debounce mechanism for keypress


License
-------

Licensed under the Apache Software Foundation license, version 2.0
