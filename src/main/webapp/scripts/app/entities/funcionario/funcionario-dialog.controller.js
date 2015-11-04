'use strict';

angular.module('tmcApp').controller('FuncionarioDialogController',
    ['$scope', '$http', '$stateParams', '$modalInstance', '$q', 'entity', 'Funcionario', 'Cep', 'Condominio', 'TelefoneFuncionario', 'Comentario',
        function($scope, $http, $stateParams, $modalInstance, $q, entity, Funcionario, Cep, Condominio, TelefoneFuncionario, Comentario) {

        $scope.funcionario = entity;
        $scope.ceps = Cep.query({filter: 'funcionario-is-null'});
        $q.all([$scope.funcionario.$promise, $scope.ceps.$promise]).then(function() {
            if (!$scope.funcionario.cep.id) {
                return $q.reject();
            }
            return Cep.get({id : $scope.funcionario.cep.id}).$promise;
        }).then(function(cep) {
            $scope.ceps.push(cep);
        });
        $scope.condominios = Condominio.query();
        $scope.telefonefuncionarios = TelefoneFuncionario.query();
        $scope.comentarios = Comentario.query();
        $scope.load = function(id) {
            Funcionario.get({id : id}, function(result) {
                $scope.funcionario = result;
            });
        };

        var onSaveFinished = function (result) {
            $scope.$emit('tmcApp:funcionarioUpdate', result);
            $modalInstance.close(result);
        };

        $scope.save = function () {
            if ($scope.funcionario.id != null) {
                Funcionario.update($scope.funcionario, onSaveFinished);
            } else {
                Funcionario.save($scope.funcionario, onSaveFinished);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };

        $scope.buscaCep = function (cep) {
            if (cep != null) {
                var url = "http://cep.republicavirtual.com.br/web_cep.php?formato=json&cep=" + cep;
                var response = $http.get(url);
                response.success(function(resultado) {
                    $scope.logradouro = resultado.logradouro;
                    $scope.bairro = resultado.bairro;
                    $scope.cidade = resultado.cidade;
                    $scope.uf = resultado.uf;
                    return false;
                });
            }
        };
}]);
