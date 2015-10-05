'use strict';

angular.module('tmcApp').controller('OcorrenciaSubItemDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'OcorrenciaSubItem',
        function($scope, $stateParams, $modalInstance, entity, OcorrenciaSubItem) {

        $scope.ocorrenciaSubItem = entity;
        $scope.load = function(id) {
            OcorrenciaSubItem.get({id : id}, function(result) {
                $scope.ocorrenciaSubItem = result;
            });
        };

        var onSaveFinished = function (result) {
            $scope.$emit('tmcApp:ocorrenciaSubItemUpdate', result);
            $modalInstance.close(result);
        };

        $scope.save = function () {
            if ($scope.ocorrenciaSubItem.id != null) {
                OcorrenciaSubItem.update($scope.ocorrenciaSubItem, onSaveFinished);
            } else {
                OcorrenciaSubItem.save($scope.ocorrenciaSubItem, onSaveFinished);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
