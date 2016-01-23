'use strict';

angular.module('tmcApp')
    .controller('TopicoEncerraController', function ($scope, $modalInstance, Principal, entity, TopicoEncerra) {

        $scope.topico = entity;

        $scope.observacao = '';

        $scope.solucao = 'SIM';

        Principal.identity().then(function (account) {
            $scope.account = account;
            $scope.isAuthenticated = Principal.isAuthenticated;
        });

        $scope.save = function () {
            TopicoEncerra.execute({
                id: $scope.topico.id,
                solucao: $scope.solucao,
                observacao: $scope.observacao
            }, function (result) {
                $modalInstance.close(result);
            });
        };

        $scope.clear = function () {
            $modalInstance.dismiss('cancel');
        };

    });
