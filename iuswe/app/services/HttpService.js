app.factory('HttpService', function($log, $q, $http, $timeout, $state, $sce) {
    var api_root = window.AppProperties.baseUrl + '/api';

    var apiPath = function(path) {
        return api_root + path
    };

    var get = function(path, queryData, returnOnlySuccess, isCached, suspendLogging) {
        return http('get', path, undefined, queryData, (returnOnlySuccess === undefined) ? true : returnOnlySuccess, isCached, suspendLogging);
    };

    var post = function(path, postData, queryData, returnOnlySuccess) {
        return http('post', path, postData, queryData, returnOnlySuccess, false);
    };

    var httpDelete = function(path, postData, queryData, returnOnlySuccess) {
        return http('delete', path, postData, queryData, returnOnlySuccess, false);
    };

    var http = function(method, path, postData, queryData, returnOnlySuccess, isCached, suspendLogging) {
        const hasRawPostData = postData != undefined && _.isString(postData);
        const apiCall = method.toUpperCase() + " " + path + (hasRawPostData ? "(raw)" : "");
//        $log.debug(apiCall);

        var deferred = $q.defer();

        function log_warn(d) {
            if(!suspendLogging)
                $log.warn(d);
        }

        function log_debug(d) {
            if(!suspendLogging)
                $log.debug(d);
        }

        function show_message(msg, typ) {
            if(!suspendLogging)
                $state.showMessage(msg, typ);
        }

        $http({
            method: method,
            cache: isCached,
            url: apiPath(path),
            data: undefined==postData ? undefined : (hasRawPostData ? postData : $.param(postData)),
            params: queryData,
            headers: {'Content-Type': hasRawPostData ? 'application/json' : 'application/x-www-form-urlencoded'}}).
            success(function (data, status, headers, config) {
                if(data.success) {
                    if(!suspendLogging) {
                        log_debug(apiCall + ":success");
                        log_debug(data);
                    }
                } else {
                    log_warn(apiCall + ":failure");
                    log_warn(data);
                }
                if(returnOnlySuccess ) {
                    if(data.success === true)
                        deferred.resolve(data.result);
                    else {
//                        var errorMessage = "Error: " + apiCall;
                        var errorMessage = "Error ";
                        if(data.errorMessage)
                            errorMessage += " [" + data.errorMessage + "]";
                        if(data.errorCode)
                            errorMessage += " Code: " + data.errorCode;

                        show_message(errorMessage);
                        log_debug("Not returning data to caller because the call failed");
                        deferred.reject(data.errorMessage);
                    }
                } else {
                    deferred.resolve(data);
                }
            }).
            error(function (data, status, headers, config) {
                if(data)
                    show_message(apiCall + " / Failure:" + data, "danger");
                else
                    show_message(apiCall + " / Failed", "danger");
                deferred.reject(data);
            });

        $state.loadingTracker().addPromise(deferred.promise);

        return deferred.promise;
    };

    var setParam = function (params, name, value) {
        if(value)
            params[name] = value;

        return params;
    };

    return {
        post: post,
        get: get,
        httpDelete: httpDelete,
        setParam: setParam
    };
});


