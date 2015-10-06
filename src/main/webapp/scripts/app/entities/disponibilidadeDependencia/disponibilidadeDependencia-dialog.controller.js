'use strict';

angular.module('tmcApp').controller('DisponibilidadeDependenciaDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'DisponibilidadeDependencia', 'Dependencia',
        function($scope, $stateParams, $modalInstance, entity, DisponibilidadeDependencia, Dependencia) {

        $scope.disponibilidadeDependencia = entity;
        $scope.dependencias = Dependencia.query();
        $scope.load = function(id) {
            DisponibilidadeDependencia.get({id : id}, function(result) {
                $scope.disponibilidadeDependencia = result;
            });
        };

        var onSaveFinished = function (result) {
            $scope.$emit('tmcApp:disponibilidadeDependenciaUpdate', result);
            $modalInstance.close(result);
        };

        $scope.save = function () {
            if ($scope.disponibilidadeDependencia.id != null) {
                DisponibilidadeDependencia.update($scope.disponibilidadeDependencia, onSaveFinished);
            } else {
                DisponibilidadeDependencia.save($scope.disponibilidadeDependencia, onSaveFinished);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
