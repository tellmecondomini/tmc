'use strict';

angular.module('tmcApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('imovel', {
                parent: 'entity',
                url: '/imovels',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'tmcApp.imovel.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/imovel/imovels.html',
                        controller: 'ImovelController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('imovel');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('imovel.detail', {
                parent: 'entity',
                url: '/imovel/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'tmcApp.imovel.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/imovel/imovel-detail.html',
                        controller: 'ImovelDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('imovel');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'Imovel', function($stateParams, Imovel) {
                        return Imovel.get({id : $stateParams.id});
                    }]
                }
            })
            .state('imovel.new', {
                parent: 'imovel',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/imovel/imovel-dialog.html',
                        controller: 'ImovelDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {ruaBloco: null, numero: null, id: null};
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('imovel', null, { reload: true });
                    }, function() {
                        $state.go('imovel');
                    })
                }]
            })
            .state('imovel.edit', {
                parent: 'imovel',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/imovel/imovel-dialog.html',
                        controller: 'ImovelDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Imovel', function(Imovel) {
                                return Imovel.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('imovel', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
