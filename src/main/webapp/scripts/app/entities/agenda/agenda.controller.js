'use strict';

angular.module('tmcApp')
    .controller('AgendaController', function ($scope, Agenda) {
        $scope.agendas = [];
        $scope.loadAll = function() {
            Agenda.query(function(result) {
               $scope.agendas = result;
            });
        };
        $scope.loadAll();

        $scope.delete = function (id) {
            Agenda.get({id: id}, function(result) {
                $scope.agenda = result;
                $('#deleteAgendaConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            Agenda.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteAgendaConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.agenda = {data: null, horaInicio: null, horaFim: null, id: null};
        };
    });
