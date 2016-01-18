'use strict';

angular.module('tmcApp')
    .controller('AvaliaCompetenciaDetailController', function ($scope, $rootScope, $stateParams, entity, AvaliaCompetencia, Morador, PrestadorServico, CompetenciaPrestador) {
        $scope.avaliaCompetencia = entity;
        $scope.load = function (id) {
            AvaliaCompetencia.get({id: id}, function(result) {
                $scope.avaliaCompetencia = result;
            });
        };
        $rootScope.$on('tmcApp:avaliaCompetenciaUpdate', function(event, result) {
            $scope.avaliaCompetencia = result;
        });
    });
