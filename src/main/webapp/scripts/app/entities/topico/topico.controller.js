'use strict';

angular.module('tmcApp')
    .controller('TopicoController', function ($scope, Topico) {
        $scope.topicos = [];
        $scope.loadAll = function() {
            Topico.query(function(result) {
               $scope.topicos = result;
            });
        };
        $scope.loadAll();

        $scope.delete = function (id) {
            Topico.get({id: id}, function(result) {
                $scope.topico = result;
                $('#deleteTopicoConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            Topico.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteTopicoConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.topico = {conteudo: null, data: null, aprovado: null, id: null};
        };
    });
