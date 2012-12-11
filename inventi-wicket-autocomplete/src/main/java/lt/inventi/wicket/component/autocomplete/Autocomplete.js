$.ui.widget.subclass('ui.superautocomplete', $.ui.autocomplete.prototype);
$.ui.superautocomplete.subclass('ui.objectautocomplete', {

    options: {
        minLength: 0
    },

    _create: function() {
        var self = this;
        this.lastValue = "";
        this.hiddenField = this.element.find("~ input[type='hidden']");
        this.dropDownButton = this.element.find("~ button");
        this.spinner = this.element.find("~ span");
        
        if(this.hiddenField.length == 0 
        	|| this.dropDownButton.length == 0
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

        if (this.element.val() == '') {
            this.dropDownButton.addClass('show');
        }

        this.element.change(function() {
            if (self.lastSelected != self.element.val()) {
                self.hiddenField.val("");
            }
            if (self.element.val() != '') {
                self.dropDownButton.removeClass('show');
            } else {
                self.dropDownButton.addClass('show');
            }
        });
    },

    close: function(event) {
        if (this.menu.element.is(":visible")) {
            this.element.change();
        }
        this._super(event);
    },


    _trigger: function(name, event, obj) {
        if (name == "select") {
            this.hiddenField.val(obj.item.id);
            this.lastSelected = obj.item.value;
        }
        this._super(name, event, obj);
    },

    _renderItem: function(ul, item) {

        if (item.addNew) {
        	var link = $("#"+item.id).parent().clone();
        	link.appendTo(ul);
        	link.show();
        } else {
            this._super(ul, item);
        }
    },

    _search: function(value) {
        this._super(value);
        this.dropDownButton.removeClass("show");
        this.spinner.addClass("show");
    },
    _response: function(content) {
        this.spinner.removeClass('show');
        this._super(content);
    },

    setCustomValue: function(id, value) {
        this.hiddenField.val(id);
        this.lastSelected = value;
        this.element.val(value);
        this.element.change();
    }
});