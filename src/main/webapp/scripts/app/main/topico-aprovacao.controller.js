'use strict';

angular.module('tmcApp')
    .controller('TopicoAprovacaoController', function ($scope, $modalInstance, Principal, entity, TopicoAprovacao) {

        $scope.topico = entity;

        Principal.identity().then(function (account) {
            $scope.account = account;
            $scope.isAuthenticated = Principal.isAuthenticated;
        });

        $scope.save = function () {
            TopicoAprovacao.updateAprovacao({
                id: $scope.topico.id,
                status: 'ABERTO',
                mensagem: $scope.topico.mensagemAprovacao
            }, function (result) {
                $modalInstance.close(result);
            });
        };

        $scope.clear = function () {
            $modalInstance.dismiss('cancel');
        };

    });
