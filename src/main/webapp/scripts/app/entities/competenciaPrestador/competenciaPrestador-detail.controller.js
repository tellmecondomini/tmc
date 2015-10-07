'use strict';

angular.module('tmcApp')
    .controller('CompetenciaPrestadorDetailController', function ($scope, $rootScope, $stateParams, entity, CompetenciaPrestador) {
        $scope.competenciaPrestador = entity;
        $scope.load = function (id) {
            CompetenciaPrestador.get({id: id}, function(result) {
                $scope.competenciaPrestador = result;
            });
        };
        $rootScope.$on('tmcApp:competenciaPrestadorUpdate', function(event, result) {
            $scope.competenciaPrestador = result;
        });
    });
