app.factory('UserService', function ($q, $timeout, $log, $state, HttpService) {

    var signin = function (username, password) {
        return HttpService.post('/auth/signin', {principal: username, password: password}).then(function (response) {
            if (response.success)
                $state.setAuthenticatedUser(response.result);

            return response;
        })
    };

    var register = function (email, password, name, address, userType, pledge, pledgeDay, pledgeWeekDay, latitude, longitude, totalCapacity) {
        return HttpService.post('/auth/register', {
            email: email,
            name: name,
            password: password,
            address: address,
            userType: userType,
            pledge: pledge,
            pledgeDay: pledgeDay,
            pledgeWeekDay: pledgeWeekDay}).then(function (response) {
            if (response.success)
                $state.setAuthenticatedUser(response.result);

            return response;
        });
    };

    var getMyPickupPoints = function (volunteerId) {
        return HttpService.get('/user/myPickups', {volunteerId: volunteerId}).then(function (response) {
            return response;
        })
    };

    var getMyDeliveryPoints = function (volunteerId) {
        return HttpService.get('/user/myDeliveries', {volunteerId: volunteerId}).then(function (response) {
            return response;
        })
    };

    var postDeliveryInfo = function (volunteerId, needyId, comments) {
        return HttpService.post('/user/deliveryInfo', {
            volunteerId: volunteerId,
            needyId: needyId,
            comments: comments}).then(function (response) {
            return response;
        })
    };

    var getMyDeliveryInfo = function (userId) {
        return HttpService.get('/user/deliveryInfo', {userId: userId}).then(function (response) {
            return response;
        })
    };

    var getMyPledgeForToday = function (contributorId) {
        return HttpService.get('/user/pledge', {contributorId: contributorId}).then(function (response) {
            return response;
        })
    };

    var updateFoodAvailabilityStatus = function (contributorId, status) {
        return HttpService.post('/user/foodReady', {contributorId: contributorId, status: status}).then(function (response) {
            return response;
        })
    };

    return {
        register: register,
        signin: signin,
        getMyPickupPoints: getMyPickupPoints,
        getMyDeliveryPoints: getMyDeliveryPoints,
        getMyPledgeForToday: getMyPledgeForToday,
        getMyDeliveryInfo: getMyDeliveryInfo,
        postDeliveryInfo: postDeliveryInfo,
        updateFoodAvailabilityStatus: updateFoodAvailabilityStatus
    };

});