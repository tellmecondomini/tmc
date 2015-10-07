'use strict';

angular.module('tmcApp').controller('DependenteDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'Dependente', 'Convidado',
        function($scope, $stateParams, $modalInstance, entity, Dependente, Convidado) {

        $scope.dependente = entity;
        $scope.convidados = Convidado.query();
        $scope.load = function(id) {
            Dependente.get({id : id}, function(result) {
                $scope.dependente = result;
            });
        };

        var onSaveFinished = function (result) {
            $scope.$emit('tmcApp:dependenteUpdate', result);
            $modalInstance.close(result);
        };

        $scope.save = function () {
            if ($scope.dependente.id != null) {
                Dependente.update($scope.dependente, onSaveFinished);
            } else {
                Dependente.save($scope.dependente, onSaveFinished);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
