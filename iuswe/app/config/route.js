app.config(['$routeProvider', '$locationProvider', function($routeProvider, $locationProvider) {
    $locationProvider.html5Mode(true);
    $routeProvider
        .when("/view1", {
            templateUrl: "public/views/view1.html",
            controller: 'View1Ctrl'
        })
        .when("/view2", {
            templateUrl: "public/views/view1.html",
            controller: "View1Ctrl"
        })
        .otherwise({redirectTo: "/"});
}]);
