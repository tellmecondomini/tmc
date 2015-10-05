'use strict';

angular.module('tmcApp').controller('OcorrenciaDialogController',
    ['$scope', '$stateParams', '$modalInstance', '$q', 'entity', 'Ocorrencia', 'OcorrenciaTipo',
        function($scope, $stateParams, $modalInstance, $q, entity, Ocorrencia, OcorrenciaTipo) {

        $scope.ocorrencia = entity;
        $scope.ocorrenciatipos = OcorrenciaTipo.query({filter: 'ocorrenciatipo-is-null'});
        $q.all([$scope.ocorrenciaTipo.$promise, $scope.ocorrenciatipos.$promise]).then(function() {
            if (!$scope.ocorrenciaTipo.ocorrenciaTipo.id) {
                return $q.reject();
            }
            return OcorrenciaTipo.get({id : $scope.ocorrenciaTipo.ocorrenciaTipo.id}).$promise;
        }).then(function(ocorrenciaTipo) {
            $scope.ocorrenciatipos.push(ocorrenciaTipo);
        });
        $scope.load = function(id) {
            Ocorrencia.get({id : id}, function(result) {
                $scope.ocorrencia = result;
            });
        };

        var onSaveFinished = function (result) {
            $scope.$emit('tmcApp:ocorrenciaUpdate', result);
            $modalInstance.close(result);
        };

        $scope.save = function () {
            if ($scope.ocorrencia.id != null) {
                Ocorrencia.update($scope.ocorrencia, onSaveFinished);
            } else {
                Ocorrencia.save($scope.ocorrencia, onSaveFinished);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
