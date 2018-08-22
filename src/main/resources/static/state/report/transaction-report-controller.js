angular.module('roomApp')

	.factory('TransactionReportFactory', function($resource) {
		return $resource("/transaction/getReport", {}, {
			getReport : { method : 'POST',  params : {}, isArray : false }
		});
	})
	.factory('MembersNameList', function($resource) {
		return $resource("/member/names", {}, {
			query : {method : 'GET'}
		});
	})

	.controller('TransactionReportController',function($rootScope,$state,$scope,$localStorage,growl,$timeout,TransactionReportFactory,MembersNameList){
		
		$scope.transaction = {};
		$scope.memberNames = [];
		$scope.reportTransactions = [];
		$scope.reportDeposits = [];
		$scope.reportSummary = [];
		$scope.report = {};
		$scope.rent = 0;
		$scope.water = 0;
		$scope.electricity = 0;
		
		MembersNameList.query(function(data){
			console.log("data.content ",data.content);
			$scope.memberNames = data.content;
		});
		
		$scope.submit = function(){
			
			$scope.report.fromDate = $scope.report.fromDateUi + " 00:00:00";
			$scope.report.toDate = $scope.report.toDateUi + " 00:00:00";
			
			TransactionReportFactory.getReport($scope.report).$promise.then(function(data){
				if(data.status){
					$scope.reportTransactions = data.content.transactions;
					$scope.reportDeposits = data.content.deposits;
					$scope.reportSummary = data.content.summaryList;
				}
				else{
					$scope.reportTransactions = [];
					$scope.reportDeposits = [];
					$scope.reportSummary = [];
				}
			},function(error){
				$timeout(function(){ growl.error(error.data.message); }, 100);
				console.log("Error",error.data.message);
			});
		}
	});


