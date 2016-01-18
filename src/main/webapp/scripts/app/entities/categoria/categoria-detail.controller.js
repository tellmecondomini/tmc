'use strict';

angular.module('tmcApp')
    .controller('CategoriaDetailController', function ($scope, $rootScope, $stateParams, entity, Categoria, Funcionario) {
        $scope.categoria = entity;
        $scope.load = function (id) {
            Categoria.get({id: id}, function(result) {
                $scope.categoria = result;
            });
        };
        $rootScope.$on('tmcApp:categoriaUpdate', function(event, result) {
            $scope.categoria = result;
        });
    });
