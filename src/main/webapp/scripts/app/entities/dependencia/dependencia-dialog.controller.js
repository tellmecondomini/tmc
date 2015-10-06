'use strict';

angular.module('tmcApp').controller('DependenciaDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'Dependencia', 'DisponibilidadeDependencia',
        function($scope, $stateParams, $modalInstance, entity, Dependencia, DisponibilidadeDependencia) {

        $scope.dependencia = entity;
        $scope.disponibilidadedependencias = DisponibilidadeDependencia.query();
        $scope.load = function(id) {
            Dependencia.get({id : id}, function(result) {
                $scope.dependencia = result;
            });
        };

        var onSaveFinished = function (result) {
            $scope.$emit('tmcApp:dependenciaUpdate', result);
            $modalInstance.close(result);
        };

        $scope.save = function () {
            if ($scope.dependencia.id != null) {
                Dependencia.update($scope.dependencia, onSaveFinished);
            } else {
                Dependencia.save($scope.dependencia, onSaveFinished);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
