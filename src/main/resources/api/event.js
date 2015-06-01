var Event = {internal : {}};

Event.register = function(event, action) {
    org.ame.jsforge.EventBus.register(event, modID);
    Event.internal.registered[event] = action;
}

Event.internal.call = function(event) {
    var newArgs;
    for (var i = 0; i <= arguments.length; i++) {
        if (i === 0) {
            continue;
        }
        newArgs[i - 1] = arguments[i];
    }
    Event.internal.registered[event].apply(null, newArgs);
}
