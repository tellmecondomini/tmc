'use strict';

angular.module('tmcApp')
    .factory('Dependencia', function ($resource, DateUtils) {
        return $resource('api/dependencias/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    });
