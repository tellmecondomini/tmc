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
                        value.solicitacaoJaExiste = $scope.isSolicitacaoJaExiste(value.id);
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

            $http.get("api/account/morador").then(
                function (result) {
                    $scope.moradorSolicitante = result.data;
                    if ($scope.moradorSolicitante) {
                        if ($scope.moradorSolicitante.id === $scope.comentario.morador.id)
                            $('#deleteComentarioByFuncionario').modal('show');
                        else
                            $('#deleteComentarioByMorador').modal('show');
                    }
                }, function (error) {
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

        $scope.isSolicitacaoJaExiste = function (id) {
            var filtered = [];
            angular.forEach($scope.solicitacoes, function (value, key) {
                if (value.aprovado === null && value.comentario.id === id)
                    this.push(value);
            }, filtered);
            return filtered.length <= 0;
        };

        $scope.load();

        /*$timeout(function () {
         $scope.refresh();
         }, 10000);*/

        $scope.openImage = function () {
            $('#modalImage').modal('show');
        };

    });
