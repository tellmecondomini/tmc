'use strict';

angular.module('tmcApp')
    .controller('TopicosCategoriaStatusController', function ($scope, $http, entity) {

        $scope.reportObj = entity;

        $scope.print = function () {
            $http.post('api/reports/topicos/categoria/status', $scope.reportObj).then(
                function (result) {
                    window.open(result.data.url, '_blank');
                    return false;
                }, function (error) {
                    console.log(error);
                });
        };

    });
