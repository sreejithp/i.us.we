// Declare app level module which depends on views, and components
var app = angular.module('IUsWe', [
    'ngRoute',
    'ui.bootstrap',
    'ajoslin.promise-tracker',
    'dialogs.main'
  ]);
app.factory('AdminService', [
  '$q',
  '$timeout',
  '$log',
  '$state',
  'HttpService',
  function ($q, $timeout, $log, $state, HttpService) {
    var getFoodAvailabilityList = function () {
      return HttpService.get('/foodAvailabilityList').then(function (response) {
        return response;
      });
    };
    var getListOfContributors = function () {
      return HttpService.get('/user/listOfContributors').then(function (response) {
        return response;
      });
    };
    var getListOfVolunteers = function () {
      return HttpService.get('/user/listOfVolunteers').then(function (response) {
        return response;
      });
    };
    var getListOfNeedy = function () {
      return HttpService.get('/user/listOfNeedy').then(function (response) {
        return response;
      });
    };
    var assignVolunteerTask = function (volunteerId, needyId) {
      return HttpService.post('/user/assignVolunteerTask', {
        contributorId: volunteerId,
        needyId: needyId
      }).then(function (response) {
        return response;
      });
    };
    var addNeedy = function (name, address, totalPeople, loc, volunteers) {
      return HttpService.post('/user/createNeedy', {
        name: name,
        address: address,
        totalPeople: totalPeople,
        loc: loc,
        volunteers: volunteers
      }).then(function (response) {
        return response;
      });
    };
    return {
      getFoodAvailabilityList: getFoodAvailabilityList,
      getListOfContributors: getListOfContributors,
      getListOfVolunteers: getListOfVolunteers,
      getListOfNeedy: getListOfNeedy,
      assignVolunteerTask: assignVolunteerTask,
      addNeedy: addNeedy
    };
  }
]);
app.factory('HttpService', [
  '$log',
  '$q',
  '$http',
  '$timeout',
  '$state',
  '$sce',
  function ($log, $q, $http, $timeout, $state, $sce) {
    var api_root = window.AppProperties.baseUrl + '/api';
    var apiPath = function (path) {
      return api_root + path;
    };
    var get = function (path, queryData, returnOnlySuccess, isCached, suspendLogging) {
      return http('get', path, undefined, queryData, returnOnlySuccess === undefined ? true : returnOnlySuccess, isCached, suspendLogging);
    };
    var post = function (path, postData, queryData, returnOnlySuccess) {
      return http('post', path, postData, queryData, returnOnlySuccess, false);
    };
    var httpDelete = function (path, postData, queryData, returnOnlySuccess) {
      return http('delete', path, postData, queryData, returnOnlySuccess, false);
    };
    var http = function (method, path, postData, queryData, returnOnlySuccess, isCached, suspendLogging) {
      const hasRawPostData = postData != undefined && _.isString(postData);
      const apiCall = method.toUpperCase() + ' ' + path + (hasRawPostData ? '(raw)' : '');
      //        $log.debug(apiCall);
      var deferred = $q.defer();
      function log_warn(d) {
        if (!suspendLogging)
          $log.warn(d);
      }
      function log_debug(d) {
        if (!suspendLogging)
          $log.debug(d);
      }
      function show_message(msg, typ) {
        if (!suspendLogging)
          $state.showMessage(msg, typ);
      }
      $http({
        method: method,
        cache: isCached,
        url: apiPath(path),
        data: undefined == postData ? undefined : hasRawPostData ? postData : $.param(postData),
        params: queryData,
        headers: { 'Content-Type': hasRawPostData ? 'application/json' : 'application/x-www-form-urlencoded' }
      }).success(function (data, status, headers, config) {
        if (data.success) {
          if (!suspendLogging) {
            log_debug(apiCall + ':success');
            log_debug(data);
          }
        } else {
          log_warn(apiCall + ':failure');
          log_warn(data);
        }
        if (returnOnlySuccess) {
          if (data.success === true)
            deferred.resolve(data.result);
          else {
            //                        var errorMessage = "Error: " + apiCall;
            var errorMessage = 'Error ';
            if (data.errorMessage)
              errorMessage += ' [' + data.errorMessage + ']';
            if (data.errorCode)
              errorMessage += ' Code: ' + data.errorCode;
            show_message(errorMessage);
            log_debug('Not returning data to caller because the call failed');
            deferred.reject(data.errorMessage);
          }
        } else {
          deferred.resolve(data);
        }
      }).error(function (data, status, headers, config) {
        if (data)
          show_message(apiCall + ' / Failure:' + data, 'danger');
        else
          show_message(apiCall + ' / Failed', 'danger');
        deferred.reject(data);
      });
      $state.loadingTracker().addPromise(deferred.promise);
      return deferred.promise;
    };
    var setParam = function (params, name, value) {
      if (value)
        params[name] = value;
      return params;
    };
    return {
      post: post,
      get: get,
      httpDelete: httpDelete,
      setParam: setParam
    };
  }
]);
app.factory('$state', [
  '$log',
  '$location',
  '$rootScope',
  'promiseTracker',
  '$timeout',
  function ($log, $location, $rootScope, promiseTracker, $timeout) {
    $log.debug('$state.init');
    $rootScope.some = true;
    $rootScope.loadingTracker = promiseTracker({ activationDelay: 500 });
    $rootScope.alerts = [];
    function setAuthenticatedUser(user) {
      $rootScope.authenticatedUser = user;
    }
    function showMessage(text, alertType, hideMillis) {
      if (!_.find($rootScope.alerts, { 'msg': text })) {
        $rootScope.alerts.push({
          type: alertType,
          msg: text
        });
        $timeout(function () {
          if ($rootScope.alerts.length > 0)
            $rootScope.alerts.splice(0, 1);
        }, hideMillis ? hideMillis : 7000);
      }
    }
    function broadcastGlobalEvent(eventName, eventObject) {
      $rootScope.$broadcast(eventName, eventObject);
    }
    return {
      broadcastGlobalEvent: broadcastGlobalEvent,
      showMessage: showMessage,
      setAuthenticatedUser: setAuthenticatedUser,
      authenticatedUser: function () {
        return $rootScope.authenticatedUser;
      },
      isLoggedIn: function () {
        return $rootScope.authenticatedUser != undefined;
      },
      loadingTracker: function () {
        return $rootScope.loadingTracker;
      }
    };
  }
]);
app.factory('UserService', [
  '$q',
  '$timeout',
  '$log',
  '$state',
  'HttpService',
  function ($q, $timeout, $log, $state, HttpService) {
    var signin = function (username, password) {
      return HttpService.post('/auth/signin', {
        email: username,
        password: password
      }).then(function (response) {
        if (response.success)
          $state.setAuthenticatedUser(response.result);
        return response;
      });
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
        pledgeWeekDay: pledgeWeekDay,
        lat: latitude,
        lng: longitude,
        totalCapacity: totalCapacity
      }).then(function (response) {
        if (response.success)
          $state.setAuthenticatedUser(response.result);
        return response;
      });
    };
    var getMyPickupPoints = function (volunteerId) {
      return HttpService.get('/user/myPickups', { volunteerId: volunteerId }).then(function (response) {
        return response;
      });
    };
    var getMyDeliveryPoints = function (volunteerId) {
      return HttpService.get('/user/myDeliveries', { volunteerId: volunteerId }).then(function (response) {
        return response;
      });
    };
    var postDeliveryInfo = function (volunteerId, needyId, comments) {
      return HttpService.post('/user/deliveryInfo', {
        volunteerId: volunteerId,
        needyId: needyId,
        comments: comments
      }).then(function (response) {
        return response;
      });
    };
    var getMyDeliveryInfo = function (userId) {
      return HttpService.get('/user/deliveryInfo', { userId: userId }).then(function (response) {
        return response;
      });
    };
    var getMyPledgeForToday = function (contributorId) {
      return HttpService.get('/user/pledge', { contributorId: contributorId }).then(function (response) {
        return response;
      });
    };
    var updateFoodAvailabilityStatus = function (contributorId, status) {
      return HttpService.post('/user/foodReady', {
        contributorId: contributorId,
        status: status
      }).then(function (response) {
        return response;
      });
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
  }
]);
app.controller('HomePageController', [
  '$log',
  '$scope',
  '$routeParams',
  'UserService',
  '$location',
  function ($log, $scope, $routeParams, UserService, $location) {
    $log.debug('home page init');
    $scope.input = {};
    $scope.signup = function () {
      // Register user
      $log.debug('clicked register');
      if ($scope.input.pledgeWeekDay != undefined) {
        $scope.input.pledge = 2;
        $scope.input.pledgeDay = 0;
      } else if ($scope.input.pledgeType == 0) {
        $scope.input.pledge = 0;
        $scope.input.pledgeDay = 0;
        $scope.input.pledgeWeekDay = 0;
      } else if ($scope.input.pledgeType == 1) {
        $scope.input.pledge = 1;
        $scope.input.pledgeDay = 1;
        $scope.input.pledgeWeekDay = 0;
      }
      UserService.register($scope.input.registerEmail, $scope.input.registerPassword, $scope.input.registerName, $scope.input.registerAddress, $scope.input.userType, $scope.input.pledge, $scope.input.pledgeDay, $scope.input.pledgeWeekDay, $scope.input.latitude, $scope.input.longitude, $scope.input.totalCapacity).then(function (response) {
        $log.debug(response);
        if ($scope.input.userType == 2) {
          $location.path('/admin');
        } else {
          $location.path('/user');
        }
      });
    };
    $scope.signin = function () {
      $log.debug('clicked signIn');
      UserService.signin($scope.input.loginEmail, $scope.input.loginPassword).then(function (response) {
        $log.debug(response);
        $location.path('/user');
      });
    };
    $scope.forgotPassword = function () {
      $log.debug('going away');
      $location.path('/home');
    };
  }
]);
app.config([
  '$routeProvider',
  '$locationProvider',
  function ($routeProvider, $locationProvider) {
    $locationProvider.html5Mode(true);
    $routeProvider.otherwise({ redirectTo: '/' });
  }
]);