'use strict';

angular.module('tmcApp')
    .controller('CategoriaController', function ($scope, Categoria) {
        $scope.categorias = [];
        $scope.loadAll = function () {
            Categoria.query(function (result) {
                $scope.categorias = result;
            });
        };
        $scope.loadAll();

        $scope.delete = function (id) {
            Categoria.get({id: id}, function (result) {
                $scope.categoria = result;
                $('#deleteCategoriaConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            Categoria.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteCategoriaConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.categoria = {descricao: null, id: null};
        };
    });
