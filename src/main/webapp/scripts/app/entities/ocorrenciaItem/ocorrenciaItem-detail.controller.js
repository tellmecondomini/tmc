'use strict';

angular.module('tmcApp')
    .controller('OcorrenciaItemDetailController', function ($scope, $rootScope, $stateParams, entity, OcorrenciaItem) {
        $scope.ocorrenciaItem = entity;
        $scope.load = function (id) {
            OcorrenciaItem.get({id: id}, function(result) {
                $scope.ocorrenciaItem = result;
            });
        };
        $rootScope.$on('tmcApp:ocorrenciaItemUpdate', function(event, result) {
            $scope.ocorrenciaItem = result;
        });
    });
