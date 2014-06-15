'use strict';

paymeLaterApp.controller('DebtsController', ['$http','$location','$scope', '$modal', 'resolvedPayment', 'Debt', 'PayPal',
    function ($http,$location,$scope,$modal, resolvedPayment, Debt, PayPal, Card ) {

        $scope.payments = resolvedPayment;

        $scope.payWithPayPal = function (id) {
        	$scope.paypal = undefined;
        	       	
        	$('#paypalConfirmationModal').modal('show');
        	
        	$http.get('app/rest/debts/payPal/' + id).then(function(res) {
                $scope.paypal = res.data;
            });
        	
        };
        
        $scope.payWithCard = function (id) {
        	$location.url('/app/rest/debts/card/' + id);
        };
        
   
        
    }]);



