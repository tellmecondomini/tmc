'use strict';

angular.module('tmcApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('dependente', {
                parent: 'entity',
                url: '/dependentes',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'tmcApp.dependente.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/dependente/dependentes.html',
                        controller: 'DependenteController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('dependente');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('dependente.detail', {
                parent: 'entity',
                url: '/dependente/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'tmcApp.dependente.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/dependente/dependente-detail.html',
                        controller: 'DependenteDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('dependente');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'Dependente', function($stateParams, Dependente) {
                        return Dependente.get({id : $stateParams.id});
                    }]
                }
            })
            .state('dependente.new', {
                parent: 'dependente',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/dependente/dependente-dialog.html',
                        controller: 'DependenteDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {nome: null, documento: null, id: null};
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('dependente', null, { reload: true });
                    }, function() {
                        $state.go('dependente');
                    })
                }]
            })
            .state('dependente.edit', {
                parent: 'dependente',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/dependente/dependente-dialog.html',
                        controller: 'DependenteDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Dependente', function(Dependente) {
                                return Dependente.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('dependente', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
