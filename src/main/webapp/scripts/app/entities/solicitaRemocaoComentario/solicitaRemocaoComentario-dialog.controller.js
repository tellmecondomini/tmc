'use strict';

angular.module('tmcApp').controller('SolicitaRemocaoComentarioDialogController',
    ['$scope', '$stateParams', '$modalInstance', '$q', 'entity', 'SolicitaRemocaoComentario', 'Comentario', 'Morador',
        function($scope, $stateParams, $modalInstance, $q, entity, SolicitaRemocaoComentario, Comentario, Morador) {

        $scope.solicitaRemocaoComentario = entity;
        $scope.comentarios = Comentario.query({filter: 'solicitaremocaocomentario-is-null'});
        $q.all([$scope.solicitaRemocaoComentario.$promise, $scope.comentarios.$promise]).then(function() {
            if (!$scope.solicitaRemocaoComentario.comentario.id) {
                return $q.reject();
            }
            return Comentario.get({id : $scope.solicitaRemocaoComentario.comentario.id}).$promise;
        }).then(function(comentario) {
            $scope.comentarios.push(comentario);
        });
        $scope.moradors = Morador.query();
        $scope.load = function(id) {
            SolicitaRemocaoComentario.get({id : id}, function(result) {
                $scope.solicitaRemocaoComentario = result;
            });
        };

        var onSaveFinished = function (result) {
            $scope.$emit('tmcApp:solicitaRemocaoComentarioUpdate', result);
            $modalInstance.close(result);
        };

        $scope.save = function () {
            if ($scope.solicitaRemocaoComentario.id != null) {
                SolicitaRemocaoComentario.update($scope.solicitaRemocaoComentario, onSaveFinished);
            } else {
                SolicitaRemocaoComentario.save($scope.solicitaRemocaoComentario, onSaveFinished);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
