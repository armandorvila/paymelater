'use strict';

paymeLaterApp.factory('Debt', ['$resource',
    function ($resource) {
        return $resource('app/rest/debts/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': { method: 'GET'},
            'save': { method: 'POST'}
        });
    }]);
