'use strict';

angular.module('tmcApp')
    .controller('TelefoneFuncionarioDetailController', function ($scope, $rootScope, $stateParams, entity, TelefoneFuncionario, Funcionario) {
        $scope.telefoneFuncionario = entity;
        $scope.load = function (id) {
            TelefoneFuncionario.get({id: id}, function (result) {
                $scope.telefoneFuncionario = result;
            });
        };
        $rootScope.$on('tmcApp:telefoneFuncionarioUpdate', function (event, result) {
            $scope.telefoneFuncionario = result;
        });
    });
