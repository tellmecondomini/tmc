'use strict';

angular.module('tmcApp')
    .factory('TelefoneFuncionario', function ($resource, DateUtils) {
        return $resource('api/telefoneFuncionarios/:id', {}, {
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
