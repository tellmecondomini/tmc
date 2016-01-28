'use strict';

angular.module('tmcApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('avaliaCompetencia', {
                parent: 'entity',
                url: '/avaliaCompetencia',
                data: {
                    authorities: ['ROLE_ADMIN', 'ROLE_ADMIN_CONDOMINIO', 'ROLE_MORADOR'],
                    pageTitle: 'tmcApp.avaliaCompetencia.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/avaliaCompetencia/avaliaCompetencias.html',
                        controller: 'AvaliaCompetenciaController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('avaliaCompetencia');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('avaliaCompetencia.detail', {
                parent: 'entity',
                url: '/avaliaCompetencia/{idPrestador}/{idCompetencia}',
                data: {
                    authorities: ['ROLE_ADMIN', 'ROLE_ADMIN_CONDOMINIO', 'ROLE_MORADOR'],
                    pageTitle: 'tmcApp.avaliaCompetencia.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/avaliaCompetencia/avaliaCompetencia-detail.html',
                        controller: 'AvaliaCompetenciaDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('avaliaCompetencia');
                        return $translate.refresh();
                    }],
                    list: ['$stateParams', 'GetAvaliacoes', function ($stateParams, GetAvaliacoes) {
                        return GetAvaliacoes.query({
                            idPrestador: $stateParams.idPrestador,
                            idCompetencia: $stateParams.idCompetencia
                        });
                    }]
                }
            })
            .state('avaliaCompetencia.new', {
                parent: 'avaliaCompetencia',
                url: '/new',
                data: {
                    authorities: ['ROLE_ADMIN', 'ROLE_ADMIN_CONDOMINIO', 'ROLE_MORADOR'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function ($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/avaliaCompetencia/avaliaCompetencia-dialog.html',
                        controller: 'AvaliaCompetenciaDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {nota: null, mensagem: null, ativo: null, id: null};
                            }
                        }
                    }).result.then(function (result) {
                        $state.go('avaliaCompetencia', null, {reload: true});
                    }, function () {
                        $state.go('avaliaCompetencia');
                    })
                }]
            })
            .state('avaliaCompetencia.edit', {
                parent: 'avaliaCompetencia',
                url: '/{idPrestador}/{idCompetencia}/edit',
                data: {
                    authorities: ['ROLE_ADMIN', 'ROLE_ADMIN_CONDOMINIO', 'ROLE_MORADOR'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function ($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/avaliaCompetencia/avaliaCompetencia-dialog.html',
                        controller: 'AvaliaCompetenciaDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['NewAvaliacao', function (NewAvaliacao) {
                                return NewAvaliacao.get({
                                    idPrestador: $stateParams.idPrestador,
                                    idCompetencia: $stateParams.idCompetencia
                                });
                            }]
                        }
                    }).result.then(function (result) {
                        $state.go('avaliaCompetencia', null, {reload: true});
                    }, function () {
                        $state.go('^');
                    })
                }]
            });
    });
