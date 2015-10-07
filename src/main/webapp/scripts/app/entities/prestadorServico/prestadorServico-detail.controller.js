'use strict';

angular.module('tmcApp')
    .controller('PrestadorServicoDetailController', function ($scope, $rootScope, $stateParams, entity, PrestadorServico, Cep, TelefonePrestadorServico, CompetenciaPrestador) {
        $scope.prestadorServico = entity;
        $scope.load = function (id) {
            PrestadorServico.get({id: id}, function(result) {
                $scope.prestadorServico = result;
            });
        };
        $rootScope.$on('tmcApp:prestadorServicoUpdate', function(event, result) {
            $scope.prestadorServico = result;
        });
    });
