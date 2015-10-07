'use strict';

angular.module('tmcApp')
    .factory('Convidado', function ($resource, DateUtils) {
        return $resource('api/convidados/:id', {}, {
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
