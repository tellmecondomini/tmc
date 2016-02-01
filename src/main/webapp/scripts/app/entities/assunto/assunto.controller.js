'use strict';

angular.module('tmcApp')
    .controller('AssuntoController', function ($scope, Assunto) {

        $scope.assuntos = [];
        $scope.loadAll = function () {
            Assunto.query(function (result) {
                $scope.assuntos = result;
            });
        };
        $scope.loadAll();

        $scope.delete = function (id) {
            Assunto.get({id: id}, function (result) {
                $scope.assunto = result;
                $('#deleteAssuntoConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            Assunto.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteAssuntoConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.assunto = {descricao: null, id: null};
        };
    });
