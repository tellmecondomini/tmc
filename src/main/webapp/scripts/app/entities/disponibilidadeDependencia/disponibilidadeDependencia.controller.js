'use strict';

angular.module('tmcApp')
    .controller('DisponibilidadeDependenciaController', function ($scope, DisponibilidadeDependencia) {
        $scope.disponibilidadeDependencias = [];
        $scope.loadAll = function() {
            DisponibilidadeDependencia.query(function(result) {
               $scope.disponibilidadeDependencias = result;
            });
        };
        $scope.loadAll();

        $scope.delete = function (id) {
            DisponibilidadeDependencia.get({id: id}, function(result) {
                $scope.disponibilidadeDependencia = result;
                $('#deleteDisponibilidadeDependenciaConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            DisponibilidadeDependencia.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteDisponibilidadeDependenciaConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.disponibilidadeDependencia = {diaSemana: null, horaInicio: null, horaFim: null, id: null};
        };
    });
