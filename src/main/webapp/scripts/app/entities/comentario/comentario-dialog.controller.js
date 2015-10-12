'use strict';

angular.module('tmcApp').controller('ComentarioDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'Comentario', 'Topico', 'Morador', 'Funcionario',
        function($scope, $stateParams, $modalInstance, entity, Comentario, Topico, Morador, Funcionario) {

        $scope.comentario = entity;
        $scope.topicos = Topico.query();
        $scope.moradors = Morador.query();
        $scope.funcionarios = Funcionario.query();
        $scope.load = function(id) {
            Comentario.get({id : id}, function(result) {
                $scope.comentario = result;
            });
        };

        var onSaveFinished = function (result) {
            $scope.$emit('tmcApp:comentarioUpdate', result);
            $modalInstance.close(result);
        };

        $scope.save = function () {
            if ($scope.comentario.id != null) {
                Comentario.update($scope.comentario, onSaveFinished);
            } else {
                Comentario.save($scope.comentario, onSaveFinished);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
