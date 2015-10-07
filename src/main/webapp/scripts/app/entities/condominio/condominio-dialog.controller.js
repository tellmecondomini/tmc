'use strict';

angular.module('tmcApp').controller('CondominioDialogController',
    ['$scope', '$stateParams', '$modalInstance', '$q', 'entity', 'Condominio', 'Cep', 'Funcionario', 'Dependencia',
        function($scope, $stateParams, $modalInstance, $q, entity, Condominio, Cep, Funcionario, Dependencia) {

        $scope.condominio = entity;
        $scope.ceps = Cep.query({filter: 'condominio-is-null'});
        $q.all([$scope.condominio.$promise, $scope.ceps.$promise]).then(function() {
            if (!$scope.condominio.cep.id) {
                return $q.reject();
            }
            return Cep.get({id : $scope.condominio.cep.id}).$promise;
        }).then(function(cep) {
            $scope.ceps.push(cep);
        });
        $scope.funcionarios = Funcionario.query();
        $scope.dependencias = Dependencia.query();
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
