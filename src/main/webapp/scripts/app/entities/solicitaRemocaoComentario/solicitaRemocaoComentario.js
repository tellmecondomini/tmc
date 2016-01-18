'use strict';

angular.module('tmcApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('solicitaRemocaoComentario', {
                parent: 'entity',
                url: '/solicitaRemocaoComentarios',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'tmcApp.solicitaRemocaoComentario.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/solicitaRemocaoComentario/solicitaRemocaoComentarios.html',
                        controller: 'SolicitaRemocaoComentarioController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('solicitaRemocaoComentario');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('solicitaRemocaoComentario.detail', {
                parent: 'entity',
                url: '/solicitaRemocaoComentario/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'tmcApp.solicitaRemocaoComentario.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/solicitaRemocaoComentario/solicitaRemocaoComentario-detail.html',
                        controller: 'SolicitaRemocaoComentarioDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('solicitaRemocaoComentario');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'SolicitaRemocaoComentario', function($stateParams, SolicitaRemocaoComentario) {
                        return SolicitaRemocaoComentario.get({id : $stateParams.id});
                    }]
                }
            })
            .state('solicitaRemocaoComentario.new', {
                parent: 'solicitaRemocaoComentario',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/solicitaRemocaoComentario/solicitaRemocaoComentario-dialog.html',
                        controller: 'SolicitaRemocaoComentarioDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {data: null, motivo: null, dataAtendimento: null, observacao: null, id: null};
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('solicitaRemocaoComentario', null, { reload: true });
                    }, function() {
                        $state.go('solicitaRemocaoComentario');
                    })
                }]
            })
            .state('solicitaRemocaoComentario.edit', {
                parent: 'solicitaRemocaoComentario',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/solicitaRemocaoComentario/solicitaRemocaoComentario-dialog.html',
                        controller: 'SolicitaRemocaoComentarioDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['SolicitaRemocaoComentario', function(SolicitaRemocaoComentario) {
                                return SolicitaRemocaoComentario.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('solicitaRemocaoComentario', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
