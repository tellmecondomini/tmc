'use strict';

angular.module('tmcApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('prestadorServico', {
                parent: 'entity',
                url: '/prestadorServicos',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'tmcApp.prestadorServico.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/prestadorServico/prestadorServicos.html',
                        controller: 'PrestadorServicoController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('prestadorServico');
                        $translatePartialLoader.addPart('pessoa');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('prestadorServico.detail', {
                parent: 'entity',
                url: '/prestadorServico/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'tmcApp.prestadorServico.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/prestadorServico/prestadorServico-detail.html',
                        controller: 'PrestadorServicoDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('prestadorServico');
                        $translatePartialLoader.addPart('pessoa');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'PrestadorServico', function($stateParams, PrestadorServico) {
                        return PrestadorServico.get({id : $stateParams.id});
                    }]
                }
            })
            .state('prestadorServico.new', {
                parent: 'prestadorServico',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/prestadorServico/prestadorServico-dialog.html',
                        controller: 'PrestadorServicoDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {nome: null, email: null, documento: null, pessoa: null, numero: null, complemento: null, id: null};
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('prestadorServico', null, { reload: true });
                    }, function() {
                        $state.go('prestadorServico');
                    })
                }]
            })
            .state('prestadorServico.edit', {
                parent: 'prestadorServico',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/prestadorServico/prestadorServico-dialog.html',
                        controller: 'PrestadorServicoDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['PrestadorServico', function(PrestadorServico) {
                                return PrestadorServico.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('prestadorServico', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
