'use strict';

angular.module('tmcApp').controller('CategoriaDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'Categoria', 'Funcionario',
        function($scope, $stateParams, $modalInstance, entity, Categoria, Funcionario) {

        $scope.categoria = entity;
        $scope.funcionarios = Funcionario.query();
        $scope.load = function(id) {
            Categoria.get({id : id}, function(result) {
                $scope.categoria = result;
            });
        };

        var onSaveFinished = function (result) {
            $scope.$emit('tmcApp:categoriaUpdate', result);
            $modalInstance.close(result);
        };

        $scope.save = function () {
            if ($scope.categoria.id != null) {
                Categoria.update($scope.categoria, onSaveFinished);
            } else {
                Categoria.save($scope.categoria, onSaveFinished);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
