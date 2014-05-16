'use strict';

paymeLaterApp.controller('ChargesController', ['$http','$scope', 'resolvedPayment', 'Charge',
    function ($http,$scope, resolvedPayment, Charge) {

        $scope.payments = resolvedPayment;

        $scope.create = function () {
        	$scope.payment.borrower = $scope.payment.borrower.login;
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
            $scope.payment = { subject: "", description: "", deadLine: "", amount: "" , borrower: ""};
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
        
        $scope.getBorrowers = function(val) {
       	 return $http.get('/app/rest/users/find', {
                params : {
                    name : val
                }
            }).then(function(res) {
                return res.data;
            });
       };
    }]);
