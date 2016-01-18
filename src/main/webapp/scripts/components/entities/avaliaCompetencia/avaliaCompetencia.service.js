'use strict';

angular.module('tmcApp')
    .factory('AvaliaCompetencia', function ($resource, DateUtils) {
        return $resource('api/avaliaCompetencias/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    });
