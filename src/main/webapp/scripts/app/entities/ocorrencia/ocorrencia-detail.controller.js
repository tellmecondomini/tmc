'use strict';

angular.module('tmcApp')
    .controller('OcorrenciaDetailController', function ($scope, $rootScope, $stateParams, entity, Ocorrencia, OcorrenciaTipo, Morador) {
        $scope.ocorrencia = entity;
        $scope.load = function (id) {
            Ocorrencia.get({id: id}, function(result) {
                $scope.ocorrencia = result;
            });
        };
        $rootScope.$on('tmcApp:ocorrenciaUpdate', function(event, result) {
            $scope.ocorrencia = result;
        });
    });
