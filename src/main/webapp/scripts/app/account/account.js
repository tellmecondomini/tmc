'use strict';

angular.module('tmcApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('account', {
                abstract: true,
                parent: 'site'
            });
    });
