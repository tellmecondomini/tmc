'use strict';

angular.module('tmcApp').controller('CondominioDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'Condominio', 'Endereco', 'Funcionario',
        function($scope, $stateParams, $modalInstance, entity, Condominio, Endereco, Funcionario) {

        $scope.condominio = entity;
        $scope.enderecos = Endereco.query();
        $scope.funcionarios = Funcionario.query();
        $scope.load = function(id) {
            Condominio.get({id : id}, function(result) {
                $scope.condominio = result;
            });
        };

        var onSaveFinished = function (result) {
            $scope.$emit('tmcApp:condominioUpdate', result);
            $modalInstance.close(result);
        };

        $scope.save = function () {
            if ($scope.condominio.id != null) {
                Condominio.update($scope.condominio, onSaveFinished);
            } else {
                Condominio.save($scope.condominio, onSaveFinished);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
