/**
 * Hides blank input unitl user clicks the link
 */
$.widget("ui.blankInput", {

    options: {
    	id: 'blankField',
        label: "enter terxt",
        focus: ""
    },
    _create: function() {
        var field = this.element;
        
        // remove link if already exists
        var linkId = this.options.id + '_show';
        $('#' + linkId).remove();
        
        // remove div if already exits (rare case when component is added to ajax)
        var actionElement = $(this.element).siblings(".action").first();
        if(actionElement){
        	actionElement.remove();
        }

        if(!field.val()){
            var self = this;
            
            var link = $('<a id="' + linkId + '" href="#">' + this.options.label + '</a>');
            var div = $('<div class="action"></div>');
            field.hide();
            field.after(div.append(link));

            link.click(function() {
                div.hide();
                field.show();
                var focusNext = self.options.focus || field.attr('id');
                var element = $('#' + focusNext);
                if (!element.is('input')) {
                	var possibleInput = element.children('input');
                    if (possibleInput.length > 0) {
                    	element = possibleInput[0];
                    }
                }
                element.focus();
                return false;
            });
        }
    }
});