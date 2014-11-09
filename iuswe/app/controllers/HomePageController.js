app.controller('HomePageController', function ($log, $scope, $routeParams, UserService, $location) {

    $scope.signup = function () {
        // Register user
        $log.debug("clicked register");
        UserService.register("randomEmail", "randomPassword", "name", "address", "location", 1, 1, 1).then(function(response){
            $log.debug(response)
        })
    };

    $scope.signIn = function() {
        $log.debug("cliced signIn");
        UserService.signin("email", "password").then(function(response){
            $log.debug(response)
        })
    };

    $scope.forgotPassword = function() {
        $log.debug("going away");
        $location.path("/home");
    };
});
