'use strict';

angular.module('tmcApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('telefoneMorador', {
                parent: 'entity',
                url: '/telefoneMoradors',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'tmcApp.telefoneMorador.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/telefoneMorador/telefoneMoradors.html',
                        controller: 'TelefoneMoradorController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('telefoneMorador');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('telefoneMorador.detail', {
                parent: 'entity',
                url: '/telefoneMorador/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'tmcApp.telefoneMorador.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/telefoneMorador/telefoneMorador-detail.html',
                        controller: 'TelefoneMoradorDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('telefoneMorador');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'TelefoneMorador', function ($stateParams, TelefoneMorador) {
                        return TelefoneMorador.get({id: $stateParams.id});
                    }]
                }
            })
            .state('telefoneMorador.new', {
                parent: 'telefoneMorador',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function ($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/telefoneMorador/telefoneMorador-dialog.html',
                        controller: 'TelefoneMoradorDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {numero: null, id: null};
                            }
                        }
                    }).result.then(function (result) {
                        $state.go('telefoneMorador', null, {reload: true});
                    }, function () {
                        $state.go('telefoneMorador');
                    })
                }]
            })
            .state('telefoneMorador.edit', {
                parent: 'telefoneMorador',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function ($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/telefoneMorador/telefoneMorador-dialog.html',
                        controller: 'TelefoneMoradorDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['TelefoneMorador', function (TelefoneMorador) {
                                return TelefoneMorador.get({id: $stateParams.id});
                            }]
                        }
                    }).result.then(function (result) {
                        $state.go('telefoneMorador', null, {reload: true});
                    }, function () {
                        $state.go('^');
                    })
                }]
            });
    });
