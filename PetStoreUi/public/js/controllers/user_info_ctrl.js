// Create main controller
angular.module('petStoreDemoApp').controller('userInfoCtrl',
      function($scope, $http, $log, $window) {

  // Bootstrap page
  $http.get("api/v1/user_info").then(function(res) {
    $scope.user_info = {
      mask: parseInt(res.data.user_info.mask,2),
      name: res.data.user_info.name
    };
    
    $scope.breed_info_url = res.breed_info_url;

    $scope.$parent.$broadcast('userMask', {
      user_mask: $scope.user_info.mask
    });

    $log.debug("User Mask: " + $scope.user_info.mask);
  }, function(error) {
    
    if (error.status == 401 || error.status == 403) {
      // redirect on login page
      console.log(error);
      $window.location.href = '/login';
    }
  });
});
