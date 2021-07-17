// Controller for Add New Pet functionality
angular.module('petStoreDemoApp').controller('ModalInstanceCtrl', 
  function ($scope, $uibModalInstance) {
    $scope.pet = new Pet();
    
    // Regex for Pet Name
    $scope.name_regex = "[A-Z]{1}[a-z0-9 ]+";
  
    $scope.ok = function () {
      $uibModalInstance.close($scope.pet);
    };
  
    $scope.cancel = function () {
      $uibModalInstance.dismiss('cancel');
    };
});
