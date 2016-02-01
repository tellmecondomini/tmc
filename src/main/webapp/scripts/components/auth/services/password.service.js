'use strict';

angular.module('tmcApp')
    .factory('Password', function ($resource) {
        return $resource('api/account/change_password', {}, {});
    });

angular.module('tmcApp')
    .factory('PasswordResetInit', function ($resource) {
        return $resource('api/account/reset_password/init', {}, {})
    });

angular.module('tmcApp')
    .factory('PasswordResetFinish', function ($resource) {
        return $resource('api/account/reset_password/finish', {}, {})
    });
