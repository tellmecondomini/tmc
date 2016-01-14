'use strict';

angular.module('tmcApp')
    .factory('TopicoComentarios', function ($resource, DateUtils) {
        return $resource('api/:id/comentarios', {}, {
            'query': {method: 'GET', isArray: true}
        });
    });
