app.config(['$routeProvider', '$locationProvider', function($routeProvider, $locationProvider) {
    $locationProvider.html5Mode(true);
    $routeProvider
        .when("/", {
            templateUrl: "/public/views/page-landing.html",
            controller: 'HomePageController'
        })
        .when("/home", {
            templateUrl: "/public/views/page-landing.html",
            controller: 'HomePageController'
        })
        .when("/view2", {
            templateUrl: "/public/views/view1.html",
            controller: "View1Ctrl"
        })
        .otherwise({redirectTo: "/"});
}]);
