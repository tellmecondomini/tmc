'use strict';

angular.module('tmcApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('categoria', {
                parent: 'entity',
                url: '/categorias',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'tmcApp.categoria.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/categoria/categorias.html',
                        controller: 'CategoriaController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('categoria');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('categoria.detail', {
                parent: 'entity',
                url: '/categoria/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'tmcApp.categoria.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/categoria/categoria-detail.html',
                        controller: 'CategoriaDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('categoria');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'Categoria', function ($stateParams, Categoria) {
                        return Categoria.get({id: $stateParams.id});
                    }]
                }
            })
            .state('categoria.new', {
                parent: 'categoria',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function ($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/categoria/categoria-dialog.html',
                        controller: 'CategoriaDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {descricao: null, id: null};
                            }
                        }
                    }).result.then(function (result) {
                        $state.go('categoria', null, {reload: true});
                    }, function () {
                        $state.go('categoria');
                    })
                }]
            })
            .state('categoria.edit', {
                parent: 'categoria',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function ($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/categoria/categoria-dialog.html',
                        controller: 'CategoriaDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Categoria', function (Categoria) {
                                return Categoria.get({id: $stateParams.id});
                            }]
                        }
                    }).result.then(function (result) {
                        $state.go('categoria', null, {reload: true});
                    }, function () {
                        $state.go('^');
                    })
                }]
            });
    });
