'use strict';

angular.module('tmcApp').controller('FuncionarioDialogController',
    ['$scope', '$stateParams', '$modalInstance', '$q', 'entity', 'Funcionario', 'Cep', 'Condominio',
        function($scope, $stateParams, $modalInstance, $q, entity, Funcionario, Cep, Condominio) {

        $scope.funcionario = entity;
        $scope.ceps = Cep.query({filter: 'funcionario-is-null'});
        $q.all([$scope.funcionario.$promise, $scope.ceps.$promise]).then(function() {
            if (!$scope.funcionario.cep.id) {
                return $q.reject();
            }
            return Cep.get({id : $scope.funcionario.cep.id}).$promise;
        }).then(function(cep) {
            $scope.ceps.push(cep);
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