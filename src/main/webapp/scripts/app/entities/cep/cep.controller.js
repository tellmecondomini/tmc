'use strict';

angular.module('tmcApp')
    .controller('CepController', function ($scope, Cep) {
        $scope.ceps = [];
        $scope.loadAll = function() {
            Cep.query(function(result) {
               $scope.ceps = result;
            });
        };
        $scope.loadAll();

        $scope.delete = function (id) {
            Cep.get({id: id}, function(result) {
                $scope.cep = result;
                $('#deleteCepConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            Cep.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteCepConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.cep = {logradouro: null, bairro: null, cidade: null, uf: null, cep: null, id: null};
        };
    });
