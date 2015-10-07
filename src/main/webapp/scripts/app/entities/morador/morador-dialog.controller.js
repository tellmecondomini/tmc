'use strict';

angular.module('tmcApp').controller('MoradorDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'Morador', 'Imovel', 'TelefoneMorador', 'Ocorrencia', 'Agenda',
        function($scope, $stateParams, $modalInstance, entity, Morador, Imovel, TelefoneMorador, Ocorrencia, Agenda) {

        $scope.morador = entity;
        $scope.imovels = Imovel.query();
        $scope.telefonemoradors = TelefoneMorador.query();
        $scope.ocorrencias = Ocorrencia.query();
        $scope.agendas = Agenda.query();
        $scope.load = function(id) {
            Morador.get({id : id}, function(result) {
                $scope.morador = result;
            });
        };

        var onSaveFinished = function (result) {
            $scope.$emit('tmcApp:moradorUpdate', result);
            $modalInstance.close(result);
        };

        $scope.save = function () {
            if ($scope.morador.id != null) {
                Morador.update($scope.morador, onSaveFinished);
            } else {
                Morador.save($scope.morador, onSaveFinished);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
