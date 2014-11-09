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

    var assignVolunteerTask = function (volunteerId, needyId) {
        return HttpService.post('/user/assignVolunteerTask', {
            contributorId: volunteerId,
            needyId: needyId}).then(function (response) {
            return response;
        })
    };

    var addNeedy = function (name, address, totalPeople, loc, volunteers) {
        return HttpService.post('/user/createNeedy', {
            name: name,
            address: address,
            totalPeople: totalPeople,
            loc: loc,
            volunteers: volunteers}).then(function (response) {
            return response;
        })
    };

    return {
        getFoodAvailabilityList: getFoodAvailabilityList,
        getListOfContributors: getListOfContributors,
        getListOfVolunteers: getListOfVolunteers,
        getListOfNeedy: getListOfNeedy,
        assignVolunteerTask: assignVolunteerTask,
        addNeedy: addNeedy
    };

});