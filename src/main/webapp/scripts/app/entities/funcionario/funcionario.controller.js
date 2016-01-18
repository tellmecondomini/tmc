'use strict';

angular.module('tmcApp')
    .controller('FuncionarioController', function ($scope, Funcionario, Principal) {

        Principal.identity(true).then(function (account) {
            $scope.settingsAccount = account;
        });

        $scope.funcionarios = [];

        $scope.loadAll = function () {
            Funcionario.query(function (result) {
                $scope.funcionarios = result;
            });
        };

        $scope.loadAll();

        $scope.delete = function (id) {
            Funcionario.get({id: id}, function (result) {
                $scope.funcionario = result;
                $('#deleteFuncionarioConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            Funcionario.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteFuncionarioConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.funcionario = {
                id: null,
                nome: null,
                cpf: null,
                sexo: null,
                dataNascimento: null,
                email: null,
                senha: null,
                ativo: null,
                cepId: null,
                cep: null,
                logradouro: null,
                bairro: null,
                cidade: null,
                uf: null,
                numero: null,
                complemento: null,
                condominioId: null,
                condominioRazaoSocial: null
            };
        };
    });
