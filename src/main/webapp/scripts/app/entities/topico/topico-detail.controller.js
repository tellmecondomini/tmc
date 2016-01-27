'use strict';

angular.module('tmcApp')
    .controller('TopicoDetailController', function ($scope, $rootScope, $stateParams, entity, Topico, Assunto, Comentario) {
        $scope.topico = entity;
        $scope.load = function (id) {
            Topico.get({id: id}, function (result) {
                $scope.topico = result;
            });
        };
        $rootScope.$on('tmcApp:topicoUpdate', function (event, result) {
            $scope.topico = result;
        });
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
