'use strict';

angular.module('tmcApp')
    .factory('Topico', function ($resource, DateUtils) {
        return $resource('api/topicos/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    data.data = DateUtils.convertLocaleDateFromServer(data.data);
                    return data;
                }
            },
            'update': {
                method: 'PUT',
                transformRequest: function (data) {
                    data.data = DateUtils.convertLocaleDateToServer(data.data);
                    return angular.toJson(data);
                }
            },
            'save': {
                method: 'POST',
                transformRequest: function (data) {
                    data.data = DateUtils.convertLocaleDateToServer(data.data);
                    return angular.toJson(data);
                }
            }
        });
    });
