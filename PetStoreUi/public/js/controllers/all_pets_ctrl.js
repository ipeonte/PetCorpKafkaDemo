angular.module('AddPetServices', ['ngResource'])
  .factory('add_pet', ['$resource',
    function($resource) {
        return $resource('api/v1/admin/pet', {
          pet : '@pet'
        }, {
          save : {
            method : 'POST'
          }
        });
      }])
      

angular.module('BasicPetServices', ['ngResource'])
  .factory('pets', ['$resource',
    function($resource){
      return $resource('api/v1/pet/:petId', {petId:'@id'}, {
        find: {method: 'GET', params: {petId:'@id'}}
      });
    }])
  .factory('admin_pets', ['$resource',
    function($resource) {
      return $resource('api/v1/admin/pet/:petId', {petId:'@id'}, {
        find: {method: 'GET', params: {petId:'@id'}}
      });
    }])
  .factory('mgr_pets', ['$resource',
    function($resource) {
      return $resource('api/v1/mgr/pet/:petId/:clientId', {petId:'@id', clientId: '@clientId'}, {
        adopt: {method: 'DELETE', params: {petId:'@id', clientId: '@clientId'}}
      });
    }]);

// Create main controller
angular.module('petStoreDemoApp', ['AddPetServices', 'BasicPetServices', 'ui.bootstrap']).controller('allPetsCtrl',
      function($scope, $http, $uibModal, $log, add_pet, pets, admin_pets, mgr_pets) {

  // Regex for Pet Name
  $scope.id_regex = "[0-9]+";
    
  // Number of selected pets
  $scope.selected_cnt = 0;

  // Map of selected only pet in format pet.id => pet
  $scope.selected = {};

  // Status of each selected pet in format pet.id => true/false
  $scope.checked = {};

  // Selected client
  $scope.client = {};
  
  $scope.$on('userMask', function (event, data) {
    $scope.user_mask = data.user_mask;
    $scope.url_prefix = data.user_mask == 0 ? "" : "/admin";

    $scope.reset();
  });

  // Get URL prefix from mask
  $scope.get_mask_url = function() {
    return $scope.url_prefix;
  };

  // Visibility function for buttons since AngularJs doesn't support bitwise operators
  // Must be the first one
  $scope.is_visible = function(mask) {
    return $scope.user_mask !== undefined 
      ? ($scope.user_mask & mask) > 0
      : false;
  };

  $scope.reset_selection = function() {
    $scope.selected = {};
    $scope.checked = [];
    $scope.selected_cnt = 0;
  };
  
  $scope.find = function() {
    var service = $scope.user_mask == 0 ? pets : admin_pets;

    service.find({petId: $scope.pet_id}, function(pet) {
      $scope.all_pets = new Array(pet);
      $scope.reset_selection();
    }).$promise.then(function(data) {
      console.log(data);
    });;
  };

  $scope.reset = function() {
    $http.get("api/v1" + $scope.url_prefix + "/pets").then(function(res) {
      $scope.all_pets = res.data;
    }, function() {});

    $http.get("api/v1/clients").then(function(res) {
      $scope.clients = res.data;

      // Index clients by id
      $scope.client_map = {};
      for (idx in res.data)
        $scope.client_map[res.data[idx].id] = res.data[idx];
    }, function() {});
  };
  
  $scope.toggle_selection = function(pet) {
    if ($scope.checked[pet.id]) {
      $scope.selected[pet.id] = pet;
      $scope.selected_cnt++;
    } else {
      delete $scope.selected[pet.id];
      $scope.selected_cnt--;
    }
  };
  
  $scope.use_client = function(client_id) {
    $scope.client_id = client_id;
  };

  $scope.adopt_pets = function() {
    var client = $scope.client_map[$scope.client.id];
    if (!confirm("Please confirm adoption of " +
        $scope.selected_cnt + " pet" + ($scope.selected_cnt > 1 ? "s" : "") +
          " by " + client.first_name + " " + client.last_name))
      return;
    
    // Collect pet id's
    for (key in $scope.selected) {
      var pet = $scope.selected[key];
      
      (function(pet, client){
        mgr_pets.adopt({petId: pet.id, clientId: client.id}, function() {
          var idx = $scope.all_pets.indexOf(pet);
        
          $scope.all_pets.splice(idx, 1);
          delete $scope.selected[pet.id];
          $scope.checked[pet.id] = false;
          $scope.selected_cnt--;

          if ($scope.selected_cnt == 0)
            // Reset client selection
            $scope.client.id = null;
        });
      })(pet, $scope.client);
    }
  };

  $scope.add_pet_form = function(pet) {
    var modalInstance = $uibModal.open({
      animation : false,
      templateUrl : 'new_pet_form.html',
      controller : 'ModalInstanceCtrl',
      resolve : {
        new_pet : pet,
      }
    });

    modalInstance.result.then(function(pet) {
      // Finally adding a pet
      admin_pets.save(pet, function(data) {
        pet = data;
        $scope.all_pets.push(pet);
        $log.info('Pet ' + pet.name + ' saved at: ' + new Date() + " with id: " + pet.id);
      });

      $log.debug('Pet ' + pet.name + ' sent at: ' + new Date());
    }, function() {
      $log.debug('Modal dismissed at: ' + new Date());
    });
  };
});
