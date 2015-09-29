'use strict';

angular.module('tmcApp')
    .factory('Ocorrencia', function ($resource, DateUtils) {
        return $resource('api/ocorrencias/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    data.dataAbertura = DateUtils.convertLocaleDateFromServer(data.dataAbertura);
                    data.dataFechamento = DateUtils.convertLocaleDateFromServer(data.dataFechamento);
                    return data;
                }
            },
            'update': {
                method: 'PUT',
                transformRequest: function (data) {
                    data.dataAbertura = DateUtils.convertLocaleDateToServer(data.dataAbertura);
                    data.dataFechamento = DateUtils.convertLocaleDateToServer(data.dataFechamento);
                    return angular.toJson(data);
                }
            },
            'save': {
                method: 'POST',
                transformRequest: function (data) {
                    data.dataAbertura = DateUtils.convertLocaleDateToServer(data.dataAbertura);
                    data.dataFechamento = DateUtils.convertLocaleDateToServer(data.dataFechamento);
                    return angular.toJson(data);
                }
            }
        });
    });
