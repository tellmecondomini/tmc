'use strict';

angular.module('tmcApp')
    .controller('OcorrenciaSubItemDetailController', function ($scope, $rootScope, $stateParams, entity, OcorrenciaSubItem) {
        $scope.ocorrenciaSubItem = entity;
        $scope.load = function (id) {
            OcorrenciaSubItem.get({id: id}, function(result) {
                $scope.ocorrenciaSubItem = result;
            });
        };
        $rootScope.$on('tmcApp:ocorrenciaSubItemUpdate', function(event, result) {
            $scope.ocorrenciaSubItem = result;
        });
    });
