'use strict';

angular.module('tmcApp')
    .factory('Topico', function ($resource, DateUtils) {
        return $resource('api/topicos/:id', {}, {
            'query': {method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    data.data = DateUtils.convertDateTimeFromServer(data.data);
                    return data;
                }
            },
            'update': {method: 'PUT'}
        });
    })
    .factory('TopicoAprovacao', function ($resource) {
        return $resource('api/aprovacao/:id/:status/:mensagem', {}, {
            'updateAprovacao': {method: 'POST'}
        });
    });
