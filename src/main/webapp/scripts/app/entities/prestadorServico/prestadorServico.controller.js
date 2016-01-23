'use strict';

angular.module('tmcApp')
    .controller('PrestadorServicoController', function ($scope, PrestadorServico, Principal) {

        Principal.identity(true).then(function (account) {
            $scope.settingsAccount = account;
        });

        $scope.prestadorServicos = [];
        $scope.loadAll = function () {
            PrestadorServico.query(function (result) {
                $scope.prestadorServicos = result;
            });
        };
        $scope.loadAll();

        $scope.delete = function (id) {
            PrestadorServico.get({id: id}, function (result) {
                $scope.prestadorServico = result;
                $('#deletePrestadorServicoConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            PrestadorServico.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deletePrestadorServicoConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.prestadorServico = {
                nome: null,
                email: null,
                documento: null,
                pessoa: null,
                numero: null,
                complemento: null,
                id: null
            };
        };
    });
