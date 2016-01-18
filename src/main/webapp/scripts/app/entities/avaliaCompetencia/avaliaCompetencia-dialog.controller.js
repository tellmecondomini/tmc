'use strict';

angular.module('tmcApp').controller('AvaliaCompetenciaDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'AvaliaCompetencia', 'Morador', 'PrestadorServico', 'CompetenciaPrestador',
        function($scope, $stateParams, $modalInstance, entity, AvaliaCompetencia, Morador, PrestadorServico, CompetenciaPrestador) {

        $scope.avaliaCompetencia = entity;
        $scope.moradors = Morador.query();
        $scope.prestadorservicos = PrestadorServico.query();
        $scope.competenciaprestadors = CompetenciaPrestador.query();
        $scope.load = function(id) {
            AvaliaCompetencia.get({id : id}, function(result) {
                $scope.avaliaCompetencia = result;
            });
        };

        var onSaveFinished = function (result) {
            $scope.$emit('tmcApp:avaliaCompetenciaUpdate', result);
            $modalInstance.close(result);
        };

        $scope.save = function () {
            if ($scope.avaliaCompetencia.id != null) {
                AvaliaCompetencia.update($scope.avaliaCompetencia, onSaveFinished);
            } else {
                AvaliaCompetencia.save($scope.avaliaCompetencia, onSaveFinished);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
