'use strict';

angular.module('tmcApp')
    .factory('Comentario', function ($resource, DateUtils) {
        return $resource('api/comentarios/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    data.data = DateUtils.convertDateTimeFromServer(data.data);
                    return data;
                }
            },
            'update': { method:'PUT' },
            'findByTopico': {
                method: 'GET',
                isArray: true
            }
        });
    });
