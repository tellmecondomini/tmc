'use strict';

angular.module('tmcApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('telefoneFuncionario', {
                parent: 'entity',
                url: '/telefoneFuncionarios',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'tmcApp.telefoneFuncionario.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/telefoneFuncionario/telefoneFuncionarios.html',
                        controller: 'TelefoneFuncionarioController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('telefoneFuncionario');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('telefoneFuncionario.detail', {
                parent: 'entity',
                url: '/telefoneFuncionario/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'tmcApp.telefoneFuncionario.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/telefoneFuncionario/telefoneFuncionario-detail.html',
                        controller: 'TelefoneFuncionarioDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('telefoneFuncionario');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'TelefoneFuncionario', function($stateParams, TelefoneFuncionario) {
                        return TelefoneFuncionario.get({id : $stateParams.id});
                    }]
                }
            })
            .state('telefoneFuncionario.new', {
                parent: 'telefoneFuncionario',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/telefoneFuncionario/telefoneFuncionario-dialog.html',
                        controller: 'TelefoneFuncionarioDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {numero: null, id: null};
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('telefoneFuncionario', null, { reload: true });
                    }, function() {
                        $state.go('telefoneFuncionario');
                    })
                }]
            })
            .state('telefoneFuncionario.edit', {
                parent: 'telefoneFuncionario',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/telefoneFuncionario/telefoneFuncionario-dialog.html',
                        controller: 'TelefoneFuncionarioDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['TelefoneFuncionario', function(TelefoneFuncionario) {
                                return TelefoneFuncionario.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('telefoneFuncionario', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
