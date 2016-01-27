'use strict';

angular.module('tmcApp')
    .factory('Funcionario', function ($resource, DateUtils) {
        return $resource('api/funcionarios/:id', {}, {
            'query': {method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    data.dataNascimento = DateUtils.convertDateTimeFromServer(data.dataNascimento);
                    data.dataCadastro = DateUtils.convertDateTimeFromServer(data.dataCadastro);
                    return data;
                }
            },
            'update': {method: 'PUT'}
        });
    });
