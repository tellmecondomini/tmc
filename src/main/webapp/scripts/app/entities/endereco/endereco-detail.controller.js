'use strict';

angular.module('tmcApp')
    .controller('EnderecoDetailController', function ($scope, $rootScope, $stateParams, entity, Endereco) {
        $scope.endereco = entity;
        $scope.load = function (id) {
            Endereco.get({id: id}, function(result) {
                $scope.endereco = result;
            });
        };
        $rootScope.$on('tmcApp:enderecoUpdate', function(event, result) {
            $scope.endereco = result;
        });
    });
