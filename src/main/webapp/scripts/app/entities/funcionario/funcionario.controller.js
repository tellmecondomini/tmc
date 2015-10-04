'use strict';

angular.module('tmcApp')
    .controller('FuncionarioController', function ($scope, Funcionario) {
        $scope.funcionarios = [];
        $scope.loadAll = function() {
            Funcionario.query(function(result) {
               $scope.funcionarios = result;
            });
        };
        $scope.loadAll();

        $scope.delete = function (id) {
            Funcionario.get({id: id}, function(result) {
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
            $scope.funcionario = {nome: null, cpf: null, sexo: null, dataNascimento: null, email: null, senha: null, ativo: null, dataCadastro: null, id: null};
        };
    });
