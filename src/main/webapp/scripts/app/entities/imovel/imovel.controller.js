'use strict';

angular.module('tmcApp')
    .controller('ImovelController', function ($scope, Imovel) {
        $scope.imovels = [];
        $scope.loadAll = function() {
            Imovel.query(function(result) {
               $scope.imovels = result;
            });
        };
        $scope.loadAll();

        $scope.delete = function (id) {
            Imovel.get({id: id}, function(result) {
                $scope.imovel = result;
                $('#deleteImovelConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            Imovel.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteImovelConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.imovel = {ruaBloco: null, numero: null, id: null};
        };
    });
