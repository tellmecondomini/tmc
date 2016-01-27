'use strict';

angular.module('tmcApp')
    .controller('TopicoComentariosController', function ($scope, $rootScope, $stateParams, $timeout, $http, entity, Topico,
                                                         Assunto, Comentario, TopicoComentarios, Principal,
                                                         ComentarioDeleteByMorador, AccountMorador, SolicitaRemocaoComentario) {

        Principal.identity().then(function (account) {
            $scope.account = account;
        });

        $scope.topico = entity;

        $scope.comentarios = [];

        $scope.solicitacoes = [];

        $scope.comentario = {
            id: null,
            conteudo: null,
            data: null,
            topico: $scope.topico,
            funcionario: null,
            morador: null,
            solicitacaoJaExiste: false
        };

        $scope.load = function () {

            var id = $stateParams.id;

            SolicitaRemocaoComentario.query(function (solicitacoes) {
                $scope.solicitacoes = solicitacoes;
                TopicoComentarios.query({id: id}, function (comentarios) {
                    angular.forEach(comentarios, function (value, key) {
                        value.solicitacaoJaExiste = $scope.isSolicitacaoJaExite(value.id);
                        this.push(value);
                    }, $scope.comentarios);
                });
            });
        };

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
                morador: null,
                solicitacaoJaExiste: false
            };
            $scope.comentarios = [];
            $scope.solicitacoes = [];
        };

        $scope.refresh = function () {
            $scope.clear();
            $scope.load();
        };

        $rootScope.$on('tmcApp:topicoUpdate', function (event, result) {
            $scope.topico = result;
        });

        $scope.delete = function (id) {

            Comentario.get({id: id}, function (result) {
                $scope.comentario = result;
                $scope.motivo = "";
            });

            AccountMorador.get(function (morador) {
                $scope.moradorSolicitante = morador;
                if ($scope.moradorSolicitante)
                    $('#deleteComentarioByMorador').modal('show');
                else
                    $('#deleteComentarioByFuncionario').modal('show');
            });
        };

        $scope.confirmDeleteFuncionario = function (id) {
            Comentario.delete({id: id},
                function () {
                    $scope.load();
                    $('#deleteComentarioByFuncionario').modal('hide');
                    $scope.clear();
                });
        };

        $scope.confirmDeleteMorador = function (id) {
            ComentarioDeleteByMorador.execute({id: id, moradorId: $scope.moradorSolicitante.id, motivo: $scope.motivo},
                function () {
                    $scope.refresh();
                    $('#deleteComentarioByMorador').modal('hide');
                    $('#deleteComentarioByMoradorConfirmation').modal('show');
                });
        };

        $scope.isSolicitacaoJaExite = function (id) {
            var filtered = [];
            angular.forEach($scope.solicitacoes, function (value, key) {
                if (value.comentario.id === id)
                    this.push(value);
            }, filtered);
            return filtered.length <= 0;
        };

        $scope.load();

        /*$timeout(function () {
            $scope.refresh();
        }, 10000);*/
    });
