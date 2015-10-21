'use strict';

angular.module('tmcApp').controller('CondominioDialogController',
    ['$scope', '$http', '$stateParams', '$modalInstance', '$q', 'entity', 'Condominio', 'Cep', 'Funcionario', 'Dependencia', 'TelefoneCondominio',
        function($scope, $http, $stateParams, $modalInstance, $q, entity, Condominio, Cep, Funcionario, Dependencia, TelefoneCondominio) {

        $scope.condominio = entity;

        $scope.ceps = Cep.query({filter: 'condominio-is-null'});
        $q.all([$scope.condominio.$promise, $scope.ceps.$promise]).then(function() {
            if (!$scope.condominio.cep.id) {
                return $q.reject();
            }
            return Cep.get({id : $scope.condominio.cep.id}).$promise;
        }).then(function(cep) {
            $scope.ceps.push(cep);
        });

        $scope.funcionarios = Funcionario.query();
        $scope.dependencias = Dependencia.query();
        $scope.telefonecondominios = TelefoneCondominio.query();
        $scope.load = function(id) {
            Condominio.get({id : id}, function(result) {
                $scope.condominio = result;
            });
        };

        var onSaveFinished = function (result) {
            $scope.$emit('tmcApp:condominioUpdate', result);
            $modalInstance.close(result);
        };

        $scope.save = function () {
            if ($scope.condominio.id != null) {
                Condominio.update($scope.condominio, onSaveFinished);
            } else {
                Condominio.save($scope.condominio, $scope.cep, $scope.funcionario, onSaveFinished);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };

        $scope.buscaCep = function (object) {
            if (object != null) {
                var url = "http://cep.republicavirtual.com.br/web_cep.php?formato=json&cep=" + object.cep;
                var response = $http.get(url);
                response.success(function(resultado) {
                    console.log(resultado);
                    return false;
                });
            }
        };
}]);
