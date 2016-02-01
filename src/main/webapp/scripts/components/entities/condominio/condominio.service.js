'use strict';

angular.module('tmcApp')
    .factory('Condominio', function ($resource, DateUtils) {
        return $resource('api/condominios/:id', {}, {
            'query': {method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    data.dataCadastro = DateUtils.convertDateTimeFromServer(data.dataCadastro);
                    return data;
                }
            },
            'update': {method: 'PUT'}
        });
    });
