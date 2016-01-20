'use strict';

angular.module('tmcApp')
    .factory('Register', function ($resource) {
        return $resource('api/register', {}, {});
    })
    .factory('RegisterMorador', function ($resource) {
        return $resource('api/register/morador', {}, {});
    });
