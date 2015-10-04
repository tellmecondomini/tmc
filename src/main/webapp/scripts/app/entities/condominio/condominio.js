'use strict';

angular.module('tmcApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('condominio', {
                parent: 'entity',
                url: '/condominios',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'tmcApp.condominio.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/condominio/condominios.html',
                        controller: 'CondominioController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('condominio');
                        $translatePartialLoader.addPart('disposicao');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('condominio.detail', {
                parent: 'entity',
                url: '/condominio/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'tmcApp.condominio.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/condominio/condominio-detail.html',
                        controller: 'CondominioDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('condominio');
                        $translatePartialLoader.addPart('disposicao');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'Condominio', function($stateParams, Condominio) {
                        return Condominio.get({id : $stateParams.id});
                    }]
                }
            })
            .state('condominio.new', {
                parent: 'condominio',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/condominio/condominio-dialog.html',
                        controller: 'CondominioDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {razaoSocial: null, cnpj: null, ativo: null, dataCadastro: null, telefone: null, disposicao: null, id: null};
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('condominio', null, { reload: true });
                    }, function() {
                        $state.go('condominio');
                    })
                }]
            })
            .state('condominio.edit', {
                parent: 'condominio',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/condominio/condominio-dialog.html',
                        controller: 'CondominioDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Condominio', function(Condominio) {
                                return Condominio.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('condominio', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
