'use strict';

angular.module('tmcApp')
    .controller('NavbarController', function ($scope, $location, $state, Auth, Principal, ENV) {

        Principal.identity().then(function (account) {
            $scope.account = account;
            $scope.isAuthenticated = Principal.isAuthenticated;
        });

        $scope.$state = $state;

        $scope.inProduction = ENV === 'prod';

        $scope.logout = function () {
            Auth.logout();
            $state.go('login');
        };
    });
