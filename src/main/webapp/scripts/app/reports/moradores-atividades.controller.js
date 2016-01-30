'use strict';

angular.module('tmcApp')
    .controller('MoradoresAtividadesController', function ($scope, AtividadesMorador) {

        $scope.atividades = [];
        $scope.loadAtividades = function () {
            AtividadesMorador.query(function (result) {
                $scope.atividades = result;
            });
        };
        $scope.loadAtividades();

    });
