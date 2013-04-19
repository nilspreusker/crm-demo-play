'use strict';


// Declare app level module which depends on filters, services and directives
angular.module('CrmDemo', [ 'CrmDemo.filters', 'CrmDemo.services', 'CrmDemo.directives' ])
    .config([ '$routeProvider', function ($routeProvider) {
        $routeProvider.when('/list', {
            templateUrl: 'assets/app/partials/customer-list.html',
            controller: CustomerListCtrl
        });
        $routeProvider.when('/edit/:customerId', {
            templateUrl: 'assets/app/partials/customer-detail.html',
            controller: CustomerDetailCtrl
        });
        $routeProvider.when('/new', {
            templateUrl: 'assets/app/partials/customer-detail.html',
            controller: CustomerDetailCtrl
        });
        $routeProvider.otherwise({
            redirectTo: '/list'
        });
    } ]);
