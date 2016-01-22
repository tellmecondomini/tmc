'use strict';

angular.module('tmcApp').controller('TopicoDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'Topico', 'Assunto', 'Comentario',
        function ($scope, $stateParams, $modalInstance, entity, Topico, Assunto, Comentario) {

            $scope.topico = entity;

            $scope.assuntos = Assunto.query();

            $scope.comentarios = Comentario.query();

            $scope.load = function (id) {
                Topico.get({id: id}, function (result) {
                    $scope.topico = result;
                });
            };

            var onSaveFinished = function (result) {
                $scope.$emit('tmcApp:topicoUpdate', result);
                $modalInstance.close(result);
            };

            $scope.save = function () {
                if ($scope.topico.id != null) {
                    Topico.update($scope.topico, onSaveFinished);
                } else {
                    Topico.save($scope.topico, onSaveFinished);
                }
            };

            $scope.clear = function () {
                $modalInstance.dismiss('cancel');
            };

            $scope.onSelectFirstCategoria = function () {
                var assunto = $scope.topico.assunto;
                if (assunto) {
                    if (assunto.categorias.length > 0)
                        $scope.topico.categoria = assunto.categorias[0];
                }
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

            $scope.byteSize = function () {
                var base64String = $scope.topico.imagem;
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

            $scope.setImagem = function ($file) {
                if ($file && $file.$error == 'pattern') {
                    return;
                }
                if ($file) {
                    var fileReader = new FileReader();
                    fileReader.readAsDataURL($file);
                    fileReader.onload = function (e) {
                        var data = e.target.result;
                        var base64Data = data.substr(data.indexOf('base64,') + 'base64,'.length);
                        $scope.$apply(function () {
                            $scope.topico.imagem = base64Data;
                        });
                    };
                }
            };

        }]);
