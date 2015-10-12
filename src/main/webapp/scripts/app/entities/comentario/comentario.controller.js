'use strict';

angular.module('tmcApp')
    .controller('ComentarioController', function ($scope, Comentario) {
        $scope.comentarios = [];
        $scope.loadAll = function() {
            Comentario.query(function(result) {
               $scope.comentarios = result;
            });
        };
        $scope.loadAll();

        $scope.delete = function (id) {
            Comentario.get({id: id}, function(result) {
                $scope.comentario = result;
                $('#deleteComentarioConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            Comentario.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteComentarioConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.comentario = {conteudo: null, data: null, ativo: null, id: null};
        };
    });
