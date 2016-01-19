'use strict';

angular.module('tmcApp')
    .controller('CondominioController', function ($scope, Condominio, Principal) {

        Principal.identity(true).then(function (account) {
            $scope.settingsAccount = account;
        });

        $scope.condominios = [];

        $scope.loadAll = function() {
            Condominio.query(function(result) {
               $scope.condominios = result;
            });
        };

        $scope.loadAll();

        $scope.delete = function (id) {
            Condominio.get({id: id}, function(result) {
                $scope.condominio = result;
                $('#deleteCondominioConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            Condominio.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteCondominioConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.condominio = {
                id: null,
                razaoSocial: null,
                cnpj: null,
                disposicao: null,
                dataCadastro: null,
                ativo: null,

                /* Cep Condominio */
                condominioCepId: null,
                condominioCep: null,
                condominioLogradouro: null,
                condominioBairro: null,
                condominioCidade: null,
                condominioUf: null,
                condominioNumero: null,
                condominioComplemento: null,

                /* Dados do responsavel */
                responsavelNome: null,
                responsavelCpf: null,
                responsavelSexo: null,
                responsavelDataNascimento: null,
                responsavelEmail: null,
                responsavelSenha: null,

                /* Cep responsavel */
                responsavelCep: null,
                responsavelLogradouro: null,
                responsavelBairro: null,
                responsavelCidade: null,
                responsavelUf: null,
                responsavelNumero: null,
                responsavelComplemento: null
            };
        };
    });
