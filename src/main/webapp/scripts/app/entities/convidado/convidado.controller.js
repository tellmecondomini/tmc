'use strict';

angular.module('tmcApp')
    .controller('ConvidadoController', function ($scope, Convidado) {
        $scope.convidados = [];
        $scope.loadAll = function() {
            Convidado.query(function(result) {
               $scope.convidados = result;
            });
        };
        $scope.loadAll();

        $scope.delete = function (id) {
            Convidado.get({id: id}, function(result) {
                $scope.convidado = result;
                $('#deleteConvidadoConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            Convidado.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteConvidadoConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.convidado = {nome: null, cpf: null, email: null, id: null};
        };
    });
