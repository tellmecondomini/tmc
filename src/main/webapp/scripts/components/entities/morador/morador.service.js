'use strict';

angular.module('tmcApp')
    .factory('Morador', function ($resource, DateUtils) {
        return $resource('api/moradors/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    data.dataNascimento = DateUtils.convertLocaleDateFromServer(data.dataNascimento);
                    return data;
                }
            },
            'update': {
                method: 'PUT',
                transformRequest: function (data) {
                    data.dataNascimento = DateUtils.convertLocaleDateToServer(data.dataNascimento);
                    return angular.toJson(data);
                }
            },
            'save': {
                method: 'POST',
                transformRequest: function (data) {
                    data.dataNascimento = DateUtils.convertLocaleDateToServer(data.dataNascimento);
                    return angular.toJson(data);
                }
            }
        });
    });
