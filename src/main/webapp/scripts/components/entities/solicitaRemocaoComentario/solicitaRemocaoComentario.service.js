'use strict';

angular.module('tmcApp')
    .factory('SolicitaRemocaoComentario', function ($resource, DateUtils) {
        return $resource('api/solicitaRemocaoComentarios/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    data.data = DateUtils.convertDateTimeFromServer(data.data);
                    data.dataAtendimento = DateUtils.convertDateTimeFromServer(data.dataAtendimento);
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    });
