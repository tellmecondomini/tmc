'use strict';

angular.module('tmcApp').controller('TelefoneCondominioDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'TelefoneCondominio', 'Condominio',
        function($scope, $stateParams, $modalInstance, entity, TelefoneCondominio, Condominio) {

        $scope.telefoneCondominio = entity;
        $scope.condominios = Condominio.query();
        $scope.load = function(id) {
            TelefoneCondominio.get({id : id}, function(result) {
                $scope.telefoneCondominio = result;
            });
        };

        var onSaveFinished = function (result) {
            $scope.$emit('tmcApp:telefoneCondominioUpdate', result);
            $modalInstance.close(result);
        };

        $scope.save = function () {
            if ($scope.telefoneCondominio.id != null) {
                TelefoneCondominio.update($scope.telefoneCondominio, onSaveFinished);
            } else {
                TelefoneCondominio.save($scope.telefoneCondominio, onSaveFinished);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
