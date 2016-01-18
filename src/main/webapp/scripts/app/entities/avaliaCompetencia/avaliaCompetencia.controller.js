'use strict';

angular.module('tmcApp')
    .controller('AvaliaCompetenciaController', function ($scope, AvaliaCompetencia) {
        $scope.avaliaCompetencias = [];
        $scope.loadAll = function() {
            AvaliaCompetencia.query(function(result) {
               $scope.avaliaCompetencias = result;
            });
        };
        $scope.loadAll();

        $scope.delete = function (id) {
            AvaliaCompetencia.get({id: id}, function(result) {
                $scope.avaliaCompetencia = result;
                $('#deleteAvaliaCompetenciaConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            AvaliaCompetencia.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteAvaliaCompetenciaConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.avaliaCompetencia = {nota: null, mensagem: null, ativo: null, id: null};
        };
    });
