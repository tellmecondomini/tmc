'use strict';

angular.module('tmcApp')
    .controller('OcorrenciaItemController', function ($scope, OcorrenciaItem) {
        $scope.ocorrenciaItems = [];
        $scope.loadAll = function() {
            OcorrenciaItem.query(function(result) {
               $scope.ocorrenciaItems = result;
            });
        };
        $scope.loadAll();

        $scope.delete = function (id) {
            OcorrenciaItem.get({id: id}, function(result) {
                $scope.ocorrenciaItem = result;
                $('#deleteOcorrenciaItemConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            OcorrenciaItem.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteOcorrenciaItemConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.ocorrenciaItem = {descricao: null, id: null};
        };
    });
