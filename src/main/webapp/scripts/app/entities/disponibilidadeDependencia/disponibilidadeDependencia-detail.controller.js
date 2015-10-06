'use strict';

angular.module('tmcApp')
    .controller('DisponibilidadeDependenciaDetailController', function ($scope, $rootScope, $stateParams, entity, DisponibilidadeDependencia, Dependencia) {
        $scope.disponibilidadeDependencia = entity;
        $scope.load = function (id) {
            DisponibilidadeDependencia.get({id: id}, function(result) {
                $scope.disponibilidadeDependencia = result;
            });
        };
        $rootScope.$on('tmcApp:disponibilidadeDependenciaUpdate', function(event, result) {
            $scope.disponibilidadeDependencia = result;
        });
    });
