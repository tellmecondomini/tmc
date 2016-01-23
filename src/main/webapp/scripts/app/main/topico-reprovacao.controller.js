'use strict';

angular.module('tmcApp')
    .controller('TopicoReprovacaoController', function ($scope, $modalInstance, Principal, entity, TopicoReprovacao) {

        $scope.topico = entity;

        Principal.identity().then(function (account) {
            $scope.account = account;
            $scope.isAuthenticated = Principal.isAuthenticated;
        });

        $scope.save = function () {
            TopicoReprovacao.updateAprovacao({
                id: $scope.topico.id,
                status: 'REPROVADO',
                mensagem: $scope.topico.mensagemAprovacao
            }, function (result) {
                $modalInstance.close(result);
            });
        };

        $scope.clear = function () {
            $modalInstance.dismiss('cancel');
        };

    });
