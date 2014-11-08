app.factory('$state', function ($log, $location, $rootScope, promiseTracker, $timeout) {

    $log.debug('$state.init');
    $rootScope.some = true;
    $rootScope.loadingTracker = promiseTracker({activationDelay: 500});
    $rootScope.alerts = [];

    function setAuthenticatedUser(user) {
        $rootScope.authenticatedUser = user;
    }

    function showMessage(text, alertType, hideMillis) {
        if(!_.find($rootScope.alerts, {'msg': text})) {
            $rootScope.alerts.push({type: alertType, msg: text});
            $timeout(function() {
                if($rootScope.alerts.length > 0)
                    $rootScope.alerts.splice(0, 1);
            }, hideMillis ? hideMillis : 7000)
        }
    }

    function broadcastGlobalEvent(eventName, eventObject) {
        $rootScope.$broadcast(eventName, eventObject);
    }

    return {
        broadcastGlobalEvent: broadcastGlobalEvent,
        showMessage: showMessage,
        setAuthenticatedUser: setAuthenticatedUser,
        authenticatedUser: function() { return $rootScope.authenticatedUser},
        isLoggedIn: function() { return $rootScope.authenticatedUser != undefined},
        loadingTracker: function() { return $rootScope.loadingTracker}
    }
});