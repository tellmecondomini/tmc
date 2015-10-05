'use strict';

angular.module('tmcApp')
    .controller('OcorrenciaPrioridadeDetailController', function ($scope, $rootScope, $stateParams, entity, OcorrenciaPrioridade) {
        $scope.ocorrenciaPrioridade = entity;
        $scope.load = function (id) {
            OcorrenciaPrioridade.get({id: id}, function(result) {
                $scope.ocorrenciaPrioridade = result;
            });
        };
        $rootScope.$on('tmcApp:ocorrenciaPrioridadeUpdate', function(event, result) {
            $scope.ocorrenciaPrioridade = result;
        });
    });
