'use strict';

angular.module('tmcApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('register', {
                parent: 'account',
                url: '/register',
                data: {
                    authorities: [],
                    pageTitle: 'register.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/account/register/register.html',
                        controller: 'RegisterController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('register');
                        return $translate.refresh();
                    }]
                }
            })
            .state('register.morador', {
                parent: 'account',
                url: '/register/morador/{id}',
                data: {
                    authorities: [],
                    pageTitle: 'register.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/account/register/register-morador.html',
                        controller: 'MoradorNewController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('register');
                        $translatePartialLoader.addPart('morador');
                        $translatePartialLoader.addPart('sexo');
                        $translatePartialLoader.addPart('tipoMorador');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }],
                    entity: function () {
                        return {
                            nome: null,
                            cpf: null,
                            sexo: null,
                            email: null,
                            senha: null,
                            dataNascimento: null,
                            ativo: null,
                            bloqueiaAgendamento: null,
                            tipo: null,
                            id: null,
                            condominioId: null
                        };
                    }
                }
            });
    });
