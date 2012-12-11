function registerBounceEvent(cfg){
    var cmp = $("#"+cfg.markupId);
    cmp.keyup($.debounce(cfg.timeout, function(){
        cfg.callback();
    }));
}