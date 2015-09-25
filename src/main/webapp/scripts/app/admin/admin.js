'use strict';

angular.module('tmcApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('admin', {
                abstract: true,
                parent: 'site'
            });
    });
