'use strict';

angular.module('tmcApp')
    .factory('TelefonePrestadorServico', function ($resource, DateUtils) {
        return $resource('api/telefonePrestadorServicos/:id', {}, {
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
