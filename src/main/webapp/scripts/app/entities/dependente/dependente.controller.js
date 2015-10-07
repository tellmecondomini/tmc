'use strict';

angular.module('tmcApp')
    .controller('DependenteController', function ($scope, Dependente) {
        $scope.dependentes = [];
        $scope.loadAll = function() {
            Dependente.query(function(result) {
               $scope.dependentes = result;
            });
        };
        $scope.loadAll();

        $scope.delete = function (id) {
            Dependente.get({id: id}, function(result) {
                $scope.dependente = result;
                $('#deleteDependenteConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            Dependente.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteDependenteConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.dependente = {nome: null, documento: null, id: null};
        };
    });
