'use strict';

angular.module('tmcApp')
    .factory('Activate', function ($resource) {
        return $resource('api/activate', {}, {
            'get': {method: 'GET', params: {}, isArray: false}
        });
    });


