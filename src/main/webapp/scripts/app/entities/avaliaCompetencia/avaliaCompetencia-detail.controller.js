'use strict';

angular.module('tmcApp')
    .controller('AvaliaCompetenciaDetailController', function ($scope, $rootScope, $stateParams, list) {
        $scope.avaliacoes = list;
    });
