'use strict';

angular.module('tmcApp').controller('FuncionarioDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'Funcionario', 'Condominio', 'Endereco',
        function($scope, $stateParams, $modalInstance, entity, Funcionario, Condominio, Endereco) {

        $scope.funcionario = entity;
        $scope.condominios = Condominio.query();
        $scope.enderecos = Endereco.query();
        $scope.load = function(id) {
            Funcionario.get({id : id}, function(result) {
                $scope.funcionario = result;
            });
        };

        var onSaveFinished = function (result) {
            $scope.$emit('tmcApp:funcionarioUpdate', result);
            $modalInstance.close(result);
        };

        $scope.save = function () {
            if ($scope.funcionario.id != null) {
                Funcionario.update($scope.funcionario, onSaveFinished);
            } else {
                Funcionario.save($scope.funcionario, onSaveFinished);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
