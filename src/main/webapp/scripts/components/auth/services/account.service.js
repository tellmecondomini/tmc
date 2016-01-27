'use strict';

angular.module('tmcApp')
    .factory('Account', function Account($resource) {
        return $resource('api/account', {}, {
            'get': {
                method: 'GET', params: {}, isArray: false,
                interceptor: {
                    response: function (response) {
                        // expose response
                        return response;
                    }
                }
            }
        });
    })
    .factory('AccountMorador', function Account($resource, DateUtils) {
        return $resource('api/account/morador', {}, {
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    data.dataNascimento = DateUtils.convertDateTimeFromServer(data.dataNascimento);
                    return data;
                }
            }
        });
    });
