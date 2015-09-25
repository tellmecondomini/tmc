'use strict';

angular.module('tmcApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('entity', {
                abstract: true,
                parent: 'site'
            });
    });
