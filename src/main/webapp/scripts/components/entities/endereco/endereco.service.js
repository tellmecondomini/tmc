'use strict';

angular.module('tmcApp')
    .factory('Endereco', function ($resource, DateUtils) {
        return $resource('api/enderecos/:id', {}, {
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
