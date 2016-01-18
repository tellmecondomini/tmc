'use strict';

angular.module('tmcApp')
    .controller('ImagemTopicoController', function ($scope, ImagemTopico) {
        $scope.imagemTopicos = [];
        $scope.loadAll = function() {
            ImagemTopico.query(function(result) {
               $scope.imagemTopicos = result;
            });
        };
        $scope.loadAll();

        $scope.delete = function (id) {
            ImagemTopico.get({id: id}, function(result) {
                $scope.imagemTopico = result;
                $('#deleteImagemTopicoConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            ImagemTopico.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteImagemTopicoConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.imagemTopico = {imagem: null, id: null};
        };

        $scope.abbreviate = function (text) {
            if (!angular.isString(text)) {
                return '';
            }
            if (text.length < 30) {
                return text;
            }
            return text ? (text.substring(0, 15) + '...' + text.slice(-10)) : '';
        };

        $scope.byteSize = function (base64String) {
            if (!angular.isString(base64String)) {
                return '';
            }
            function endsWith(suffix, str) {
                return str.indexOf(suffix, str.length - suffix.length) !== -1;
            }
            function paddingSize(base64String) {
                if (endsWith('==', base64String)) {
                    return 2;
                }
                if (endsWith('=', base64String)) {
                    return 1;
                }
                return 0;
            }
            function size(base64String) {
                return base64String.length / 4 * 3 - paddingSize(base64String);
            }
            function formatAsBytes(size) {
                return size.toString().replace(/\B(?=(\d{3})+(?!\d))/g, " ") + " bytes";
            }

            return formatAsBytes(size(base64String));
        };
    });
