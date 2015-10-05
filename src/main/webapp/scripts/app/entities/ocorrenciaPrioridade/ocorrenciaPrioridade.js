'use strict';

angular.module('tmcApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('ocorrenciaPrioridade', {
                parent: 'entity',
                url: '/ocorrenciaPrioridades',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'tmcApp.ocorrenciaPrioridade.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/ocorrenciaPrioridade/ocorrenciaPrioridades.html',
                        controller: 'OcorrenciaPrioridadeController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('ocorrenciaPrioridade');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('ocorrenciaPrioridade.detail', {
                parent: 'entity',
                url: '/ocorrenciaPrioridade/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'tmcApp.ocorrenciaPrioridade.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/ocorrenciaPrioridade/ocorrenciaPrioridade-detail.html',
                        controller: 'OcorrenciaPrioridadeDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('ocorrenciaPrioridade');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'OcorrenciaPrioridade', function($stateParams, OcorrenciaPrioridade) {
                        return OcorrenciaPrioridade.get({id : $stateParams.id});
                    }]
                }
            })
            .state('ocorrenciaPrioridade.new', {
                parent: 'ocorrenciaPrioridade',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/ocorrenciaPrioridade/ocorrenciaPrioridade-dialog.html',
                        controller: 'OcorrenciaPrioridadeDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {descricao: null, id: null};
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('ocorrenciaPrioridade', null, { reload: true });
                    }, function() {
                        $state.go('ocorrenciaPrioridade');
                    })
                }]
            })
            .state('ocorrenciaPrioridade.edit', {
                parent: 'ocorrenciaPrioridade',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/ocorrenciaPrioridade/ocorrenciaPrioridade-dialog.html',
                        controller: 'OcorrenciaPrioridadeDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['OcorrenciaPrioridade', function(OcorrenciaPrioridade) {
                                return OcorrenciaPrioridade.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('ocorrenciaPrioridade', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
