'use strict';

angular.module('tmcApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('ocorrenciaTipo', {
                parent: 'entity',
                url: '/ocorrenciaTipos',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'tmcApp.ocorrenciaTipo.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/ocorrenciaTipo/ocorrenciaTipos.html',
                        controller: 'OcorrenciaTipoController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('ocorrenciaTipo');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('ocorrenciaTipo.detail', {
                parent: 'entity',
                url: '/ocorrenciaTipo/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'tmcApp.ocorrenciaTipo.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/ocorrenciaTipo/ocorrenciaTipo-detail.html',
                        controller: 'OcorrenciaTipoDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('ocorrenciaTipo');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'OcorrenciaTipo', function($stateParams, OcorrenciaTipo) {
                        return OcorrenciaTipo.get({id : $stateParams.id});
                    }]
                }
            })
            .state('ocorrenciaTipo.new', {
                parent: 'ocorrenciaTipo',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/ocorrenciaTipo/ocorrenciaTipo-dialog.html',
                        controller: 'OcorrenciaTipoDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {id: null};
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('ocorrenciaTipo', null, { reload: true });
                    }, function() {
                        $state.go('ocorrenciaTipo');
                    })
                }]
            })
            .state('ocorrenciaTipo.edit', {
                parent: 'ocorrenciaTipo',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/ocorrenciaTipo/ocorrenciaTipo-dialog.html',
                        controller: 'OcorrenciaTipoDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['OcorrenciaTipo', function(OcorrenciaTipo) {
                                return OcorrenciaTipo.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('ocorrenciaTipo', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
