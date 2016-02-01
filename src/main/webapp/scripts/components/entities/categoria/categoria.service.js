'use strict';

angular.module('tmcApp')
    .factory('Categoria', function ($resource, DateUtils) {
        return $resource('api/categorias/:id', {}, {
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
