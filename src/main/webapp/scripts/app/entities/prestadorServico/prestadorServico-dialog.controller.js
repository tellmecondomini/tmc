'use strict';

angular.module('tmcApp').controller('PrestadorServicoDialogController',
    ['$scope', '$stateParams', '$modalInstance', '$q', 'entity', 'PrestadorServico', 'Endereco',
        function($scope, $stateParams, $modalInstance, $q, entity, PrestadorServico, Endereco) {

        $scope.prestadorServico = entity;
        $scope.enderecos = Endereco.query({filter: 'prestadorservico-is-null'});
        $q.all([$scope.prestadorServico.$promise, $scope.enderecos.$promise]).then(function() {
            if (!$scope.prestadorServico.endereco.id) {
                return $q.reject();
            }
            return Endereco.get({id : $scope.prestadorServico.endereco.id}).$promise;
        }).then(function(endereco) {
            $scope.enderecos.push(endereco);
        });
        $scope.load = function(id) {
            PrestadorServico.get({id : id}, function(result) {
                $scope.prestadorServico = result;
            });
        };

        var onSaveFinished = function (result) {
            $scope.$emit('tmcApp:prestadorServicoUpdate', result);
            $modalInstance.close(result);
        };

        $scope.save = function () {
            if ($scope.prestadorServico.id != null) {
                PrestadorServico.update($scope.prestadorServico, onSaveFinished);
            } else {
                PrestadorServico.save($scope.prestadorServico, onSaveFinished);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
