'use strict';

angular.module('tmcApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('home', {
                parent: 'site',
                url: '/',
                data: {
                    authorities: []
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/main/main.html',
                        controller: 'MainController'
                    }
                },
                resolve: {
                    mainTranslatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('main');
                        return $translate.refresh();
                    }]
                }
            })
            .state('topico.aprova', {
                parent: 'home',
                url: 'topico/{id}/aprovacao',
                data: {
                    authorities: ['ROLE_ADMIN', 'ROLE_ADMIN_CONDOMINIO'],
                },
                onEnter: ['$stateParams', '$state', '$modal', 'Topico', function ($stateParams, $state, $modal, Topico) {
                    $modal.open({
                        templateUrl: 'scripts/app/main/topico-aprovacao.html',
                        controller: 'TopicoAprovacaoController',
                        size: 'lg',
                        resolve: {
                            entity: ['Topico', function (Topico) {
                                return Topico.get({id: $stateParams.id});
                            }]
                        }
                    }).result.then(function (result) {
                        $state.go('^', null, {reload: true});
                    }, function () {
                        $state.go('^');
                    })
                }]
            })
            .state('topico.reprova', {
                parent: 'home',
                url: 'topico/{id}/reprovacao',
                data: {
                    authorities: ['ROLE_ADMIN', 'ROLE_ADMIN_CONDOMINIO'],
                },
                onEnter: ['$stateParams', '$state', '$modal', 'Topico', function ($stateParams, $state, $modal, Topico) {
                    $modal.open({
                        templateUrl: 'scripts/app/main/topico-reprovacao.html',
                        controller: 'TopicoReprovacaoController',
                        size: 'lg',
                        resolve: {
                            entity: ['Topico', function (Topico) {
                                return Topico.get({id: $stateParams.id});
                            }]
                        }
                    }).result.then(function (result) {
                        $state.go('^', null, {reload: true});
                    }, function () {
                        $state.go('^');
                    })
                }]
            })
            .state('aprovacao', {
                parent: 'home',
                url: 'solicitacao/{id}/aprovacao',
                data: {
                    authorities: ['ROLE_ADMIN', 'ROLE_ADMIN_CONDOMINIO'],
                },
                onEnter: ['$stateParams', '$state', '$modal', 'SolicitaRemocaoComentario', function ($stateParams, $state, $modal, SolicitaRemocaoComentario) {
                    $modal.open({
                        templateUrl: 'scripts/app/main/solicitacao-aprovacao.html',
                        controller: 'SolicitacaoAprovacaoController',
                        size: 'lg',
                        resolve: {
                            entity: ['SolicitaRemocaoComentario', function (SolicitaRemocaoComentario) {
                                return SolicitaRemocaoComentario.get({id: $stateParams.id});
                            }]
                        }
                    }).result.then(function (result) {
                        $state.go('^', null, {reload: true});
                    }, function () {
                        $state.go('^');
                    })
                }]
            })
            .state('reprovacao', {
                parent: 'home',
                url: 'solicitacao/{id}/reprovacao',
                data: {
                    authorities: ['ROLE_ADMIN', 'ROLE_ADMIN_CONDOMINIO'],
                },
                onEnter: ['$stateParams', '$state', '$modal', 'SolicitaRemocaoComentario', function ($stateParams, $state, $modal, SolicitaRemocaoComentario) {
                    $modal.open({
                        templateUrl: 'scripts/app/main/solicitacao-reprovacao.html',
                        controller: 'SolicitacaoAprovacaoController',
                        size: 'lg',
                        resolve: {
                            entity: ['SolicitaRemocaoComentario', function (SolicitaRemocaoComentario) {
                                return SolicitaRemocaoComentario.get({id: $stateParams.id});
                            }]
                        }
                    }).result.then(function (result) {
                        $state.go('^', null, {reload: true});
                    }, function () {
                        $state.go('^');
                    })
                }]
            })
            .state('avaliaCompetencia.aprovacao', {
                parent: 'home',
                url: 'avaliacao/{id}/aprovacao',
                data: {
                    authorities: ['ROLE_ADMIN', 'ROLE_ADMIN_CONDOMINIO'],
                },
                onEnter: ['$stateParams', '$state', '$modal', 'AvaliaCompetencia', function ($stateParams, $state, $modal, AvaliaCompetencia) {
                    $modal.open({
                        templateUrl: 'scripts/app/main/avaliacao-aprovacao.html',
                        controller: 'AvaliacaoAprovacaoController',
                        size: 'lg',
                        resolve: {
                            entity: ['AvaliaCompetencia', function (AvaliaCompetencia) {
                                return AvaliaCompetencia.get({id: $stateParams.id});
                            }]
                        }
                    }).result.then(function (result) {
                        $state.go('^', null, {reload: true});
                    }, function () {
                        $state.go('^');
                    })
                }]
            })
            .state('avaliaCompetencia.reprovacao', {
                parent: 'home',
                url: 'avaliacao/{id}/reprovacao',
                data: {
                    authorities: ['ROLE_ADMIN', 'ROLE_ADMIN_CONDOMINIO'],
                },
                onEnter: ['$stateParams', '$state', '$modal', 'AvaliaCompetencia', function ($stateParams, $state, $modal, AvaliaCompetencia) {
                    $modal.open({
                        templateUrl: 'scripts/app/main/avaliacao-reprovacao.html',
                        controller: 'AvaliacaoAprovacaoController',
                        size: 'lg',
                        resolve: {
                            entity: ['AvaliaCompetencia', function (AvaliaCompetencia) {
                                return AvaliaCompetencia.get({id: $stateParams.id});
                            }]
                        }
                    }).result.then(function (result) {
                        $state.go('^', null, {reload: true});
                    }, function () {
                        $state.go('^');
                    })
                }]
            });
    });
