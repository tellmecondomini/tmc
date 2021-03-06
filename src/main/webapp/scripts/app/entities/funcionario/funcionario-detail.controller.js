'use strict';

angular.module('tmcApp')
    .controller('FuncionarioDetailController', function ($scope, $rootScope, $stateParams, entity, Funcionario, Cep, Condominio, TelefoneFuncionario, Comentario) {
        $scope.funcionario = entity;
        $scope.load = function (id) {
            Funcionario.get({id: id}, function (result) {
                $scope.funcionario = result;
            });
        };
        $rootScope.$on('tmcApp:funcionarioUpdate', function (event, result) {
            $scope.funcionario = result;
        });
    });
