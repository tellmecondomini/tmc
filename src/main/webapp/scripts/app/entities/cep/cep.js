'use strict';

angular.module('tmcApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('cep', {
                parent: 'entity',
                url: '/ceps',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'tmcApp.cep.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/cep/ceps.html',
                        controller: 'CepController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('cep');
                        $translatePartialLoader.addPart('uf');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('cep.detail', {
                parent: 'entity',
                url: '/cep/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'tmcApp.cep.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/cep/cep-detail.html',
                        controller: 'CepDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('cep');
                        $translatePartialLoader.addPart('uf');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'Cep', function($stateParams, Cep) {
                        return Cep.get({id : $stateParams.id});
                    }]
                }
            })
            .state('cep.new', {
                parent: 'cep',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/cep/cep-dialog.html',
                        controller: 'CepDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {logradouro: null, bairro: null, cidade: null, uf: null, cep: null, id: null};
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('cep', null, { reload: true });
                    }, function() {
                        $state.go('cep');
                    })
                }]
            })
            .state('cep.edit', {
                parent: 'cep',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/cep/cep-dialog.html',
                        controller: 'CepDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Cep', function(Cep) {
                                return Cep.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('cep', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
