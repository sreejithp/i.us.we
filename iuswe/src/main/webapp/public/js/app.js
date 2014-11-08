// Declare app level module which depends on views, and components
var app = angular.module('IUsWe', [
    'ngRoute',
    'ui.bootstrap',
    'ajoslin.promise-tracker',
    'dialogs.main'
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
        principal: username,
        password: password
      }).then(function (response) {
        if (response.success)
          $state.setAuthenticatedUser(response.result);
        return response;
      });
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
        pledgeWeekDay: pledgeWeekDay
      }).then(function (response) {
        if (response.success)
          $state.setAuthenticatedUser(response.result);
        return response;
      });
    };
    return {
      register: register,
      signin: signin
    };
  }
]);
app.controller('HomePageController', [
  '$log',
  '$scope',
  '$routeParams',
  'UserService',
  function ($log, $scope, $routeParams, UserService) {
    $scope.signup = function () {
      // Register user
      $log.debug('clicked register');
      UserService.register('randomEmail', 'randomPassword', 'name', 'address', 'location', 1, 1, 1).then(function (response) {
        $log.debug(response);
      });
    };
    $scope.signIn = function () {
      $log.debug('cliced signIn');
      UserService.signin('email', 'password').then(function (response) {
        $log.debug(response);
      });
    };
  }
]);
app.config([
  '$routeProvider',
  '$locationProvider',
  function ($routeProvider, $locationProvider) {
    $locationProvider.html5Mode(true);
    $routeProvider.when('/view1', {
      templateUrl: 'public/views/view1.html',
      controller: 'View1Ctrl'
    }).when('/view2', {
      templateUrl: 'public/views/view1.html',
      controller: 'View1Ctrl'
    }).otherwise({ redirectTo: '/' });
  }
]);