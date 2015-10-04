'use strict';

angular.module('tmcApp')
    .controller('MoradorController', function ($scope, Morador) {
        $scope.moradors = [];
        $scope.loadAll = function() {
            Morador.query(function(result) {
               $scope.moradors = result;
            });
        };
        $scope.loadAll();

        $scope.delete = function (id) {
            Morador.get({id: id}, function(result) {
                $scope.morador = result;
                $('#deleteMoradorConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            Morador.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteMoradorConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.morador = {nome: null, cpf: null, sexo: null, email: null, senha: null, dataNascimento: null, ativo: null, bloqueiaAgendamento: null, tipo: null, id: null};
        };
    });
