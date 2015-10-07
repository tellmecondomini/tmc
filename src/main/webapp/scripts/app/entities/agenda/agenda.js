'use strict';

angular.module('tmcApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('agenda', {
                parent: 'entity',
                url: '/agendas',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'tmcApp.agenda.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/agenda/agendas.html',
                        controller: 'AgendaController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('agenda');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('agenda.detail', {
                parent: 'entity',
                url: '/agenda/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'tmcApp.agenda.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/agenda/agenda-detail.html',
                        controller: 'AgendaDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('agenda');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'Agenda', function($stateParams, Agenda) {
                        return Agenda.get({id : $stateParams.id});
                    }]
                }
            })
            .state('agenda.new', {
                parent: 'agenda',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/agenda/agenda-dialog.html',
                        controller: 'AgendaDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {data: null, horaInicio: null, horaFim: null, id: null};
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('agenda', null, { reload: true });
                    }, function() {
                        $state.go('agenda');
                    })
                }]
            })
            .state('agenda.edit', {
                parent: 'agenda',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/agenda/agenda-dialog.html',
                        controller: 'AgendaDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Agenda', function(Agenda) {
                                return Agenda.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('agenda', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
