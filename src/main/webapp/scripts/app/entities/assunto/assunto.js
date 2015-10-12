'use strict';

angular.module('tmcApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('assunto', {
                parent: 'entity',
                url: '/assuntos',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'tmcApp.assunto.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/assunto/assuntos.html',
                        controller: 'AssuntoController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('assunto');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('assunto.detail', {
                parent: 'entity',
                url: '/assunto/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'tmcApp.assunto.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/assunto/assunto-detail.html',
                        controller: 'AssuntoDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('assunto');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'Assunto', function($stateParams, Assunto) {
                        return Assunto.get({id : $stateParams.id});
                    }]
                }
            })
            .state('assunto.new', {
                parent: 'assunto',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/assunto/assunto-dialog.html',
                        controller: 'AssuntoDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {descricao: null, id: null};
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('assunto', null, { reload: true });
                    }, function() {
                        $state.go('assunto');
                    })
                }]
            })
            .state('assunto.edit', {
                parent: 'assunto',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/assunto/assunto-dialog.html',
                        controller: 'AssuntoDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Assunto', function(Assunto) {
                                return Assunto.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('assunto', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
