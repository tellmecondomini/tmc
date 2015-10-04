'use strict';

angular.module('tmcApp')
    .controller('TelefoneFuncionarioController', function ($scope, TelefoneFuncionario) {
        $scope.telefoneFuncionarios = [];
        $scope.loadAll = function() {
            TelefoneFuncionario.query(function(result) {
               $scope.telefoneFuncionarios = result;
            });
        };
        $scope.loadAll();

        $scope.delete = function (id) {
            TelefoneFuncionario.get({id: id}, function(result) {
                $scope.telefoneFuncionario = result;
                $('#deleteTelefoneFuncionarioConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            TelefoneFuncionario.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteTelefoneFuncionarioConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.telefoneFuncionario = {numero: null, id: null};
        };
    });
