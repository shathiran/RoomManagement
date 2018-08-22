angular.module('roomApp')

	.factory('MemberList', function($resource) {
		return $resource("/member", {}, {
			getAll : {method : 'GET'}
		});
	})
	.factory('MemberFactory', function($resource) {
		return $resource("/member/:id", {}, {
			get : { method : 'GET',  params : {}, isArray : false },
			query : { method : 'GET',  params : { id : '' }, isArray : false },
			save : { method : 'POST',  params : {}, isArray : false },
			update : { method : 'PUT',  params : {}, isArray : false }
		});
	})
	
	.controller('MemberController',function($rootScope,$state,$scope,$localStorage,$timeout,$filter,growl,MemberList,ngTableParams){
		
		$scope.pagination = {};
		$localStorage.memberId = null;
		
		if($localStorage.action == null || $localStorage.action == undefined)
			$scope.action = "Add";
		else
			$scope.action = $localStorage.action;
		
		$localStorage.memberId = null;
		
		$scope.memberId = $localStorage.memberId;
		
		$scope.tableParams=new ngTableParams({
			page:1 , count:10,
		},{
			getData:function($defer,params){
				$scope.pagination.page = params.page() -1;
		    	$scope.pagination.size = params.count();
		    	MemberList.getAll().$promise.then(function(data){
					$scope.serialNumber=$scope.pagination.page*$scope.pagination.size;
					$scope.data = params.sorting() ? $filter('orderBy')(data.content, params.orderBy()) : data.content;
					params.total(data.totalElements);
	                $defer.resolve($scope.data);
				},function(error){
					console.log("member pagination error");
				});
			}
		});
		
		$scope.selectMember = function(memberId){
			$localStorage.memberId = memberId;
			$scope.memberId = $localStorage.memberId;
		}
		
		$scope.add = function(){
			$localStorage.action = "Add";
			$localStorage.memberId = null;
			$state.go('room.members-form');
		}

		$scope.edit = function(){
			if($localStorage.memberId == null || $localStorage.memberId == undefined){
				$timeout(function(){ growl.warning("Please select Member"); },100);
				return null;
			}
			$localStorage.action = "Edit";
			$state.go('room.members-form');
		}

		$scope.view = function(branch){
			if($localStorage.memberId == null || $localStorage.memberId == undefined){
				$timeout(function(){ growl.warning("Please select Member"); }, 100);
				return null;				
			}
			$localStorage.action = "View";
			$state.go('room.members-form');
		}
	})
	
	.controller('MemberFormController',function($rootScope,$state,$scope,$localStorage,growl,$timeout,MemberFactory){
		
		$scope.action = $localStorage.action;
		$scope.member = {};
		
		if($localStorage.memberId != null && $localStorage.memberId != undefined){
			MemberFactory.query({id:$localStorage.memberId},function(data){
				$scope.member = data.content;
			});
		}
		
		$scope.submit = function(){
			
			$scope.member.dateOfBirth = $scope.member.dateOfBirth + " 00:00:00";
			$scope.member.joiningDate = $scope.member.joiningDate + " 00:00:00";
			
			if($localStorage.action == "Add"){
				MemberFactory.save($scope.member).$promise.then(function(data){
					if(data.status){
						$timeout(function(){ growl.success(data.description); }, 300);
						$state.go('room.members');
					}
					else{
						$timeout(function(){ growl.error(data.description); }, 100);
					}
				},function(error){
					$timeout(function(){ growl.error(error.data.message); }, 100);
					console.log("Error",error.data.message);
				});
			}
			else if($localStorage.action == "Edit"){
				MemberFactory.update($scope.member).$promise.then(function(data){
					if(data.status){
						$timeout(function(){ growl.success(data.description); }, 300);
						$state.go('room.members');
					}
					else{
						$timeout(function(){ growl.error(data.description); }, 100);
					}
				},function(error){
					$timeout(function(){ growl.success(error.data.description); }, 100);
					console.log("Error",error.data.message);
				});
			}
		}
	});