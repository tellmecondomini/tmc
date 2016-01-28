'use strict';

angular.module('tmcApp')
    .controller('AvaliaCompetenciaController', function ($scope, $http, AvaliaCompetencia, PrestadorServico,
                                                         GetAvaliacao, Principal, AccountMorador, GetAvaliacaoByMorador) {

        Principal.identity(true).then(function (account) {
            $scope.settingsAccount = account;
        });

        AccountMorador.get(function (morador) {
            $scope.moradorCorrente = morador;
            $scope.usuarioCorrenteEhMorador = ($scope.moradorCorrente != null);
        });

        $scope.max = 5;

        $scope.hoveringOver = function (value) {
            $scope.overStar = value;
            $scope.percent = 100 * (value / $scope.max);
        };

        $scope.getAvaliacao = function (prestador, competencia) {
            var promise = GetAvaliacao.get({
                idPrestador: prestador.id,
                idCompetencia: competencia.id
            }, function (result) {
                var promise = GetAvaliacaoByMorador.get({
                    idPrestador: prestador.id,
                    idCompetencia: competencia.id,
                    idMorador: $scope.moradorCorrente.id
                }, function (result) {
                    return result;
                }, function (error) {
                    console.log(error);
                    return false;
                });
                var avaliacao = result;
                avaliacao.isExistByDateAndMorador = promise;
                return avaliacao;
            });
            return promise;
        };

        $scope.avaliacoes = [];
        $scope.loadAll = function () {
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
        $scope.loadAll();

        //$scope.delete = function (id) {
        //    AvaliaCompetencia.get({id: id}, function (result) {
        //        $scope.avaliaCompetencia = result;
        //        $('#deleteAvaliaCompetenciaConfirmation').modal('show');
        //    });
        //};
        //
        //$scope.confirmDelete = function (id) {
        //    AvaliaCompetencia.delete({id: id},
        //        function () {
        //            $scope.loadAll();
        //            $('#deleteAvaliaCompetenciaConfirmation').modal('hide');
        //            $scope.clear();
        //        });
        //};

        //$scope.refresh = function () {
        //    $scope.loadAll();
        //    $scope.clear();
        //};

        //$scope.clear = function () {
        //    $scope.avaliaCompetencia = {nota: null, mensagem: null, ativo: null, id: null};
        //};
    });
