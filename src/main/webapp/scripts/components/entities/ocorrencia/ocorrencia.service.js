'use strict';

angular.module('tmcApp')
    .factory('Ocorrencia', function ($resource, DateUtils) {
        return $resource('api/ocorrencias/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    data.dataAbertura = DateUtils.convertDateTimeFromServer(data.dataAbertura);
                    data.dataFechamento = DateUtils.convertDateTimeFromServer(data.dataFechamento);
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    });
