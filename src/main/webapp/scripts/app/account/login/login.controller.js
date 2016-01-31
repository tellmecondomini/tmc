'use strict';

angular.module('tmcApp')
    .controller('LoginController', function ($rootScope, $scope, $state, $window, $timeout, Auth, AccountMorador) {
        $scope.user = {};
        $scope.errors = {};

        $scope.rememberMe = true;
        $timeout(function () {
            angular.element('[ng-model="email"]').focus();
        });
        $scope.login = function (event) {
            event.preventDefault();
            Auth.login({
                email: $scope.email,
                password: $scope.password,
                rememberMe: $scope.rememberMe
            }).then(function () {
                $scope.authenticationError = false;
                AccountMorador.get(function (morador) {
                    if (morador.id == null)
                        $state.go('home');
                    else
                        $state.go('topico');
                });
            }).catch(function () {
                $scope.authenticationError = true;
            });
        };
    });
