'use strict';

angular.module('tmcApp')
    .factory('Imovel', function ($resource, DateUtils) {
        return $resource('api/imovels/:id', {}, {
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
