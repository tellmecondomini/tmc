'use strict';

angular.module('tmcApp')
    .controller('MainController', function ($scope, Principal, Topico, SolicitaRemocaoComentario, AvaliaCompetencia) {

        Principal.identity().then(function (account) {
            $scope.account = account;
            $scope.isAuthenticated = Principal.isAuthenticated;
        });

        $scope.topicos = [];
        $scope.loadTopicosASeremAprovados = function () {
            Topico.query(function (result) {
                angular.forEach(result, function (topico) {
                    if (topico.statusTopico === 'AGUARDANDO_APROVACAO')
                        $scope.topicos.push(topico);
                });
            });
        };
        $scope.loadTopicosASeremAprovados();

        $scope.solicitacoes = [];
        $scope.loadSolicitacoes = function () {
            SolicitaRemocaoComentario.query(function (result) {
                $scope.solicitacoes = result.filter(function (value) {
                    return value.dataAtendimento == null;
                })
            });
        };
        $scope.loadSolicitacoes();

        $scope.avaliacoes = [];
        $scope.loadAvaliacoes = function () {
            AvaliaCompetencia.query(function (result) {
                angular.forEach(result, function (value, key) {
                    if (value.ativo === false)
                        this.push(value);
                }, $scope.avaliacoes);
            });
        };
        $scope.loadAvaliacoes();

    });
