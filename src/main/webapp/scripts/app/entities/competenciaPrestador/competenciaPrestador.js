'use strict';

angular.module('tmcApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('competenciaPrestador', {
                parent: 'entity',
                url: '/competenciaPrestadors',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'tmcApp.competenciaPrestador.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/competenciaPrestador/competenciaPrestadors.html',
                        controller: 'CompetenciaPrestadorController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('competenciaPrestador');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('competenciaPrestador.detail', {
                parent: 'entity',
                url: '/competenciaPrestador/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'tmcApp.competenciaPrestador.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/competenciaPrestador/competenciaPrestador-detail.html',
                        controller: 'CompetenciaPrestadorDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('competenciaPrestador');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'CompetenciaPrestador', function ($stateParams, CompetenciaPrestador) {
                        return CompetenciaPrestador.get({id: $stateParams.id});
                    }]
                }
            })
            .state('competenciaPrestador.new', {
                parent: 'competenciaPrestador',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function ($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/competenciaPrestador/competenciaPrestador-dialog.html',
                        controller: 'CompetenciaPrestadorDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {descricao: null, id: null};
                            }
                        }
                    }).result.then(function (result) {
                        $state.go('competenciaPrestador', null, {reload: true});
                    }, function () {
                        $state.go('competenciaPrestador');
                    })
                }]
            })
            .state('competenciaPrestador.edit', {
                parent: 'competenciaPrestador',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function ($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/competenciaPrestador/competenciaPrestador-dialog.html',
                        controller: 'CompetenciaPrestadorDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['CompetenciaPrestador', function (CompetenciaPrestador) {
                                return CompetenciaPrestador.get({id: $stateParams.id});
                            }]
                        }
                    }).result.then(function (result) {
                        $state.go('competenciaPrestador', null, {reload: true});
                    }, function () {
                        $state.go('^');
                    })
                }]
            });
    });
