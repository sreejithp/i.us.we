app.controller('HomePageController', function ($log, $scope, $routeParams, UserService, $location) {

    $log.debug("home page init");
    $scope.input = {};
    $scope.signup = function () {
        // Register user
        $log.debug("clicked register");

        if($scope.input.pledgeWeekDay != undefined) {
            $scope.input.pledge = 2;
            $scope.input.pledgeDay = 0;
        } else if($scope.input.pledgeType == 0) {
            $scope.input.pledge = 0;
            $scope.input.pledgeDay = 0;
            $scope.input.pledgeWeekDay = 0;
        } else if($scope.input.pledgeType == 1){
            $scope.input.pledge = 1;
            $scope.input.pledgeDay = 1;
            $scope.input.pledgeWeekDay = 0;
        }

        UserService.register($scope.input.registerEmail,
            $scope.input.registerPassword,
            $scope.input.registerName,
            $scope.input.registerAddress,
            $scope.input.userType,
            $scope.input.pledge,
            $scope.input.pledgeDay,
            $scope.input.pledgeWeekDay,
            $scope.input.latitude,
            $scope.input.longitude,
            $scope.input.totalCapacity).then(function(response){
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
