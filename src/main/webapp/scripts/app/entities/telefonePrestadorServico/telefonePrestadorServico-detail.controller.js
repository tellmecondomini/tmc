'use strict';

angular.module('tmcApp')
    .controller('TelefonePrestadorServicoDetailController', function ($scope, $rootScope, $stateParams, entity, TelefonePrestadorServico, PrestadorServico) {
        $scope.telefonePrestadorServico = entity;
        $scope.load = function (id) {
            TelefonePrestadorServico.get({id: id}, function (result) {
                $scope.telefonePrestadorServico = result;
            });
        };
        $rootScope.$on('tmcApp:telefonePrestadorServicoUpdate', function (event, result) {
            $scope.telefonePrestadorServico = result;
        });
    });
