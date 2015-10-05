'use strict';

angular.module('tmcApp').controller('OcorrenciaItemDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'OcorrenciaItem',
        function($scope, $stateParams, $modalInstance, entity, OcorrenciaItem) {

        $scope.ocorrenciaItem = entity;
        $scope.load = function(id) {
            OcorrenciaItem.get({id : id}, function(result) {
                $scope.ocorrenciaItem = result;
            });
        };

        var onSaveFinished = function (result) {
            $scope.$emit('tmcApp:ocorrenciaItemUpdate', result);
            $modalInstance.close(result);
        };

        $scope.save = function () {
            if ($scope.ocorrenciaItem.id != null) {
                OcorrenciaItem.update($scope.ocorrenciaItem, onSaveFinished);
            } else {
                OcorrenciaItem.save($scope.ocorrenciaItem, onSaveFinished);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
