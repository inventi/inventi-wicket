$.widget('ui.objectautocomplete', $.ui.autocomplete, {

    options: {
        minLength: 0
    },

    _create: function() {
    	this._super("_create");
        var self = this;
        this.lastValue = "";
        this.hiddenField = this.element.find("~ input[type='hidden']");
        this.dropDownButton = this.element.find("~ .autocomplete-dropdown");
        this.clearButton = this.element.find("~ .autocomplete-clear");
        this.spinner = this.element.find("~ span");

        if (this.hiddenField.length == 0
            || this.dropDownButton.length == 0
            || this.clearButton.length == 0
            || this.spinner.length == 0){
                throw "Cant initialize autocomplete. Something wrong with the structure!";
        }

        this.dropDownButton.click(function(event) {
            if (self.menu.element.is(":visible")) {
                self.close();
                return;
            }
            self.search();
            self.element.focus();
        });

        this.clearButton.click($.proxy(self._clear, self));

        if (this.element.val() == '') {
            this.dropDownButton.addClass('show');
        } else {
            this.clearButton.addClass('show');
        }

        this.element.change(function() {
            if (self.lastSelected != self.element.val()) {
                self.hiddenField.val("");
            }
            if (self.element.val() != '') {
                self.clearButton.addClass('show');
                self.dropDownButton.removeClass('show');
            } else {
                self.clearButton.removeClass('show');
                self.dropDownButton.addClass('show');
            }
        });
    },
    
    _clear: function() {
        this.hiddenField.val('');
        this.element.val('');
        this.element.change();
    },

    /*
     * Update hidden field value when the autocomplete value is selected
     */
    _trigger: function(name, event, obj) {
        if (name === 'select') {
            this.hiddenField.val(obj.item.id);
            this.lastSelected = obj.item.value;
        } else if (name === 'search') {
            // Show spinner when starting search
            this.dropDownButton.removeClass('show');
            this.clearButton.removeClass('show');
            this.spinner.addClass('show');
        } else if (name === 'response') {
            // Hide spinner when results arrive (doesn't work with 1.8.16)
            this.spinner.removeClass('show');
        } else if (name === 'close') {
            // Always trigger onchange when menu closes
            // in order to show the dropDown button when nothing is selected.
            this.element.change();
        }
        this._super(name, event, obj);
    },

    /*
     * Render 'Add new item' link at the bottom of the result
     */
    _renderItem: function(ul, item) {
        if (item.addNew) {
        	var link = $("#"+item.id).parent().clone();
        	link.appendTo(ul);
        	link.show();
            return link;
        } 
        return this._super(ul, item);
    }

});
