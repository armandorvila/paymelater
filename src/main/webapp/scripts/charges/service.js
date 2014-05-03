'use strict';

paymeLaterApp.factory('Charge', ['$resource',
    function ($resource) {
        return $resource('app/rest/payments/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': { method: 'GET'},
            'save': { method: 'POST'}
        });
    }]);
