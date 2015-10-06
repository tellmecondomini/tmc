'use strict';

angular.module('tmcApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('disponibilidadeDependencia', {
                parent: 'entity',
                url: '/disponibilidadeDependencias',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'tmcApp.disponibilidadeDependencia.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/disponibilidadeDependencia/disponibilidadeDependencias.html',
                        controller: 'DisponibilidadeDependenciaController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('disponibilidadeDependencia');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('disponibilidadeDependencia.detail', {
                parent: 'entity',
                url: '/disponibilidadeDependencia/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'tmcApp.disponibilidadeDependencia.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/disponibilidadeDependencia/disponibilidadeDependencia-detail.html',
                        controller: 'DisponibilidadeDependenciaDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('disponibilidadeDependencia');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'DisponibilidadeDependencia', function($stateParams, DisponibilidadeDependencia) {
                        return DisponibilidadeDependencia.get({id : $stateParams.id});
                    }]
                }
            })
            .state('disponibilidadeDependencia.new', {
                parent: 'disponibilidadeDependencia',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/disponibilidadeDependencia/disponibilidadeDependencia-dialog.html',
                        controller: 'DisponibilidadeDependenciaDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {diaSemana: null, horaInicio: null, horaFim: null, id: null};
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('disponibilidadeDependencia', null, { reload: true });
                    }, function() {
                        $state.go('disponibilidadeDependencia');
                    })
                }]
            })
            .state('disponibilidadeDependencia.edit', {
                parent: 'disponibilidadeDependencia',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/disponibilidadeDependencia/disponibilidadeDependencia-dialog.html',
                        controller: 'DisponibilidadeDependenciaDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['DisponibilidadeDependencia', function(DisponibilidadeDependencia) {
                                return DisponibilidadeDependencia.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('disponibilidadeDependencia', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
