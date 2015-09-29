'use strict';

angular.module('tmcApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('ocorrencia', {
                parent: 'entity',
                url: '/ocorrencias',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'tmcApp.ocorrencia.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/ocorrencia/ocorrencias.html',
                        controller: 'OcorrenciaController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('ocorrencia');
                        $translatePartialLoader.addPart('statusSolicitacao');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('ocorrencia.detail', {
                parent: 'entity',
                url: '/ocorrencia/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'tmcApp.ocorrencia.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/ocorrencia/ocorrencia-detail.html',
                        controller: 'OcorrenciaDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('ocorrencia');
                        $translatePartialLoader.addPart('statusSolicitacao');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'Ocorrencia', function($stateParams, Ocorrencia) {
                        return Ocorrencia.get({id : $stateParams.id});
                    }]
                }
            })
            .state('ocorrencia.new', {
                parent: 'ocorrencia',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/ocorrencia/ocorrencia-dialog.html',
                        controller: 'OcorrenciaDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {dataAbertura: null, dataFechamento: null, menssagem: null, status: null, id: null};
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('ocorrencia', null, { reload: true });
                    }, function() {
                        $state.go('ocorrencia');
                    })
                }]
            })
            .state('ocorrencia.edit', {
                parent: 'ocorrencia',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/ocorrencia/ocorrencia-dialog.html',
                        controller: 'OcorrenciaDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Ocorrencia', function(Ocorrencia) {
                                return Ocorrencia.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('ocorrencia', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
