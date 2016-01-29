'use strict';

angular.module('tmcApp')
    .controller('PrestadoresNotasController', function ($scope, Principal, PrestadorServico, GetAvaliacao, CompetenciaPrestador) {

        $scope.competencia = {};
        $scope.sexo = '';
        $scope.pessoa = '';
        $scope.cidade = '';

        Principal.identity().then(function (account) {
            $scope.account = account;
            $scope.isAuthenticated = Principal.isAuthenticated;
        });

        $scope.getAvaliacao = function (prestador, competencia) {
            var promise = GetAvaliacao.get({
                idPrestador: prestador.id,
                idCompetencia: competencia.id
            }, function (result) {
                return result;
            });
            return promise;
        };

        $scope.avaliacoes = [];
        $scope.loadAvaliacoes = function () {
            PrestadorServico.query(function (result) {
                if (result) {
                    angular.forEach(result, function (prestador, prestadorKey) {

                        var competencias = [];
                        angular.forEach(prestador.competencias, function (competencia, competenciaKey) {
                            var competencia = {
                                id: competencia.id,
                                descricao: competencia.descricao,
                                avaliacao: $scope.getAvaliacao(prestador, competencia)
                            };
                            this.push(competencia);
                        }, competencias);

                        var avaliacao = {
                            prestador: prestador,
                            competencias: competencias
                        };

                        this.push(avaliacao);

                    }, $scope.avaliacoes);
                }
            });
        };
        $scope.loadAvaliacoes();

        $scope.competencias = [];
        $scope.loadCompetencias = function () {
            CompetenciaPrestador.query(function (result) {
                $scope.competencias = result;
            });
        };
        $scope.loadCompetencias();

        $scope.filterBySexo = function (avaliacao) {
            if($scope.sexo === '')
                return true;
            return (avaliacao.prestador.sexo === $scope.sexo);
        };

    });
