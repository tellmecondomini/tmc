'use strict';

angular.module('tmcApp')
    .controller('SolicitaRemocaoComentarioDetailController', function ($scope, $rootScope, $stateParams, entity, SolicitaRemocaoComentario, Comentario, Morador) {
        $scope.solicitaRemocaoComentario = entity;
        $scope.load = function (id) {
            SolicitaRemocaoComentario.get({id: id}, function (result) {
                $scope.solicitaRemocaoComentario = result;
            });
        };
        $rootScope.$on('tmcApp:solicitaRemocaoComentarioUpdate', function (event, result) {
            $scope.solicitaRemocaoComentario = result;
        });
    });
