'use strict';

angular.module('tmcApp').controller('AssuntoDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'Assunto', 'Topico',
        function($scope, $stateParams, $modalInstance, entity, Assunto, Topico) {

        $scope.assunto = entity;
        $scope.topicos = Topico.query();
        $scope.load = function(id) {
            Assunto.get({id : id}, function(result) {
                $scope.assunto = result;
            });
        };

        var onSaveFinished = function (result) {
            $scope.$emit('tmcApp:assuntoUpdate', result);
            $modalInstance.close(result);
        };

        $scope.save = function () {
            if ($scope.assunto.id != null) {
                Assunto.update($scope.assunto, onSaveFinished);
            } else {
                Assunto.save($scope.assunto, onSaveFinished);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
