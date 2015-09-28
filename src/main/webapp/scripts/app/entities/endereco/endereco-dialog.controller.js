'use strict';

angular.module('tmcApp').controller('EnderecoDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'Endereco',
        function($scope, $stateParams, $modalInstance, entity, Endereco) {

        $scope.endereco = entity;
        $scope.load = function(id) {
            Endereco.get({id : id}, function(result) {
                $scope.endereco = result;
            });
        };

        var onSaveFinished = function (result) {
            $scope.$emit('tmcApp:enderecoUpdate', result);
            $modalInstance.close(result);
        };

        $scope.save = function () {
            if ($scope.endereco.id != null) {
                Endereco.update($scope.endereco, onSaveFinished);
            } else {
                Endereco.save($scope.endereco, onSaveFinished);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
