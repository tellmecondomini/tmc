'use strict';

angular.module('tmcApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('comentario', {
                parent: 'entity',
                url: '/comentarios',
                data: {
                    authorities: ['ROLE_ADMIN', 'ROLE_ADMIN_CONDOMINIO', 'ROLE_FUNCIONARIO', 'ROLE_MORADOR'],
                    pageTitle: 'tmcApp.comentario.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/comentario/comentarios.html',
                        controller: 'ComentarioController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('comentario');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('comentario.detail', {
                parent: 'entity',
                url: '/comentario/{id}',
                data: {
                    authorities: ['ROLE_ADMIN', 'ROLE_ADMIN_CONDOMINIO', 'ROLE_FUNCIONARIO', 'ROLE_MORADOR'],
                    pageTitle: 'tmcApp.comentario.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/comentario/comentario-detail.html',
                        controller: 'ComentarioDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('comentario');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'Comentario', function ($stateParams, Comentario) {
                        return Comentario.get({id: $stateParams.id});
                    }]
                }
            })
            .state('comentario.new', {
                parent: 'comentario',
                url: '/new',
                data: {
                    authorities: ['ROLE_ADMIN', 'ROLE_ADMIN_CONDOMINIO', 'ROLE_FUNCIONARIO', 'ROLE_MORADOR'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function ($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/comentario/comentario-dialog.html',
                        controller: 'ComentarioDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {conteudo: null, data: null, ativo: null, id: null};
                            }
                        }
                    }).result.then(function (result) {
                        $state.go('comentario', null, {reload: true});
                    }, function () {
                        $state.go('comentario');
                    })
                }]
            })
            .state('comentario.edit', {
                parent: 'comentario',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_ADMIN', 'ROLE_ADMIN_CONDOMINIO', 'ROLE_FUNCIONARIO', 'ROLE_MORADOR'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function ($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/comentario/comentario-dialog.html',
                        controller: 'ComentarioDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Comentario', function (Comentario) {
                                return Comentario.get({id: $stateParams.id});
                            }]
                        }
                    }).result.then(function (result) {
                        $state.go('comentario', null, {reload: true});
                    }, function () {
                        $state.go('^');
                    })
                }]
            });
    });
