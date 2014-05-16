'use strict';

paymeLaterApp
    .config(['$routeProvider', '$httpProvider', '$translateProvider', 'USER_ROLES',
        function ($routeProvider, $httpProvider, $translateProvider,USER_ROLES) {
            $routeProvider
                .when('/debts', {
                    templateUrl: 'views/debts.html',
                    controller: 'DebtsController',
                    resolve:{
                        resolvedPayment: ['Debt', function (Debt) {
                            return Debt.query();
                        }]
                    },
                    access: {
                        authorizedRoles: [USER_ROLES.all]
                    }
                })
        }]);
