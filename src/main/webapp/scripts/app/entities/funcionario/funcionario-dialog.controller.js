'use strict';

angular.module('tmcApp').controller('FuncionarioDialogController',
    ['$scope', '$stateParams', '$modalInstance', '$q', 'entity', 'Funcionario', 'Endereco', 'Condominio',
        function($scope, $stateParams, $modalInstance, $q, entity, Funcionario, Endereco, Condominio) {

        $scope.funcionario = entity;
        $scope.enderecos = Endereco.query({filter: 'funcionario-is-null'});
        $q.all([$scope.funcionario.$promise, $scope.enderecos.$promise]).then(function() {
            if (!$scope.funcionario.endereco.id) {
                return $q.reject();
            }
            return Endereco.get({id : $scope.funcionario.endereco.id}).$promise;
        }).then(function(endereco) {
            $scope.enderecos.push(endereco);
        });
        $scope.condominios = Condominio.query();
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
