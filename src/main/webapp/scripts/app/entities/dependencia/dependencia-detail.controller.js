'use strict';

angular.module('tmcApp')
    .controller('DependenciaDetailController', function ($scope, $rootScope, $stateParams, entity, Dependencia, DisponibilidadeDependencia) {
        $scope.dependencia = entity;
        $scope.load = function (id) {
            Dependencia.get({id: id}, function(result) {
                $scope.dependencia = result;
            });
        };
        $rootScope.$on('tmcApp:dependenciaUpdate', function(event, result) {
            $scope.dependencia = result;
        });
    });
