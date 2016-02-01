'use strict';

angular.module('tmcApp').controller('MoradorDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'Morador',
        function ($scope, $stateParams, $modalInstance, entity, Morador) {

            $scope.morador = entity;

            var onSaveFinished = function (result) {
                $scope.$emit('tmcApp:moradorUpdate', result);
                $modalInstance.close(result);
            };

            $scope.save = function () {
                if ($scope.morador.id != null) {
                    Morador.update($scope.morador, onSaveFinished);
                } else {
                    Morador.save($scope.morador, onSaveFinished);
                }
            };

            $scope.clear = function () {
                $modalInstance.dismiss('cancel');
            };
        }]);
