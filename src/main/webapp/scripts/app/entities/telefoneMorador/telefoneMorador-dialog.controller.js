'use strict';

angular.module('tmcApp').controller('TelefoneMoradorDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'TelefoneMorador', 'Morador',
        function ($scope, $stateParams, $modalInstance, entity, TelefoneMorador, Morador) {

            $scope.telefoneMorador = entity;
            $scope.moradors = Morador.query();
            $scope.load = function (id) {
                TelefoneMorador.get({id: id}, function (result) {
                    $scope.telefoneMorador = result;
                });
            };

            var onSaveFinished = function (result) {
                $scope.$emit('tmcApp:telefoneMoradorUpdate', result);
                $modalInstance.close(result);
            };

            $scope.save = function () {
                if ($scope.telefoneMorador.id != null) {
                    TelefoneMorador.update($scope.telefoneMorador, onSaveFinished);
                } else {
                    TelefoneMorador.save($scope.telefoneMorador, onSaveFinished);
                }
            };

            $scope.clear = function () {
                $modalInstance.dismiss('cancel');
            };
        }]);
