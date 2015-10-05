'use strict';

angular.module('tmcApp')
    .controller('OcorrenciaSubItemController', function ($scope, OcorrenciaSubItem) {
        $scope.ocorrenciaSubItems = [];
        $scope.loadAll = function() {
            OcorrenciaSubItem.query(function(result) {
               $scope.ocorrenciaSubItems = result;
            });
        };
        $scope.loadAll();

        $scope.delete = function (id) {
            OcorrenciaSubItem.get({id: id}, function(result) {
                $scope.ocorrenciaSubItem = result;
                $('#deleteOcorrenciaSubItemConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            OcorrenciaSubItem.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteOcorrenciaSubItemConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.ocorrenciaSubItem = {descricao: null, id: null};
        };
    });
