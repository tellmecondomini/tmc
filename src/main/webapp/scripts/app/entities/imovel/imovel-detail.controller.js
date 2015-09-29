'use strict';

angular.module('tmcApp')
    .controller('ImovelDetailController', function ($scope, $rootScope, $stateParams, entity, Imovel, Morador) {
        $scope.imovel = entity;
        $scope.load = function (id) {
            Imovel.get({id: id}, function(result) {
                $scope.imovel = result;
            });
        };
        $rootScope.$on('tmcApp:imovelUpdate', function(event, result) {
            $scope.imovel = result;
        });
    });
