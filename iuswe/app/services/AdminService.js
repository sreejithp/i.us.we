app.factory('AdminService', function ($q, $timeout, $log, $state, HttpService) {

    var getFoodAvailabilityList = function () {
        return HttpService.get('/foodAvailabilityList').then(function (response) {
            return response;
        })
    };

    var getListOfContributors = function () {
        return HttpService.get('/user/listOfContributors').then(function (response) {
            return response;
        })
    };

    var getListOfVolunteers = function () {
        return HttpService.get('/user/listOfVolunteers').then(function (response) {
            return response;
        })
    };

    var getListOfNeedy = function () {
        return HttpService.get('/user/listOfNeedy').then(function (response) {
            return response;
        })
    };

    var assignVolunteerTask = function (contributorId, needyId) {
        return HttpService.post('/user/assignVolunteerTask', {
            contributorId: contributorId,
            needyId: needyId}).then(function (response) {
            return response;
        })
    };

    return {
        getFoodAvailabilityList: getFoodAvailabilityList,
        getListOfContributors: getListOfContributors,
        getListOfVolunteers: getListOfVolunteers,
        getListOfNeedy: getListOfNeedy,
        assignVolunteerTask: assignVolunteerTask
    };

});