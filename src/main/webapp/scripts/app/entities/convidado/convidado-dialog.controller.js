'use strict';

angular.module('tmcApp').controller('ConvidadoDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'Convidado', 'Agenda', 'Dependente',
        function($scope, $stateParams, $modalInstance, entity, Convidado, Agenda, Dependente) {

        $scope.convidado = entity;
        $scope.agendas = Agenda.query();
        $scope.dependentes = Dependente.query();
        $scope.load = function(id) {
            Convidado.get({id : id}, function(result) {
                $scope.convidado = result;
            });
        };

        var onSaveFinished = function (result) {
            $scope.$emit('tmcApp:convidadoUpdate', result);
            $modalInstance.close(result);
        };

        $scope.save = function () {
            if ($scope.convidado.id != null) {
                Convidado.update($scope.convidado, onSaveFinished);
            } else {
                Convidado.save($scope.convidado, onSaveFinished);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
