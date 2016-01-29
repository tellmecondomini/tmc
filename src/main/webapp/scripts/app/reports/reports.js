'use strict';

angular.module('tmcApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('reports', {
                abstract: true,
                parent: 'site'
            })
            .state('reports.prestadores', {
                parent: 'reports',
                url: '/reports/prestadores',
                data: {
                    authorities: ['ROLE_ADMIN', 'ROLE_ADMIN_CONDOMINIO', 'ROLE_MORADOR']
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/reports/prestadores.html',
                        controller: 'PrestadoresNotasController'
                    }
                },
                resolve: {
                    mainTranslatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        return $translate.refresh();
                    }]
                }
            });
    });
