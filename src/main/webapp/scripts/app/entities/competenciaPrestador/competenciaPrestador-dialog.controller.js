'use strict';

angular.module('tmcApp').controller('CompetenciaPrestadorDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'CompetenciaPrestador',
        function ($scope, $stateParams, $modalInstance, entity, CompetenciaPrestador) {

            $scope.competenciaPrestador = entity;
            $scope.load = function (id) {
                CompetenciaPrestador.get({id: id}, function (result) {
                    $scope.competenciaPrestador = result;
                });
            };

            var onSaveFinished = function (result) {
                $scope.$emit('tmcApp:competenciaPrestadorUpdate', result);
                $modalInstance.close(result);
            };

            $scope.save = function () {
                if ($scope.competenciaPrestador.id != null) {
                    CompetenciaPrestador.update($scope.competenciaPrestador, onSaveFinished);
                } else {
                    CompetenciaPrestador.save($scope.competenciaPrestador, onSaveFinished);
                }
            };

            $scope.clear = function () {
                $modalInstance.dismiss('cancel');
            };
        }]);
