'use strict';

angular.module('tmcApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('endereco', {
                parent: 'entity',
                url: '/enderecos',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'tmcApp.endereco.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/endereco/enderecos.html',
                        controller: 'EnderecoController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('endereco');
                        $translatePartialLoader.addPart('uf');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('endereco.detail', {
                parent: 'entity',
                url: '/endereco/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'tmcApp.endereco.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/endereco/endereco-detail.html',
                        controller: 'EnderecoDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('endereco');
                        $translatePartialLoader.addPart('uf');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'Endereco', function($stateParams, Endereco) {
                        return Endereco.get({id : $stateParams.id});
                    }]
                }
            })
            .state('endereco.new', {
                parent: 'endereco',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/endereco/endereco-dialog.html',
                        controller: 'EnderecoDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {logradouro: null, numero: null, bairro: null, cidade: null, uf: null, id: null};
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('endereco', null, { reload: true });
                    }, function() {
                        $state.go('endereco');
                    })
                }]
            })
            .state('endereco.edit', {
                parent: 'endereco',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/endereco/endereco-dialog.html',
                        controller: 'EnderecoDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Endereco', function(Endereco) {
                                return Endereco.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('endereco', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
