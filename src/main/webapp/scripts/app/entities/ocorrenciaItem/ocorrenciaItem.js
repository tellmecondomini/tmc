'use strict';

angular.module('tmcApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('ocorrenciaItem', {
                parent: 'entity',
                url: '/ocorrenciaItems',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'tmcApp.ocorrenciaItem.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/ocorrenciaItem/ocorrenciaItems.html',
                        controller: 'OcorrenciaItemController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('ocorrenciaItem');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('ocorrenciaItem.detail', {
                parent: 'entity',
                url: '/ocorrenciaItem/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'tmcApp.ocorrenciaItem.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/ocorrenciaItem/ocorrenciaItem-detail.html',
                        controller: 'OcorrenciaItemDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('ocorrenciaItem');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'OcorrenciaItem', function($stateParams, OcorrenciaItem) {
                        return OcorrenciaItem.get({id : $stateParams.id});
                    }]
                }
            })
            .state('ocorrenciaItem.new', {
                parent: 'ocorrenciaItem',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/ocorrenciaItem/ocorrenciaItem-dialog.html',
                        controller: 'OcorrenciaItemDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {descricao: null, id: null};
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('ocorrenciaItem', null, { reload: true });
                    }, function() {
                        $state.go('ocorrenciaItem');
                    })
                }]
            })
            .state('ocorrenciaItem.edit', {
                parent: 'ocorrenciaItem',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/ocorrenciaItem/ocorrenciaItem-dialog.html',
                        controller: 'OcorrenciaItemDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['OcorrenciaItem', function(OcorrenciaItem) {
                                return OcorrenciaItem.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('ocorrenciaItem', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
