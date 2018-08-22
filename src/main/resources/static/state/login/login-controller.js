angular.module('roomApp')
	.factory('Login', function($resource) {
		return $resource("/auth/login", {}, {
			login : {method : 'POST'}
		});
	})
	.controller('LoginController',function($rootScope,$scope,$state,Login,$localStorage,Login,$rootScope,$timeout,growl,$http){

		$scope.credential = {};
		
		$scope.login = function() {
			
			Login.login($scope.credential).$promise.then(function(response){
				if(response.status){
					$timeout(function(){ growl.success(response.description); }, 300);
					$localStorage.loggedIn = true;
					$localStorage.user = JSON.stringify(response.content.member);
					$rootScope.user = response.content.member;
					$state.go("room.transactions");
				}
				else{
					$timeout(function(){ growl.error("Please check your Email or Password!"); }, 100);
					$localStorage.loggedIn = false;
					console.log(response.description);
				}
			},
			function(error){
				$timeout(function(){ growl.error("Please check your Email or Password!"); }, 100);
			});
		}
		
		$scope.logout=function(){
			
			$http({method: 'GET', url:'/auth/logout'}).then(function successCallback(response) {
				$localStorage.loggedIn=false;
				$rootScope.user = {};
				$localStorage.user = null;
				$localStorage.loggedIn = false;
				$state.go('login');
			}, function errorCallback(response) {
				console.log("failed ");
			});
		}
	});