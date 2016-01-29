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
                    authorities: ['ROLE_ADMIN', 'ROLE_ADMIN_CONDOMINIO', 'ROLE_FUNCIONARIO', 'ROLE_MORADOR']
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/reports/prestadores.html',
                        controller: 'PrestadoresNotasController'
                    }
                },
                resolve: {
                    mainTranslatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate) {
                        return $translate.refresh();
                    }]
                }
            })
            .state('reports.topicoscategorias', {
                parent: 'reports',
                url: '/reports/topicos/categorias',
                data: {
                    authorities: ['ROLE_ADMIN', 'ROLE_ADMIN_CONDOMINIO', 'ROLE_FUNCIONARIO']
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/reports/topicos-categorias.html',
                        controller: 'TopicosCategoriasController'
                    }
                },
                resolve: {
                    entity: function () {
                        return {
                            dataInicio: new Date(),
                            dataFim: new Date()
                        };
                    },
                    mainTranslatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate) {
                        return $translate.refresh();
                    }]
                }
            })
            .state('reports.topicosstatus', {
                parent: 'reports',
                url: '/reports/topicos/status',
                data: {
                    authorities: ['ROLE_ADMIN', 'ROLE_ADMIN_CONDOMINIO', 'ROLE_FUNCIONARIO']
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/reports/topicos-status.html',
                        controller: 'TopicosStatusController'
                    }
                },
                resolve: {
                    entity: function () {
                        return {
                            dataInicio: new Date(),
                            dataFim: new Date()
                        };
                    },
                    mainTranslatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate) {
                        return $translate.refresh();
                    }]
                }
            });
    });
