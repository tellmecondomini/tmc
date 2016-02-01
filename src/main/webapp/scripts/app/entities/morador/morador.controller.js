'use strict';

angular.module('tmcApp')
    .controller('MoradorController', function ($scope, $http, Morador, Principal, AccountMorador) {

        $scope.searchText = '';

        Principal.identity(true).then(function (account) {
            $scope.settingsAccount = account;
        });

        $scope.moradors = [];
        $scope.loadAll = function () {
            Morador.query(function (result) {
                AccountMorador.get(function (morador) {
                    if (morador.id == null)
                        $scope.moradors = result;
                    else
                        $scope.moradors.push(morador);
                });
            });
        };
        $scope.loadAll();

        $scope.delete = function (id) {
            Morador.get({id: id}, function (result) {
                $scope.morador = result;
                $('#deleteMoradorConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            Morador.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteMoradorConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.morador = {
                nome: null,
                cpf: null,
                sexo: null,
                email: null,
                senha: null,
                dataNascimento: null,
                ativo: null,
                bloqueiaAgendamento: null,
                tipo: null,
                id: null
            };
        };

        $scope.printRegisterCode = function () {
            var response = $http.get('api/moradors/print');
            response.success(function (report) {
                window.open(report.url, '_blank');
                return false;
            });
        };
    });
