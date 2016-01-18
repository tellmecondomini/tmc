'use strict';

angular.module('tmcApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('avaliaCompetencia', {
                parent: 'entity',
                url: '/avaliaCompetencias',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'tmcApp.avaliaCompetencia.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/avaliaCompetencia/avaliaCompetencias.html',
                        controller: 'AvaliaCompetenciaController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('avaliaCompetencia');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('avaliaCompetencia.detail', {
                parent: 'entity',
                url: '/avaliaCompetencia/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'tmcApp.avaliaCompetencia.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/avaliaCompetencia/avaliaCompetencia-detail.html',
                        controller: 'AvaliaCompetenciaDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('avaliaCompetencia');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'AvaliaCompetencia', function($stateParams, AvaliaCompetencia) {
                        return AvaliaCompetencia.get({id : $stateParams.id});
                    }]
                }
            })
            .state('avaliaCompetencia.new', {
                parent: 'avaliaCompetencia',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/avaliaCompetencia/avaliaCompetencia-dialog.html',
                        controller: 'AvaliaCompetenciaDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {nota: null, mensagem: null, ativo: null, id: null};
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('avaliaCompetencia', null, { reload: true });
                    }, function() {
                        $state.go('avaliaCompetencia');
                    })
                }]
            })
            .state('avaliaCompetencia.edit', {
                parent: 'avaliaCompetencia',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/avaliaCompetencia/avaliaCompetencia-dialog.html',
                        controller: 'AvaliaCompetenciaDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['AvaliaCompetencia', function(AvaliaCompetencia) {
                                return AvaliaCompetencia.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('avaliaCompetencia', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
