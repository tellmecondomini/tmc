'use strict';

angular.module('tmcApp').controller('CepDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'Cep',
        function ($scope, $stateParams, $modalInstance, entity, Cep) {

            $scope.cep = entity;
            $scope.load = function (id) {
                Cep.get({id: id}, function (result) {
                    $scope.cep = result;
                });
            };

            var onSaveFinished = function (result) {
                $scope.$emit('tmcApp:cepUpdate', result);
                $modalInstance.close(result);
            };

            $scope.save = function () {
                if ($scope.cep.id != null) {
                    Cep.update($scope.cep, onSaveFinished);
                } else {
                    Cep.save($scope.cep, onSaveFinished);
                }
            };

            $scope.clear = function () {
                $modalInstance.dismiss('cancel');
            };
        }]);
