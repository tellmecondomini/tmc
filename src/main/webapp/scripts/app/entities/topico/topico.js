'use strict';

angular.module('tmcApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('topico', {
                parent: 'entity',
                url: '/topicos',
                data: {
                    authorities: ['ROLE_ADMIN', 'ROLE_ADMIN_CONDOMINIO', 'ROLE_FUNCIONARIO', 'ROLE_MORADOR'],
                    pageTitle: 'tmcApp.topico.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/topico/topicos.html',
                        controller: 'TopicoController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('topico');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('topico.detail', {
                parent: 'entity',
                url: '/topico/{id}',
                data: {
                    authorities: ['ROLE_ADMIN', 'ROLE_ADMIN_CONDOMINIO', 'ROLE_FUNCIONARIO', 'ROLE_MORADOR'],
                    pageTitle: 'tmcApp.topico.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/topico/topico-detail.html',
                        controller: 'TopicoDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('topico');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'Topico', function ($stateParams, Topico) {
                        return Topico.get({id: $stateParams.id});
                    }]
                }
            })
            .state('topico.new', {
                parent: 'topico',
                url: '/new',
                data: {
                    authorities: ['ROLE_ADMIN', 'ROLE_ADMIN_CONDOMINIO', 'ROLE_FUNCIONARIO', 'ROLE_MORADOR'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function ($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/topico/topico-dialog.html',
                        controller: 'TopicoDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    conteudo: null,
                                    data: null,
                                    aprovado: null,
                                    id: null,
                                    imagem: null,
                                    prioritario: false
                                };
                            }
                        }
                    }).result.then(function (result) {
                        $state.go('topico', null, {reload: true});
                    }, function () {
                        $state.go('topico');
                    })
                }]
            })
            .state('topico.edit', {
                parent: 'topico',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_ADMIN', 'ROLE_ADMIN_CONDOMINIO', 'ROLE_FUNCIONARIO', 'ROLE_MORADOR'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function ($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/topico/topico-dialog.html',
                        controller: 'TopicoDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Topico', function (Topico) {
                                return Topico.get({id: $stateParams.id});
                            }]
                        }
                    }).result.then(function (result) {
                        $state.go('topico', null, {reload: true});
                    }, function () {
                        $state.go('^');
                    })
                }]
            })
            .state('topico.comentarios', {
                parent: 'topico',
                url: '/{id}/comentarios',
                data: {
                    authorities: ['ROLE_ADMIN', 'ROLE_ADMIN_CONDOMINIO', 'ROLE_FUNCIONARIO', 'ROLE_MORADOR'],
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/topico/topico-comentarios.html',
                        controller: 'TopicoComentariosController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('topico');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'Topico', function ($stateParams, Topico) {
                        return Topico.get({id: $stateParams.id});
                    }]
                }
            })
            .state('topico.encerra', {
                parent: 'topico',
                url: 'topico/{id}/encerrar',
                data: {
                    authorities: ['ROLE_ADMIN', 'ROLE_ADMIN_CONDOMINIO'],
                },
                onEnter: ['$stateParams', '$state', '$modal', 'Topico', function ($stateParams, $state, $modal, Topico) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/topico/topico-encerra.html',
                        controller: 'TopicoEncerraController',
                        size: 'lg',
                        resolve: {
                            entity: ['Topico', function (Topico) {
                                return Topico.get({id: $stateParams.id});
                            }]
                        }
                    }).result.then(function (result) {
                        $state.go('topico', null, {reload: true});
                    }, function () {
                        $state.go('^');
                    })
                }]
            });
    });
