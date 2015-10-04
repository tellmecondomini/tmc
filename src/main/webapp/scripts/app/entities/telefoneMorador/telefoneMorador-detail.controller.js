'use strict';

angular.module('tmcApp')
    .controller('TelefoneMoradorDetailController', function ($scope, $rootScope, $stateParams, entity, TelefoneMorador, Morador) {
        $scope.telefoneMorador = entity;
        $scope.load = function (id) {
            TelefoneMorador.get({id: id}, function(result) {
                $scope.telefoneMorador = result;
            });
        };
        $rootScope.$on('tmcApp:telefoneMoradorUpdate', function(event, result) {
            $scope.telefoneMorador = result;
        });
    });
