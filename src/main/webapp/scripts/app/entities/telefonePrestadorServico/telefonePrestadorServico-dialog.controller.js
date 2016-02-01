'use strict';

angular.module('tmcApp').controller('TelefonePrestadorServicoDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'TelefonePrestadorServico', 'PrestadorServico',
        function ($scope, $stateParams, $modalInstance, entity, TelefonePrestadorServico, PrestadorServico) {

            $scope.telefonePrestadorServico = entity;
            $scope.prestadorservicos = PrestadorServico.query();
            $scope.load = function (id) {
                TelefonePrestadorServico.get({id: id}, function (result) {
                    $scope.telefonePrestadorServico = result;
                });
            };

            var onSaveFinished = function (result) {
                $scope.$emit('tmcApp:telefonePrestadorServicoUpdate', result);
                $modalInstance.close(result);
            };

            $scope.save = function () {
                if ($scope.telefonePrestadorServico.id != null) {
                    TelefonePrestadorServico.update($scope.telefonePrestadorServico, onSaveFinished);
                } else {
                    TelefonePrestadorServico.save($scope.telefonePrestadorServico, onSaveFinished);
                }
            };

            $scope.clear = function () {
                $modalInstance.dismiss('cancel');
            };
        }]);
