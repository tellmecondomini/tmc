'use strict';

angular.module('tmcApp').controller('OcorrenciaDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'Ocorrencia',
        function($scope, $stateParams, $modalInstance, entity, Ocorrencia) {

        $scope.ocorrencia = entity;
        $scope.load = function(id) {
            Ocorrencia.get({id : id}, function(result) {
                $scope.ocorrencia = result;
            });
        };

        var onSaveFinished = function (result) {
            $scope.$emit('tmcApp:ocorrenciaUpdate', result);
            $modalInstance.close(result);
        };

        $scope.save = function () {
            if ($scope.ocorrencia.id != null) {
                Ocorrencia.update($scope.ocorrencia, onSaveFinished);
            } else {
                Ocorrencia.save($scope.ocorrencia, onSaveFinished);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
