'use strict';

angular.module('tmcApp')
    .controller('TelefoneCondominioController', function ($scope, TelefoneCondominio) {
        $scope.telefoneCondominios = [];
        $scope.loadAll = function () {
            TelefoneCondominio.query(function (result) {
                $scope.telefoneCondominios = result;
            });
        };
        $scope.loadAll();

        $scope.delete = function (id) {
            TelefoneCondominio.get({id: id}, function (result) {
                $scope.telefoneCondominio = result;
                $('#deleteTelefoneCondominioConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            TelefoneCondominio.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteTelefoneCondominioConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.telefoneCondominio = {numero: null, id: null};
        };
    });
