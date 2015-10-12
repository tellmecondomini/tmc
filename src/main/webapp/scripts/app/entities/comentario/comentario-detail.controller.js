'use strict';

angular.module('tmcApp')
    .controller('ComentarioDetailController', function ($scope, $rootScope, $stateParams, entity, Comentario, Topico) {
        $scope.comentario = entity;
        $scope.load = function (id) {
            Comentario.get({id: id}, function(result) {
                $scope.comentario = result;
            });
        };
        $rootScope.$on('tmcApp:comentarioUpdate', function(event, result) {
            $scope.comentario = result;
        });
    });
