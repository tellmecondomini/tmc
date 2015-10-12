'use strict';

angular.module('tmcApp')
    .controller('AssuntoDetailController', function ($scope, $rootScope, $stateParams, entity, Assunto, Topico) {
        $scope.assunto = entity;
        $scope.load = function (id) {
            Assunto.get({id: id}, function(result) {
                $scope.assunto = result;
            });
        };
        $rootScope.$on('tmcApp:assuntoUpdate', function(event, result) {
            $scope.assunto = result;
        });
    });
