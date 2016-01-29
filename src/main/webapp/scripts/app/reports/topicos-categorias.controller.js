'use strict';

angular.module('tmcApp')
    .controller('TopicosCategoriasController', function ($scope, $http, entity) {

        $scope.reportObj = entity;

        $scope.print = function () {
            $http.post('api/reports/topicos/categorias', $scope.reportObj).then(
                function (result) {
                    window.open(result.data.url, '_blank');
                    return false;
                }, function (error) {
                    console.log(error);
                });
        };

    });
