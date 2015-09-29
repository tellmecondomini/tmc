'use strict';

angular.module('tmcApp').controller('ImovelDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'Imovel', 'Morador',
        function($scope, $stateParams, $modalInstance, entity, Imovel, Morador) {

        $scope.imovel = entity;
        $scope.moradors = Morador.query();
        $scope.load = function(id) {
            Imovel.get({id : id}, function(result) {
                $scope.imovel = result;
            });
        };

        var onSaveFinished = function (result) {
            $scope.$emit('tmcApp:imovelUpdate', result);
            $modalInstance.close(result);
        };

        $scope.save = function () {
            if ($scope.imovel.id != null) {
                Imovel.update($scope.imovel, onSaveFinished);
            } else {
                Imovel.save($scope.imovel, onSaveFinished);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
