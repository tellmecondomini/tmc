'use strict';

angular.module('tmcApp')
    .factory('OcorrenciaSubItem', function ($resource, DateUtils) {
        return $resource('api/ocorrenciaSubItems/:id', {}, {
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
