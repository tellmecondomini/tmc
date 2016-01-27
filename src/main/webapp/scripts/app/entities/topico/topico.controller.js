'use strict';

angular.module('tmcApp')
    .controller('TopicoController', function ($scope, $http, Topico, Principal) {

        Principal.identity().then(function (account) {
            $scope.account = account;
        });

        $scope.topicos = [];
        $scope.encerrados = [];
        $scope.loadAll = function () {
            Topico.query(function (result) {
                angular.forEach(result, function (topico) {
                    if (['ENCERRADO_COM_SUCESSO', 'ENCERRADO_SEM_SUCESSO'].indexOf(topico.statusTopico) > -1)
                        $scope.encerrados.push(topico);
                    else
                        $scope.topicos.push(topico);
                });
            });
        };
        $scope.loadAll();

        $scope.delete = function (id) {
            Topico.get({id: id}, function (result) {
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
            $scope.topico = {
                titulo: null,
                descricao: null,
                data: null,
                aprovado: null,
                id: null
            };
        };

        $scope.getDescricaoStatusTopico = function (status) {
            if (status == "AGUARDANDO_APROVACAO")
                return "Aguardando aprovação";
            else if (status == "ABERTO")
                return "Aberto";
            else if (status == "ENCERRADO_COM_SUCESSO")
                return "Encerrado com sucesso";
            else if (status == "ENCERRADO_SEM_SUCESSO")
                return "Encerrado sem sucesso";
            else
                return "Reprovado";
        };

    });
