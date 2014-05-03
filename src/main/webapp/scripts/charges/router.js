'use strict';

paymeLaterApp
    .config(['$routeProvider', '$httpProvider', '$translateProvider', 'USER_ROLES',
        function ($routeProvider, $httpProvider, $translateProvider,USER_ROLES) {
            $routeProvider
                .when('/charges', {
                    templateUrl: 'views/charges.html',
                    controller: 'ChargesController',
                    resolve:{
                        resolvedPayment: ['Charge', function (Charge) {
                            return Charge.query();
                        }]
                    },
                    access: {
                        authorizedRoles: [USER_ROLES.all]
                    }
                })
        }]);
