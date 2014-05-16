'use strict';

paymeLaterApp.controller('DebtsController', ['$location','$scope', 'resolvedPayment', 'Debt', 'PayPal',
    function ($location,$scope, resolvedPayment, Debt, PayPal, Card) {

        $scope.payments = resolvedPayment;

        $scope.payWithPayPal = function (id) {
        	PayPal.pay({id: id},
                function () {
                    $scope.payments = Debt.query();
                });
        };
        
        $scope.payWithCard = function (id) {
        	$location.url('/app/rest/debts/card/' + id);
        };
        
    }]);
