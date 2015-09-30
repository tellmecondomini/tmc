'use strict';

angular.module('tmcApp').controller('PrestadorServicoDialogController',
    ['$scope', '$stateParams', '$modalInstance', '$q', 'entity', 'PrestadorServico', 'Cep',
        function($scope, $stateParams, $modalInstance, $q, entity, PrestadorServico, Cep) {

        $scope.prestadorServico = entity;
        $scope.ceps = Cep.query({filter: 'prestadorservico-is-null'});
        $q.all([$scope.prestadorServico.$promise, $scope.ceps.$promise]).then(function() {
            if (!$scope.prestadorServico.cep.id) {
                return $q.reject();
            }
            return Cep.get({id : $scope.prestadorServico.cep.id}).$promise;
        }).then(function(cep) {
            $scope.ceps.push(cep);
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
