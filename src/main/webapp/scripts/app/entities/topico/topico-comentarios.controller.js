'use strict';

angular.module('tmcApp')
    .controller('TopicoComentariosController', function ($scope, $rootScope, $stateParams, entity, Topico, Assunto, Comentario, TopicoComentarios) {

        $scope.topico = entity;

        $scope.comentarios = [];

        $scope.comentario = {
            id: null,
            conteudo: null,
            data: null,
            topico: $scope.topico,
            funcionario: null,
            morador: null
        };

        $scope.load = function () {
            var id = $stateParams.id;
            TopicoComentarios.query({id: id}, function (result) {
                $scope.comentarios = result;
            });
        };

        $scope.load();

        var onSaveFinished = function (result) {
            $scope.refresh();
        };

        $scope.save = function () {
            if ($scope.comentario.id != null) {
                Comentario.update($scope.comentario, onSaveFinished);
            } else {
                Comentario.save($scope.comentario, onSaveFinished);
            }
        };

        $scope.clear = function () {
            $scope.comentario = {
                id: null,
                conteudo: null,
                data: null,
                topico: $scope.topico,
                funcionario: null,
                morador: null
            };
            $scope.comentarios = [];
        };

        $scope.refresh = function () {
            $scope.clear();
            $scope.load();
        };

        $rootScope.$on('tmcApp:topicoUpdate', function (event, result) {
            $scope.topico = result;
        });

    });