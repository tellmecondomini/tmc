'use strict';

angular.module('tmcApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('convidado', {
                parent: 'entity',
                url: '/convidados',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'tmcApp.convidado.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/convidado/convidados.html',
                        controller: 'ConvidadoController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('convidado');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('convidado.detail', {
                parent: 'entity',
                url: '/convidado/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'tmcApp.convidado.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/convidado/convidado-detail.html',
                        controller: 'ConvidadoDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('convidado');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'Convidado', function($stateParams, Convidado) {
                        return Convidado.get({id : $stateParams.id});
                    }]
                }
            })
            .state('convidado.new', {
                parent: 'convidado',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/convidado/convidado-dialog.html',
                        controller: 'ConvidadoDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {nome: null, cpf: null, email: null, id: null};
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('convidado', null, { reload: true });
                    }, function() {
                        $state.go('convidado');
                    })
                }]
            })
            .state('convidado.edit', {
                parent: 'convidado',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/convidado/convidado-dialog.html',
                        controller: 'ConvidadoDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Convidado', function(Convidado) {
                                return Convidado.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('convidado', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
