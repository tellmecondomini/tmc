'use strict';

angular.module('tmcApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('telefonePrestadorServico', {
                parent: 'entity',
                url: '/telefonePrestadorServicos',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'tmcApp.telefonePrestadorServico.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/telefonePrestadorServico/telefonePrestadorServicos.html',
                        controller: 'TelefonePrestadorServicoController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('telefonePrestadorServico');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('telefonePrestadorServico.detail', {
                parent: 'entity',
                url: '/telefonePrestadorServico/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'tmcApp.telefonePrestadorServico.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/telefonePrestadorServico/telefonePrestadorServico-detail.html',
                        controller: 'TelefonePrestadorServicoDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('telefonePrestadorServico');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'TelefonePrestadorServico', function($stateParams, TelefonePrestadorServico) {
                        return TelefonePrestadorServico.get({id : $stateParams.id});
                    }]
                }
            })
            .state('telefonePrestadorServico.new', {
                parent: 'telefonePrestadorServico',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/telefonePrestadorServico/telefonePrestadorServico-dialog.html',
                        controller: 'TelefonePrestadorServicoDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {numero: null, id: null};
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('telefonePrestadorServico', null, { reload: true });
                    }, function() {
                        $state.go('telefonePrestadorServico');
                    })
                }]
            })
            .state('telefonePrestadorServico.edit', {
                parent: 'telefonePrestadorServico',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/telefonePrestadorServico/telefonePrestadorServico-dialog.html',
                        controller: 'TelefonePrestadorServicoDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['TelefonePrestadorServico', function(TelefonePrestadorServico) {
                                return TelefonePrestadorServico.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('telefonePrestadorServico', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
