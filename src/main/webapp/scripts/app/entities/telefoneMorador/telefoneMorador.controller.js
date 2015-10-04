'use strict';

angular.module('tmcApp')
    .controller('TelefoneMoradorController', function ($scope, TelefoneMorador) {
        $scope.telefoneMoradors = [];
        $scope.loadAll = function() {
            TelefoneMorador.query(function(result) {
               $scope.telefoneMoradors = result;
            });
        };
        $scope.loadAll();

        $scope.delete = function (id) {
            TelefoneMorador.get({id: id}, function(result) {
                $scope.telefoneMorador = result;
                $('#deleteTelefoneMoradorConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            TelefoneMorador.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteTelefoneMoradorConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.telefoneMorador = {numero: null, id: null};
        };
    });
