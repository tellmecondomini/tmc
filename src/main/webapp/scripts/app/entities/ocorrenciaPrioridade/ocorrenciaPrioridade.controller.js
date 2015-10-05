'use strict';

angular.module('tmcApp')
    .controller('OcorrenciaPrioridadeController', function ($scope, OcorrenciaPrioridade) {
        $scope.ocorrenciaPrioridades = [];
        $scope.loadAll = function() {
            OcorrenciaPrioridade.query(function(result) {
               $scope.ocorrenciaPrioridades = result;
            });
        };
        $scope.loadAll();

        $scope.delete = function (id) {
            OcorrenciaPrioridade.get({id: id}, function(result) {
                $scope.ocorrenciaPrioridade = result;
                $('#deleteOcorrenciaPrioridadeConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            OcorrenciaPrioridade.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteOcorrenciaPrioridadeConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.ocorrenciaPrioridade = {descricao: null, id: null};
        };
    });
