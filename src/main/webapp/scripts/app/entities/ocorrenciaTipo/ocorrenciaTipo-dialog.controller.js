'use strict';

angular.module('tmcApp').controller('OcorrenciaTipoDialogController',
    ['$scope', '$stateParams', '$modalInstance', '$q', 'entity', 'OcorrenciaTipo', 'OcorrenciaItem', 'OcorrenciaSubItem', 'OcorrenciaPrioridade',
        function($scope, $stateParams, $modalInstance, $q, entity, OcorrenciaTipo, OcorrenciaItem, OcorrenciaSubItem, OcorrenciaPrioridade) {

        $scope.ocorrenciaTipo = entity;
        $scope.ocorrenciaitems = OcorrenciaItem.query({filter: 'ocorrenciatipo-is-null'});
        $q.all([$scope.ocorrenciaTipo.$promise, $scope.ocorrenciaitems.$promise]).then(function() {
            if (!$scope.ocorrenciaTipo.ocorrenciaItem.id) {
                return $q.reject();
            }
            return OcorrenciaItem.get({id : $scope.ocorrenciaTipo.ocorrenciaItem.id}).$promise;
        }).then(function(ocorrenciaItem) {
            $scope.ocorrenciaitems.push(ocorrenciaItem);
        });
        $scope.ocorrenciasubitems = OcorrenciaSubItem.query({filter: 'ocorrenciatipo-is-null'});
        $q.all([$scope.ocorrenciaTipo.$promise, $scope.ocorrenciasubitems.$promise]).then(function() {
            if (!$scope.ocorrenciaTipo.ocorrenciaSubItem.id) {
                return $q.reject();
            }
            return OcorrenciaSubItem.get({id : $scope.ocorrenciaTipo.ocorrenciaSubItem.id}).$promise;
        }).then(function(ocorrenciaSubItem) {
            $scope.ocorrenciasubitems.push(ocorrenciaSubItem);
        });
        $scope.ocorrenciaprioridades = OcorrenciaPrioridade.query({filter: 'ocorrenciatipo-is-null'});
        $q.all([$scope.ocorrenciaTipo.$promise, $scope.ocorrenciaprioridades.$promise]).then(function() {
            if (!$scope.ocorrenciaTipo.ocorrenciaPrioridade.id) {
                return $q.reject();
            }
            return OcorrenciaPrioridade.get({id : $scope.ocorrenciaTipo.ocorrenciaPrioridade.id}).$promise;
        }).then(function(ocorrenciaPrioridade) {
            $scope.ocorrenciaprioridades.push(ocorrenciaPrioridade);
        });
        $scope.load = function(id) {
            OcorrenciaTipo.get({id : id}, function(result) {
                $scope.ocorrenciaTipo = result;
            });
        };

        var onSaveFinished = function (result) {
            $scope.$emit('tmcApp:ocorrenciaTipoUpdate', result);
            $modalInstance.close(result);
        };

        $scope.save = function () {
            if ($scope.ocorrenciaTipo.id != null) {
                OcorrenciaTipo.update($scope.ocorrenciaTipo, onSaveFinished);
            } else {
                OcorrenciaTipo.save($scope.ocorrenciaTipo, onSaveFinished);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
