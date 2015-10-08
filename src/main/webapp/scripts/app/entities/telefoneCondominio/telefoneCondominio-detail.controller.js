'use strict';

angular.module('tmcApp')
    .controller('TelefoneCondominioDetailController', function ($scope, $rootScope, $stateParams, entity, TelefoneCondominio, Condominio) {
        $scope.telefoneCondominio = entity;
        $scope.load = function (id) {
            TelefoneCondominio.get({id: id}, function(result) {
                $scope.telefoneCondominio = result;
            });
        };
        $rootScope.$on('tmcApp:telefoneCondominioUpdate', function(event, result) {
            $scope.telefoneCondominio = result;
        });
    });
