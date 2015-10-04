'use strict';

angular.module('tmcApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('morador', {
                parent: 'entity',
                url: '/moradors',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'tmcApp.morador.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/morador/moradors.html',
                        controller: 'MoradorController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('morador');
                        $translatePartialLoader.addPart('sexo');
                        $translatePartialLoader.addPart('tipoMorador');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('morador.detail', {
                parent: 'entity',
                url: '/morador/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'tmcApp.morador.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/morador/morador-detail.html',
                        controller: 'MoradorDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('morador');
                        $translatePartialLoader.addPart('sexo');
                        $translatePartialLoader.addPart('tipoMorador');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'Morador', function($stateParams, Morador) {
                        return Morador.get({id : $stateParams.id});
                    }]
                }
            })
            .state('morador.new', {
                parent: 'morador',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/morador/morador-dialog.html',
                        controller: 'MoradorDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {nome: null, cpf: null, sexo: null, email: null, senha: null, dataNascimento: null, ativo: null, bloqueiaAgendamento: null, telefone: null, tipo: null, id: null};
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('morador', null, { reload: true });
                    }, function() {
                        $state.go('morador');
                    })
                }]
            })
            .state('morador.edit', {
                parent: 'morador',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/morador/morador-dialog.html',
                        controller: 'MoradorDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Morador', function(Morador) {
                                return Morador.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('morador', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
