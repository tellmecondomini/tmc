'use strict';

angular.module('tmcApp')
    .controller('CondominioController', function ($scope, Condominio) {
        $scope.condominios = [];
        $scope.loadAll = function() {
            Condominio.query(function(result) {
               $scope.condominios = result;
            });
        };
        $scope.loadAll();

        $scope.delete = function (id) {
            Condominio.get({id: id}, function(result) {
                $scope.condominio = result;
                $('#deleteCondominioConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            Condominio.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteCondominioConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.condominio = {razaoSocial: null, cnpj: null, ativo: null, disposicao: null, id: null};
        };

    });
