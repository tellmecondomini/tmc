'use strict';

angular.module('tmcApp')
    .controller('MainController', function ($scope, Principal, Topico, SolicitaRemocaoComentario, AvaliaCompetencia, AccountFuncionario) {

        Principal.identity().then(function (account) {
            $scope.account = account;
            $scope.isAuthenticated = Principal.isAuthenticated;
        });

        $scope.topicos = [];
        $scope.loadTopicosASeremAprovados = function () {
            Topico.query(function (result) {
                angular.forEach(result, function (topico) {
                    if (topico.statusTopico === 'AGUARDANDO_APROVACAO') {
                        if (['ROLE_FUNCIONARIO'].indexOf($scope.account.authorities[0]) > -1) {
                            AccountFuncionario.get(function (funcionario) {
                                var index = 0;
                                var length = funcionario.categorias.length;
                                for (var i = 0; i < length; i++) {
                                    if (funcionario.categorias[i].id === topico.categoria.id) {
                                        index = 1;
                                        break;
                                    }
                                }
                                if (index > 0) {
                                    $scope.topicos.push(topico);
                                }
                            });
                        } else {
                            $scope.topicos.push(topico);
                        }
                    }
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
                    if (value.ativo === null)
                        this.push(value);
                }, $scope.avaliacoes);
            });
        };
        $scope.loadAvaliacoes();

    });
