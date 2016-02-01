'use strict';

angular.module('tmcApp')
    .controller('CepDetailController', function ($scope, $rootScope, $stateParams, entity, Cep) {
        $scope.cep = entity;
        $scope.load = function (id) {
            Cep.get({id: id}, function (result) {
                $scope.cep = result;
            });
        };
        $rootScope.$on('tmcApp:cepUpdate', function (event, result) {
            $scope.cep = result;
        });
    });
