'use strict';

angular.module('tmcApp').controller('TopicoDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'Topico', 'Assunto', 'Comentario',
        function($scope, $stateParams, $modalInstance, entity, Topico, Assunto, Comentario) {

        $scope.topico = entity;
        $scope.assuntos = Assunto.query();
        $scope.comentarios = Comentario.query();
        $scope.load = function(id) {
            Topico.get({id : id}, function(result) {
                $scope.topico = result;
            });
        };

        var onSaveFinished = function (result) {
            $scope.$emit('tmcApp:topicoUpdate', result);
            $modalInstance.close(result);
        };

        $scope.save = function () {
            if ($scope.topico.id != null) {
                Topico.update($scope.topico, onSaveFinished);
            } else {
                Topico.save($scope.topico, onSaveFinished);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
