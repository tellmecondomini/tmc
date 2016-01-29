'use strict';

angular.module('tmcApp')
    .factory('AvaliaCompetencia', function ($resource, DateUtils) {
        return $resource('api/avaliaCompetencias/:id', {}, {
            'query': {method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    return data;
                }
            },
            'update': {method: 'PUT'}
        });
    })
    .factory('NewAvaliacao', function ($resource) {
        return $resource('api/avaliaCompetencias/new/:idPrestador/:idCompetencia', {}, {
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    return data;
                }
            }
        });
    })
    .factory('GetAvaliacao', function ($resource) {
        return $resource('api/avaliaCompetencias/get/:idPrestador/:idCompetencia', {}, {
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    return data;
                }
            }
        });
    })
    .factory('GetAvaliacaoByMorador', function ($resource) {
        return $resource('api/avaliaCompetencias/get/:idPrestador/:idCompetencia/:idMorador', {}, {
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    return data;
                }
            }
        });
    })
    .factory('GetAvaliacoes', function ($resource) {
        return $resource('api/avaliaCompetencias/query/:idPrestador/:idCompetencia', {}, {
            'query': {method: 'GET', isArray: true}
        });
    })
    .factory('GetAprovacaoAvaliacao', function ($resource) {
        return $resource('api/avaliaCompetencias/aprovacao/:idAvaliacao/:aprovado/:observacao', {}, {
            'execute': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    return data;
                }
            }
        });
    });
