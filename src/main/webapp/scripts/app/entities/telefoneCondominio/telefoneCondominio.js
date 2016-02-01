'use strict';

angular.module('tmcApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('telefoneCondominio', {
                parent: 'entity',
                url: '/telefoneCondominios',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'tmcApp.telefoneCondominio.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/telefoneCondominio/telefoneCondominios.html',
                        controller: 'TelefoneCondominioController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('telefoneCondominio');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('telefoneCondominio.detail', {
                parent: 'entity',
                url: '/telefoneCondominio/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'tmcApp.telefoneCondominio.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/telefoneCondominio/telefoneCondominio-detail.html',
                        controller: 'TelefoneCondominioDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('telefoneCondominio');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'TelefoneCondominio', function ($stateParams, TelefoneCondominio) {
                        return TelefoneCondominio.get({id: $stateParams.id});
                    }]
                }
            })
            .state('telefoneCondominio.new', {
                parent: 'telefoneCondominio',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function ($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/telefoneCondominio/telefoneCondominio-dialog.html',
                        controller: 'TelefoneCondominioDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {numero: null, id: null};
                            }
                        }
                    }).result.then(function (result) {
                        $state.go('telefoneCondominio', null, {reload: true});
                    }, function () {
                        $state.go('telefoneCondominio');
                    })
                }]
            })
            .state('telefoneCondominio.edit', {
                parent: 'telefoneCondominio',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function ($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/telefoneCondominio/telefoneCondominio-dialog.html',
                        controller: 'TelefoneCondominioDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['TelefoneCondominio', function (TelefoneCondominio) {
                                return TelefoneCondominio.get({id: $stateParams.id});
                            }]
                        }
                    }).result.then(function (result) {
                        $state.go('telefoneCondominio', null, {reload: true});
                    }, function () {
                        $state.go('^');
                    })
                }]
            });
    });
