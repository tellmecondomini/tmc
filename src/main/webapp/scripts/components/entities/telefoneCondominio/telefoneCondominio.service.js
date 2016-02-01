'use strict';

angular.module('tmcApp')
    .factory('TelefoneCondominio', function ($resource, DateUtils) {
        return $resource('api/telefoneCondominios/:id', {}, {
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
