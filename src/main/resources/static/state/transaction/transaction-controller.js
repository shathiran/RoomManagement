angular.module('roomApp')

	.factory('TransactionList', function($resource) {
		return $resource("/transaction", {}, {
			getAll : {method : 'GET'}
		});
	})
	.factory('ActiveMemberList', function($resource) {
		return $resource("/member/status/:status", {}, {
			query : {method : 'GET'}
		});
	})
	.factory('TransactionFactory', function($resource) {
		return $resource("/transaction/:id", {}, {
			get : { method : 'GET',  params : {}, isArray : false },
			query : { method : 'GET',  params : { id : '' }, isArray : false },
			save : { method : 'POST',  params : {}, isArray : false },
			update : { method : 'PUT',  params : {}, isArray : false }
		});
	})
	
	.controller('TransactionController',function($rootScope,$state,$scope,$localStorage,$timeout,$filter,growl,TransactionList,ngTableParams){
		
		$scope.pagination = {};
		$localStorage.transactionId = null;
		
		if($localStorage.action == null || $localStorage.action == undefined)
			$scope.action = "Add";
		else
			$scope.action = $localStorage.action;
		
		$localStorage.transactionId = null;
		
		$scope.transactionId = $localStorage.transactionId;
		
		$scope.tableParams=new ngTableParams({
			page:1 , count:10,
		},{
			getData:function($defer,params){
				$scope.pagination.page = params.page() -1;
		    	$scope.pagination.size = params.count();
		    	TransactionList.getAll().$promise.then(function(data){
					$scope.serialNumber=$scope.pagination.page*$scope.pagination.size;
					$scope.data = params.sorting() ? $filter('orderBy')(data.content, params.orderBy()) : data.content;
					params.total(data.totalElements);
	                $defer.resolve($scope.data);
				},function(error){
					console.log("Transaction pagination error");
				});
			}
		});
		
		$scope.selectTransaction = function(transactionId){
			$localStorage.transactionId = transactionId;
			$scope.transactionId = $localStorage.transactionId;
		}
		
		$scope.add = function(){
			$localStorage.action = "Add";
			$localStorage.transactionId = null;
			$state.go('room.transactions-form');
		}

		$scope.edit = function(){
			if($localStorage.transactionId == null || $localStorage.transactionId == undefined){
				$timeout(function(){ growl.warning("Please select Transaction"); },100);
				return null;
			}
			$localStorage.action = "Edit";
			$state.go('room.transactions-form');
		}

		$scope.view = function(branch){
			if($localStorage.transactionId == null || $localStorage.transactionId == undefined){
				$timeout(function(){ growl.warning("Please select Transaction"); }, 100);
				return null;				
			}
			$localStorage.action = "View";
			$state.go('room.transactions-form');
		}
	})
	
	.controller('TransactionFormController',function($rootScope,$state,$scope,$localStorage,growl,$timeout,TransactionFactory,ActiveMemberList){
		
		$scope.action = $localStorage.action;
		$scope.transaction = {};
		$scope.members = [];
		$scope.participatedMembers = [];

		ActiveMemberList.query({status:"ACTIVE"},function(data){
			console.log("data.content ",data.content);
			$scope.members = data.content;
			membersWithoutRoom();
		});
		
		if($localStorage.transactionId != null && $localStorage.transactionId != undefined){
			TransactionFactory.query({id:$localStorage.transactionId},function(data){
				$scope.transaction = data.content;
				$scope.participatedMembers = data.content.participatedMembers;
			});
		}
		
		$scope.removeMember = function(memberId){
			var index = -1;
			for(var i=0;i<=$scope.participatedMembers.length;i++){
				if($scope.participatedMembers[i].id === memberId){
					index = i;
					break;
				}
			}
			if(index != -1){
				$scope.participatedMembers.splice(index,1);
			}
		}
		
		$scope.showAllMembers = function(){
			membersWithoutRoom();
		}
		
		function membersWithoutRoom() {
			$scope.participatedMembers = [];
			for(var i=0;i<=$scope.members.length;i++){
				if($scope.members[i].createdBy === null){
					continue;
				}
				$scope.participatedMembers.push($scope.members[i]);
			}
		}
		
		$scope.submit = function(){
			
			$scope.transaction.date = $scope.transaction.date + " 00:00:00";
			$scope.transaction.participatedMembers = $scope.participatedMembers;
			if($localStorage.action == "Add"){
				TransactionFactory.save($scope.transaction).$promise.then(function(data){
					if(data.status){
						$timeout(function(){ growl.success(data.description); }, 300);
						$state.go('room.transactions');
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
				TransactionFactory.update($scope.transaction).$promise.then(function(data){
					if(data.status){
						$timeout(function(){ growl.success(data.description); }, 300);
						$state.go('room.transactions');
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