'use strict';

angular.module('tmcApp')
    .controller('AgendaDetailController', function ($scope, $rootScope, $stateParams, entity, Agenda, Morador, Dependencia, Convidado) {
        $scope.agenda = entity;
        $scope.load = function (id) {
            Agenda.get({id: id}, function(result) {
                $scope.agenda = result;
            });
        };
        $rootScope.$on('tmcApp:agendaUpdate', function(event, result) {
            $scope.agenda = result;
        });
    });
