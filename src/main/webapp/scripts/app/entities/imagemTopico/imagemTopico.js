'use strict';

angular.module('tmcApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('imagemTopico', {
                parent: 'entity',
                url: '/imagemTopicos',
                data: {
                    authorities: ['ROLE_USER','ROLE_ADMIN'],
                    pageTitle: 'tmcApp.imagemTopico.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/imagemTopico/imagemTopicos.html',
                        controller: 'ImagemTopicoController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('imagemTopico');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('imagemTopico.detail', {
                parent: 'entity',
                url: '/imagemTopico/{id}',
                data: {
                    authorities: ['ROLE_USER','ROLE_ADMIN'],
                    pageTitle: 'tmcApp.imagemTopico.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/imagemTopico/imagemTopico-detail.html',
                        controller: 'ImagemTopicoDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('imagemTopico');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'ImagemTopico', function($stateParams, ImagemTopico) {
                        return ImagemTopico.get({id : $stateParams.id});
                    }]
                }
            })
            .state('imagemTopico.new', {
                parent: 'imagemTopico',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER','ROLE_ADMIN'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/imagemTopico/imagemTopico-dialog.html',
                        controller: 'ImagemTopicoDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {imagem: null, id: null};
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('imagemTopico', null, { reload: true });
                    }, function() {
                        $state.go('imagemTopico');
                    })
                }]
            })
            .state('imagemTopico.edit', {
                parent: 'imagemTopico',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER','ROLE_ADMIN'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/imagemTopico/imagemTopico-dialog.html',
                        controller: 'ImagemTopicoDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['ImagemTopico', function(ImagemTopico) {
                                return ImagemTopico.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('imagemTopico', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
