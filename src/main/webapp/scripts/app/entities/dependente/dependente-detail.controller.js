'use strict';

angular.module('tmcApp')
    .controller('DependenteDetailController', function ($scope, $rootScope, $stateParams, entity, Dependente, Convidado) {
        $scope.dependente = entity;
        $scope.load = function (id) {
            Dependente.get({id: id}, function(result) {
                $scope.dependente = result;
            });
        };
        $rootScope.$on('tmcApp:dependenteUpdate', function(event, result) {
            $scope.dependente = result;
        });
    });
