'use strict';

angular.module('tmcApp')
    .controller('SolicitaRemocaoComentarioController', function ($scope, SolicitaRemocaoComentario) {
        $scope.solicitaRemocaoComentarios = [];
        $scope.loadAll = function() {
            SolicitaRemocaoComentario.query(function(result) {
               $scope.solicitaRemocaoComentarios = result;
            });
        };
        $scope.loadAll();

        $scope.delete = function (id) {
            SolicitaRemocaoComentario.get({id: id}, function(result) {
                $scope.solicitaRemocaoComentario = result;
                $('#deleteSolicitaRemocaoComentarioConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            SolicitaRemocaoComentario.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteSolicitaRemocaoComentarioConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.solicitaRemocaoComentario = {data: null, motivo: null, dataAtendimento: null, observacao: null, id: null};
        };
    });
