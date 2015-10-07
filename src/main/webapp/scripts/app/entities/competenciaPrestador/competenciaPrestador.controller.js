'use strict';

angular.module('tmcApp')
    .controller('CompetenciaPrestadorController', function ($scope, CompetenciaPrestador) {
        $scope.competenciaPrestadors = [];
        $scope.loadAll = function() {
            CompetenciaPrestador.query(function(result) {
               $scope.competenciaPrestadors = result;
            });
        };
        $scope.loadAll();

        $scope.delete = function (id) {
            CompetenciaPrestador.get({id: id}, function(result) {
                $scope.competenciaPrestador = result;
                $('#deleteCompetenciaPrestadorConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            CompetenciaPrestador.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteCompetenciaPrestadorConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.competenciaPrestador = {descricao: null, id: null};
        };
    });
