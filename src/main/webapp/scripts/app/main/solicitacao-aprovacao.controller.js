'use strict';

angular.module('tmcApp')
    .controller('SolicitacaoAprovacaoController', function ($scope, $modalInstance, Principal, entity, SolicitaAprovacao) {

        $scope.solicitacao = entity;

        Principal.identity().then(function (account) {
            $scope.account = account;
            $scope.isAuthenticated = Principal.isAuthenticated;
        });

        $scope.aprova = function () {
            SolicitaAprovacao.execute({
                id: $scope.solicitacao.id,
                aprovado: true,
                observacao: $scope.solicitacao.observacao
            }, function (result) {
                $modalInstance.close(result);
            });
        };

        $scope.reprova = function () {
            SolicitaAprovacao.execute({
                id: $scope.solicitacao.id,
                aprovado: false,
                observacao: $scope.solicitacao.observacao
            }, function (result) {
                $modalInstance.close(result);
            });
        };

        $scope.clear = function () {
            $modalInstance.dismiss('cancel');
        };

    });
