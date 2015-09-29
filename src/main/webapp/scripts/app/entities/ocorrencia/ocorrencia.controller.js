'use strict';

angular.module('tmcApp')
    .controller('OcorrenciaController', function ($scope, Ocorrencia) {
        $scope.ocorrencias = [];
        $scope.loadAll = function() {
            Ocorrencia.query(function(result) {
               $scope.ocorrencias = result;
            });
        };
        $scope.loadAll();

        $scope.delete = function (id) {
            Ocorrencia.get({id: id}, function(result) {
                $scope.ocorrencia = result;
                $('#deleteOcorrenciaConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            Ocorrencia.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteOcorrenciaConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.ocorrencia = {dataAbertura: null, dataFechamento: null, menssagem: null, status: null, id: null};
        };
    });
