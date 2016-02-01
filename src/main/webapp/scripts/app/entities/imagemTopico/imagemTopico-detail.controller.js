'use strict';

angular.module('tmcApp')
    .controller('ImagemTopicoDetailController', function ($scope, $rootScope, $stateParams, entity, ImagemTopico, Topico) {
        $scope.imagemTopico = entity;
        $scope.load = function (id) {
            ImagemTopico.get({id: id}, function (result) {
                $scope.imagemTopico = result;
            });
        };
        $rootScope.$on('tmcApp:imagemTopicoUpdate', function (event, result) {
            $scope.imagemTopico = result;
        });
    });
