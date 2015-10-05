'use strict';

angular.module('tmcApp')
    .controller('OcorrenciaTipoController', function ($scope, OcorrenciaTipo) {
        $scope.ocorrenciaTipos = [];
        $scope.loadAll = function() {
            OcorrenciaTipo.query(function(result) {
               $scope.ocorrenciaTipos = result;
            });
        };
        $scope.loadAll();

        $scope.delete = function (id) {
            OcorrenciaTipo.get({id: id}, function(result) {
                $scope.ocorrenciaTipo = result;
                $('#deleteOcorrenciaTipoConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            OcorrenciaTipo.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteOcorrenciaTipoConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.ocorrenciaTipo = {id: null};
        };
    });
