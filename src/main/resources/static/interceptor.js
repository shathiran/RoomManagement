app.factory('httpInterceptor', function($rootScope,$q,$rootScope,$injector) {
	
	return {
		request : function(config) {
			return config || $q.when(config);
		},
		response : function(response) {
			return response || $q.when(response);
		},
		responseError : function(response) {
			if(response.status === 401){
				console.log("unauthorized");
				$injector.get('$state').transitionTo('login');
			}
			return $q.reject(response);
		}
	};
});