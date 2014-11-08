app.factory('UserService', function ($q, $timeout, $log, $state, HttpService) {

    var signin = function (username, password) {
        return HttpService.post('/auth/signin', {principal: username, password: password}).then(function (response) {
            if (response.success)
                $state.setAuthenticatedUser(response.result);

            return response;
        })
    };

    var register = function (email, password, name, address, location, pledge, pledgeDay, pledgeWeekDay) {
        return HttpService.post('/auth/register', {
            email: email,
            name: name,
            password: password,
            address: address,
            location: location,
            userType: 0,
            pledge: pledge,
            pledgeDay: pledgeDay,
            pledgeWeekDay: pledgeWeekDay}).then(function (response) {
            if (response.success)
                $state.setAuthenticatedUser(response.result);

            return response;
        });
    };

    return {
        register: register,
        signin: signin
    };

});