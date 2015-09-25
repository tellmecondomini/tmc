 'use strict';

angular.module('tmcApp')
    .factory('notificationInterceptor', function ($q, AlertService) {
        return {
            response: function(response) {
                var alertKey = response.headers('X-tmcApp-alert');
                if (angular.isString(alertKey)) {
                    AlertService.success(alertKey, { param : response.headers('X-tmcApp-params')});
                }
                return response;
            },
        };
    });