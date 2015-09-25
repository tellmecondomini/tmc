'use strict';

angular.module('tmcApp')
    .controller('LogoutController', function (Auth) {
        Auth.logout();
    });
