'use strict';

angular.module('tmcApp')
    .factory('Agenda', function ($resource, DateUtils) {
        return $resource('api/agendas/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    data.data = DateUtils.convertDateTimeFromServer(data.data);
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    });
