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
            .state('home.topico', {
                parent: 'home',
                url: 'topico/{id}/aprovacao',
                data: {
                    authorities: ['ROLE_ADMIN', 'ROLE_ADMIN_CONDOMINIO'],
                },
                onEnter: ['$stateParams', '$state', '$modal', 'Topico', function ($stateParams, $state, $modal, Topico) {
                    $modal.open({
                        templateUrl: 'scripts/app/main/topico-aprovacao.html',
                        controller: 'TopicoAprovarController',
                        size: 'lg',
                        resolve: {
                            entity: ['Topico', function (Topico) {
                                return Topico.get({id: $stateParams.id});
                            }]
                        }
                    }).result.then(function (result) {
                        $state.go('topico', null, {reload: true});
                    }, function () {
                        $state.go('^');
                    })
                }]
            });
    });
