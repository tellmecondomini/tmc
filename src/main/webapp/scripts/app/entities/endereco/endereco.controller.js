'use strict';

angular.module('tmcApp')
    .controller('EnderecoController', function ($scope, Endereco) {
        $scope.enderecos = [];
        $scope.loadAll = function() {
            Endereco.query(function(result) {
               $scope.enderecos = result;
            });
        };
        $scope.loadAll();

        $scope.delete = function (id) {
            Endereco.get({id: id}, function(result) {
                $scope.endereco = result;
                $('#deleteEnderecoConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            Endereco.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteEnderecoConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.endereco = {logradouro: null, numero: null, bairro: null, cidade: null, uf: null, id: null};
        };
    });
