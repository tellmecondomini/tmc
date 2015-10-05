'use strict';

angular.module('tmcApp').controller('OcorrenciaPrioridadeDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'OcorrenciaPrioridade',
        function($scope, $stateParams, $modalInstance, entity, OcorrenciaPrioridade) {

        $scope.ocorrenciaPrioridade = entity;
        $scope.load = function(id) {
            OcorrenciaPrioridade.get({id : id}, function(result) {
                $scope.ocorrenciaPrioridade = result;
            });
        };

        var onSaveFinished = function (result) {
            $scope.$emit('tmcApp:ocorrenciaPrioridadeUpdate', result);
            $modalInstance.close(result);
        };

        $scope.save = function () {
            if ($scope.ocorrenciaPrioridade.id != null) {
                OcorrenciaPrioridade.update($scope.ocorrenciaPrioridade, onSaveFinished);
            } else {
                OcorrenciaPrioridade.save($scope.ocorrenciaPrioridade, onSaveFinished);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
