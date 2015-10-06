'use strict';

angular.module('tmcApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('dependencia', {
                parent: 'entity',
                url: '/dependencias',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'tmcApp.dependencia.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/dependencia/dependencias.html',
                        controller: 'DependenciaController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('dependencia');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('dependencia.detail', {
                parent: 'entity',
                url: '/dependencia/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'tmcApp.dependencia.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/dependencia/dependencia-detail.html',
                        controller: 'DependenciaDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('dependencia');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'Dependencia', function($stateParams, Dependencia) {
                        return Dependencia.get({id : $stateParams.id});
                    }]
                }
            })
            .state('dependencia.new', {
                parent: 'dependencia',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/dependencia/dependencia-dialog.html',
                        controller: 'DependenciaDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {nome: null, disponivel: null, capacidade: null, custoAdicional: null, regraUso: null, id: null};
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('dependencia', null, { reload: true });
                    }, function() {
                        $state.go('dependencia');
                    })
                }]
            })
            .state('dependencia.edit', {
                parent: 'dependencia',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/dependencia/dependencia-dialog.html',
                        controller: 'DependenciaDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Dependencia', function(Dependencia) {
                                return Dependencia.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('dependencia', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
