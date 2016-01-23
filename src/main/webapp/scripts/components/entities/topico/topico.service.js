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
        return $resource('api/topico/aprovacao/:id/:status/:mensagem', {}, {
            'updateAprovacao': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    return data;
                }
            }
        });
    })
    .factory('TopicoReprovacao', function ($resource) {
        return $resource('api/topico/reprovacao/:id/:status/:mensagem', {}, {
            'updateAprovacao': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    return data;
                }
            }
        });
    })
    .factory('TopicoEncerra', function ($resource) {
        return $resource('api/topico/encerra/:id/:solucao/:observacao', {}, {
            'execute': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    return data;
                }
            }
        });
    });
