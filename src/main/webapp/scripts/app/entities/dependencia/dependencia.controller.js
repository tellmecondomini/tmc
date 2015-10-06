'use strict';

angular.module('tmcApp')
    .controller('DependenciaController', function ($scope, Dependencia) {
        $scope.dependencias = [];
        $scope.loadAll = function() {
            Dependencia.query(function(result) {
               $scope.dependencias = result;
            });
        };
        $scope.loadAll();

        $scope.delete = function (id) {
            Dependencia.get({id: id}, function(result) {
                $scope.dependencia = result;
                $('#deleteDependenciaConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            Dependencia.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteDependenciaConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.dependencia = {nome: null, disponivel: null, capacidade: null, custoAdicional: null, regraUso: null, id: null};
        };
    });
