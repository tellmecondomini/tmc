'use strict';

angular.module('tmcApp')
    .controller('TopicoDetailController', function ($scope, $rootScope, $stateParams, entity, Topico, Assunto, Comentario) {
        $scope.topico = entity;
        $scope.load = function (id) {
            Topico.get({id: id}, function(result) {
                $scope.topico = result;
            });
        };
        $rootScope.$on('tmcApp:topicoUpdate', function(event, result) {
            $scope.topico = result;
        });
    });
