'use strict';

angular.module('tmcApp')
    .factory('PrestadorServico', function ($resource, DateUtils) {
        return $resource('api/prestadorServicos/:id', {}, {
            'query': {method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    return data;
                }
            },
            'update': {method: 'PUT'}
        });
    });
