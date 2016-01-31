'use strict';

angular.module('tmcApp')
    .controller('TopicoController', function ($scope, $http, Topico, Principal, AccountFuncionario, AccountMorador) {

        Principal.identity().then(function (account) {
            $scope.account = account;
        });

        $scope.topicos = [];
        $scope.encerrados = [];
        $scope.loadAll = function () {
            Topico.query(function (result) {
                    angular.forEach(result, function (topico) {

                        function addTopicosFuncionario(lista) {
                            var index = 0;
                            var length = $scope.funcionario.categorias.length;
                            for (var i = 0; i < length; i++) {
                                if ($scope.funcionario.categorias[i].id === topico.categoria.id && topico.statusTopico != 'REPROVADO') {
                                    index = 1;
                                    break;
                                }
                            }
                            if (index > 0) {
                                lista.push(topico);
                            }
                        }

                        function addTopicosMorador(lista) {
                            if (topico.statusTopico != 'REPROVADO' && topico.statusTopico != 'AGUARDANDO_APROVACAO') {
                                lista.push(topico);
                            }
                        }

                        function addTopicosAdmin(lista) {
                            lista.push(topico);
                        }

                        if (['ROLE_FUNCIONARIO'].indexOf($scope.account.authorities[0]) > -1) {
                            if (['ENCERRADO_COM_SUCESSO', 'ENCERRADO_SEM_SUCESSO', 'REPROVADO'].indexOf(topico.statusTopico) > -1) {
                                addTopicosFuncionario($scope.encerrados);
                            } else {
                                addTopicosFuncionario($scope.topicos);
                            }
                        } else if ((['ROLE_MORADOR'].indexOf($scope.account.authorities[0]) > -1)) {
                            if (['ENCERRADO_COM_SUCESSO', 'ENCERRADO_SEM_SUCESSO', 'REPROVADO'].indexOf(topico.statusTopico) > -1) {
                                addTopicosMorador($scope.encerrados);
                            } else {
                                addTopicosMorador($scope.topicos);
                            }
                        } else {
                            if (['ENCERRADO_COM_SUCESSO', 'ENCERRADO_SEM_SUCESSO', 'REPROVADO'].indexOf(topico.statusTopico) > -1) {
                                addTopicosAdmin($scope.encerrados);
                            } else {
                                addTopicosAdmin($scope.topicos);
                            }
                        }
                    });
                }
            );
        };

        AccountFuncionario.get(function (funcionario) {
            if (funcionario.id != null)
                $scope.funcionario = funcionario;
            AccountMorador.get(function (morador) {
                if (morador.id != null)
                    $scope.morador = morador;
                $scope.loadAll();
            });
        });

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

    })
;
