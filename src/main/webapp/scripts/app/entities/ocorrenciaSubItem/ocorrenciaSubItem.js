'use strict';

angular.module('tmcApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('ocorrenciaSubItem', {
                parent: 'entity',
                url: '/ocorrenciaSubItems',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'tmcApp.ocorrenciaSubItem.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/ocorrenciaSubItem/ocorrenciaSubItems.html',
                        controller: 'OcorrenciaSubItemController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('ocorrenciaSubItem');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('ocorrenciaSubItem.detail', {
                parent: 'entity',
                url: '/ocorrenciaSubItem/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'tmcApp.ocorrenciaSubItem.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/ocorrenciaSubItem/ocorrenciaSubItem-detail.html',
                        controller: 'OcorrenciaSubItemDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('ocorrenciaSubItem');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'OcorrenciaSubItem', function($stateParams, OcorrenciaSubItem) {
                        return OcorrenciaSubItem.get({id : $stateParams.id});
                    }]
                }
            })
            .state('ocorrenciaSubItem.new', {
                parent: 'ocorrenciaSubItem',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/ocorrenciaSubItem/ocorrenciaSubItem-dialog.html',
                        controller: 'OcorrenciaSubItemDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {descricao: null, id: null};
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('ocorrenciaSubItem', null, { reload: true });
                    }, function() {
                        $state.go('ocorrenciaSubItem');
                    })
                }]
            })
            .state('ocorrenciaSubItem.edit', {
                parent: 'ocorrenciaSubItem',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/ocorrenciaSubItem/ocorrenciaSubItem-dialog.html',
                        controller: 'OcorrenciaSubItemDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['OcorrenciaSubItem', function(OcorrenciaSubItem) {
                                return OcorrenciaSubItem.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('ocorrenciaSubItem', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
