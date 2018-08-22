var app = angular.module('roomApp',['ui.router','ngResource','ngCookies','ngStorage','angular-growl','ngTable','angular-loading-bar']);

	app.config(function($stateProvider, $urlRouterProvider, $httpProvider,growlProvider,cfpLoadingBarProvider) {
		
		cfpLoadingBarProvider.parentSelector = '#loading-bar-container';
	    cfpLoadingBarProvider.spinnerTemplate = '<div class="loading1"><span class="glyph-icon remove-border demo-icon tooltip-button icon-spin-4 icon-spin load2"></span></div>';
		growlProvider.globalTimeToLive(2000);
		growlProvider.globalReversedOrder(true);
		
		$httpProvider.interceptors.push('httpInterceptor');
		$urlRouterProvider.otherwise('/login');
	
		$stateProvider
			.state('login', {
				name : 'login',
				url : '/login',
				controller : 'LoginController',
				templateUrl : 'state/login/login.html'
			})
			.state('room', {
				name : 'room',
				url : '/room',
				templateUrl : 'state/layout.html'
			})
			.state('room.members', {
				name : 'members',
				url : '/members',
				controller : 'MemberController',
				templateUrl : 'state/member/members.html'
			})
			.state('room.members-form', {
				name : 'members-form',
				url : '/members-form',
				controller : 'MemberFormController',
				templateUrl : 'state/member/member-form.html'
			})
			.state('room.transactions', {
				name : 'transactions',
				url : '/transactions',
				controller : 'TransactionController',
				templateUrl : 'state/transaction/transactions.html'
			})	
			.state('room.transactions-form', {
				name : 'transactions-form',
				url : '/transactions-form',
				controller : 'TransactionFormController',
				templateUrl : 'state/transaction/transaction-form.html'
			})
			.state('room.transaction-report', {
				name : 'transaction-report',
				url : '/transaction-report',
				controller : 'TransactionReportController',
				templateUrl : 'state/report/transaction-report.html'
			});
	});
	
	app.run(function($rootScope,$localStorage,$state){
		if($localStorage.loggedIn == null || $localStorage.loggedIn == undefined || $localStorage.loggedIn == false){
			$state.go("login");
		}
		
		console.log("$localStorage.user : ",$localStorage.user);
		
		if($localStorage.user == undefined || $localStorage.user == null){
			console.log("here");
			$state.go("login");
		}else{
			$rootScope.user = JSON.parse($localStorage.user);
		}
		$rootScope.loggedIn = $localStorage.loggedIn;
	});
	
	app.factory('Logout', function($resource) {
		return $resource("/auth/logout", {}, {
			logout : {method : 'GET'}
		});
	});
	
	app.factory('ChangePassword', function($resource) {
		return $resource("/member/changePassword", {}, {
			post : { method : 'POST',  params : {}, isArray : false }
		});
	});
	
	app.controller('MainController',function($rootScope,$scope,Logout,ChangePassword,$state,$localStorage,$timeout,growl){

		if($localStorage.user != null || $localStorage.user != undefined)
			$scope.user = JSON.parse($localStorage.user);

		$scope.passwordObj = {};
		
		$scope.changePassword = function() {
			if($scope.passwordObj.newPassword === $scope.passwordObj.confirmNewPassword){
				$scope.passwordObj.memberId = $scope.user.id;
				ChangePassword.post($scope.passwordObj).$promise.then(function(data){
					if(data.status){
						$timeout(function(){ growl.success(data.description); }, 100);
						$("#myModal").modal("hide");
						$("body").removeClass("modal-open");
						$(".modal-backdrop").remove();						
					}
					else{
						$timeout(function(){ growl.error(data.description); }, 100);
						return;
					}
				},function(error){
					$timeout(function(){ growl.warning("Something went wrong..!!! Try again later"); }, 100);
					return null;
				});
			}else{
				$timeout(function(){ growl.warning("New password and Confirm password mismatched"); }, 100);
				return null;
			}
		}

		$scope.logout = function() {
			console.log("logout");
			$localStorage.loggedIn = false;
			$localStorage.user = null;
			Logout.logout(function(data){
				$state.go("login");
			});
		}	
	});