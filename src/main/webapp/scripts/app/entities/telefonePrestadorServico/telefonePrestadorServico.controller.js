'use strict';

angular.module('tmcApp')
    .controller('TelefonePrestadorServicoController', function ($scope, TelefonePrestadorServico) {
        $scope.telefonePrestadorServicos = [];
        $scope.loadAll = function () {
            TelefonePrestadorServico.query(function (result) {
                $scope.telefonePrestadorServicos = result;
            });
        };
        $scope.loadAll();

        $scope.delete = function (id) {
            TelefonePrestadorServico.get({id: id}, function (result) {
                $scope.telefonePrestadorServico = result;
                $('#deleteTelefonePrestadorServicoConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            TelefonePrestadorServico.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteTelefonePrestadorServicoConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.telefonePrestadorServico = {numero: null, id: null};
        };
    });
