'use strict';

angular.module('tmcApp')
    .controller('AvaliaCompetenciaController', function ($scope, $http, AvaliaCompetencia, PrestadorServico, NotaAvaliacao, Principal) {

        Principal.identity(true).then(function (account) {
            $scope.settingsAccount = account;
        });

        $scope.max = 5;

        $scope.hoveringOver = function (value) {
            $scope.overStar = value;
            $scope.percent = 100 * (value / $scope.max);
        };

        $scope.getAvaliacao = function (prestador, competencia) {
            var promise = NotaAvaliacao.get({
                idPrestador: prestador.id,
                idCompetencia: competencia.id
            }, function (result) {
                return result;
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
