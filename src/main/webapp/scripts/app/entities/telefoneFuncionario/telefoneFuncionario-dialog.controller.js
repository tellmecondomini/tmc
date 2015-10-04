'use strict';

angular.module('tmcApp').controller('TelefoneFuncionarioDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'TelefoneFuncionario', 'Funcionario',
        function($scope, $stateParams, $modalInstance, entity, TelefoneFuncionario, Funcionario) {

        $scope.telefoneFuncionario = entity;
        $scope.funcionarios = Funcionario.query();
        $scope.load = function(id) {
            TelefoneFuncionario.get({id : id}, function(result) {
                $scope.telefoneFuncionario = result;
            });
        };

        var onSaveFinished = function (result) {
            $scope.$emit('tmcApp:telefoneFuncionarioUpdate', result);
            $modalInstance.close(result);
        };

        $scope.save = function () {
            if ($scope.telefoneFuncionario.id != null) {
                TelefoneFuncionario.update($scope.telefoneFuncionario, onSaveFinished);
            } else {
                TelefoneFuncionario.save($scope.telefoneFuncionario, onSaveFinished);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
