'use strict';

angular.module('tmcApp')
    .controller('ConvidadoDetailController', function ($scope, $rootScope, $stateParams, entity, Convidado, Agenda, Dependente) {
        $scope.convidado = entity;
        $scope.load = function (id) {
            Convidado.get({id: id}, function(result) {
                $scope.convidado = result;
            });
        };
        $rootScope.$on('tmcApp:convidadoUpdate', function(event, result) {
            $scope.convidado = result;
        });
    });
