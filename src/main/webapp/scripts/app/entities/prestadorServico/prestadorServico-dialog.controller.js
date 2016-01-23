'use strict';

angular.module('tmcApp').controller('PrestadorServicoDialogController',
    ['$scope', '$stateParams', '$modalInstance', '$http', '$q', 'entity', 'PrestadorServico', 'Cep', 'TelefonePrestadorServico', 'CompetenciaPrestador',
        function ($scope, $stateParams, $modalInstance, $http, $q, entity, PrestadorServico, Cep, TelefonePrestadorServico, CompetenciaPrestador) {

            $scope.prestadorServico = entity;
            $scope.ceps = Cep.query({filter: 'prestadorservico-is-null'});
            $q.all([$scope.prestadorServico.$promise, $scope.ceps.$promise]).then(function () {
                if (!$scope.prestadorServico.cep.id) {
                    return $q.reject();
                }
                return Cep.get({id: $scope.prestadorServico.cep.id}).$promise;
            }).then(function (cep) {
                $scope.ceps.push(cep);
            });

            $scope.telefoneprestadorservicos = TelefonePrestadorServico.query();
            $scope.competenciaprestadors = CompetenciaPrestador.query({filter: 'prestadorservico-is-null'});
            $q.all([$scope.prestadorServico.$promise, $scope.competenciaprestadors.$promise]).then(function () {
                if (!$scope.prestadorServico.competenciaPrestador.id) {
                    return $q.reject();
                }
                return CompetenciaPrestador.get({id: $scope.prestadorServico.competenciaPrestador.id}).$promise;
            }).then(function (competenciaPrestador) {
                $scope.competenciaprestadors.push(competenciaPrestador);
            });

            $scope.load = function (id) {
                PrestadorServico.get({id: id}, function (result) {
                    $scope.prestadorServico = result;
                });
            };

            var onSaveFinished = function (result) {
                $scope.$emit('tmcApp:prestadorServicoUpdate', result);
                $modalInstance.close(result);
            };

            $scope.save = function () {
                if ($scope.prestadorServico.id != null) {
                    PrestadorServico.update($scope.prestadorServico, onSaveFinished);
                } else {
                    PrestadorServico.save($scope.prestadorServico, onSaveFinished);
                }
            };

            $scope.clear = function () {
                $modalInstance.dismiss('cancel');
            };

            $scope.buscaCep = function (cep) {
                if (cep != null) {
                    var url = "http://cep.republicavirtual.com.br/web_cep.php?formato=json&cep=" + cep;
                    var response = $http.get(url);
                    response.success(function (resultado) {
                        $scope.prestadorServico.cep.logradouro = resultado.logradouro;
                        $scope.prestadorServico.cep.bairro = resultado.bairro;
                        $scope.prestadorServico.cep.cidade = resultado.cidade;
                        $scope.prestadorServico.cep.uf = resultado.uf;
                        return false;
                    });
                }
            };

        }]);
