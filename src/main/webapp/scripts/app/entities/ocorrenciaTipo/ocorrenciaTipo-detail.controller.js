'use strict';

angular.module('tmcApp')
    .controller('OcorrenciaTipoDetailController', function ($scope, $rootScope, $stateParams, entity, OcorrenciaTipo, OcorrenciaItem, OcorrenciaSubItem, OcorrenciaPrioridade) {
        $scope.ocorrenciaTipo = entity;
        $scope.load = function (id) {
            OcorrenciaTipo.get({id: id}, function(result) {
                $scope.ocorrenciaTipo = result;
            });
        };
        $rootScope.$on('tmcApp:ocorrenciaTipoUpdate', function(event, result) {
            $scope.ocorrenciaTipo = result;
        });
    });
