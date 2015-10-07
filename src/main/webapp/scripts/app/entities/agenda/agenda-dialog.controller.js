'use strict';

angular.module('tmcApp').controller('AgendaDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'Agenda', 'Morador', 'Dependencia', 'Convidado',
        function($scope, $stateParams, $modalInstance, entity, Agenda, Morador, Dependencia, Convidado) {

        $scope.agenda = entity;
        $scope.moradors = Morador.query();
        $scope.dependencias = Dependencia.query();
        $scope.convidados = Convidado.query();
        $scope.load = function(id) {
            Agenda.get({id : id}, function(result) {
                $scope.agenda = result;
            });
        };

        var onSaveFinished = function (result) {
            $scope.$emit('tmcApp:agendaUpdate', result);
            $modalInstance.close(result);
        };

        $scope.save = function () {
            if ($scope.agenda.id != null) {
                Agenda.update($scope.agenda, onSaveFinished);
            } else {
                Agenda.save($scope.agenda, onSaveFinished);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
