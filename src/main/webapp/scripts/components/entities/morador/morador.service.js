'use strict';

angular.module('tmcApp')
    .factory('Morador', function ($resource, DateUtils) {
        return $resource('api/moradors/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    data.dataNascimento = DateUtils.convertDateTimeFromServer(data.dataNascimento);
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    });
