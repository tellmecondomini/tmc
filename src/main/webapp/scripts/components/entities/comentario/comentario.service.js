'use strict';

angular.module('tmcApp')
    .factory('Comentario', function ($resource, DateUtils) {
        return $resource('api/comentarios/:id', {}, {
            'query': {method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    data.data = DateUtils.convertDateTimeFromServer(data.data);
                    return data;
                }
            },
            'update': {method: 'PUT'},
            'findByTopico': {
                method: 'GET',
                isArray: true
            }
        });
    })
    .factory('ComentarioDeleteByMorador', function ($resource) {
        return $resource('api/comentarios/remove/:id/:moradorId/:motivo', {}, {
            'execute': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    return data;
                }
            }
        });
    })
    .factory('SolicitacaoRemoveExiste', function ($resource) {
        return $resource('api/comentarios/remove/:idComentario', {}, {
            'execute': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    return data;
                }
            }
        });
    });
