'use strict';

angular.module('tmcApp')
    .controller('AvaliacaoAprovacaoController', function ($scope, $modalInstance, Principal, entity, GetAprovacaoAvaliacao) {

        $scope.avaliacao = entity;

        $scope.observacao = "";

        Principal.identity().then(function (account) {
            $scope.account = account;
            $scope.isAuthenticated = Principal.isAuthenticated;
        });

        $scope.aprova = function () {
            GetAprovacaoAvaliacao.execute({
                idAvaliacao: $scope.avaliacao.id,
                aprovado: true,
                observacao: $scope.observacao
            }, function (result) {
                $modalInstance.close(result);
            });
        };

        $scope.reprova = function () {
            GetAprovacaoAvaliacao.execute({
                idAvaliacao: $scope.avaliacao.id,
                aprovado: false,
                observacao: $scope.observacao
            }, function (result) {
                $modalInstance.close(result);
            });
        };

        $scope.clear = function () {
            $modalInstance.dismiss('cancel');
        };

    });
