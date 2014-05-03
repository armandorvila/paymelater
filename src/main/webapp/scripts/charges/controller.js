'use strict';

paymeLaterApp.controller('ChargesController', ['$http','$scope', 'resolvedPayment', 'Charge',
    function ($http,$scope, resolvedPayment, Charge) {

        $scope.payments = resolvedPayment;

        $scope.create = function () {
        	Charge.save(JSON.stringify($scope.payment),
                function () {
                    $scope.payments = Charge.query();
                    $('#savePaymentModal').modal('hide');
                    $scope.clear();
                });
        };

        $scope.update = function (id) {
            $scope.payment = Charge.get({id: id});
            $('#savePaymentModal').modal('show');
        };

        $scope.delete = function (id) {
        	Charge.delete({id: id},
                function () {
                    $scope.payments = Charge.query();
                });
        };

        $scope.clear = function () {
            $scope.payment = { subject: "sdsdsdsds", description: "ssdsdsdsdsdsdsdsdsds", deadLine: "", amount: "2.0", lender: "admin" , borrower: "user"};
        };
        
        $scope.getLocations = function(val) {
        	 return $http.get('http://maps.googleapis.com/maps/api/geocode/json', {
                 params : {
                     address : val,
                     sensor : false
                 }
             }).then(function(res) {
                 return res.data.results;
             });
        };
    }]);
