'use strict';

angular.module('tmcApp')
    .controller('CondominioDetailController', function ($scope, $rootScope, $stateParams, entity, Condominio, Endereco, Funcionario) {
        $scope.condominio = entity;
        $scope.load = function (id) {
            Condominio.get({id: id}, function(result) {
                $scope.condominio = result;
            });
        };
        $rootScope.$on('tmcApp:condominioUpdate', function(event, result) {
            $scope.condominio = result;
        });
    });
