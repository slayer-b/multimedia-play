app = angular.module('myApp', [])

app.controller 'TestTable', ($scope, $http) ->
    console.log 'Initialize TestTable'
    
    $scope.data = []
    
    $scope.refresh = () ->
      $http.post('/p/c/w/view_cache.json', '{}').success (response) ->
        console.log('Refresh: request data from server')
        $scope.data = response

    $scope.accept = () ->
      $http.post('/p/c/w/accept', '{}').success (response) ->
        console.log('Accept')

    $scope.reject = () ->
      $http.post('/p/c/w/reject', '{}').success (response) ->
        console.log('Reject')

    $http.post('/p/c/w/view_cache.json', '{}').success (response) ->
        console.log 'Init: request data from server'
        $scope.data = response
        
app.controller 'TestEdit', ($scope, $http) ->
    console.log 'Initialize TestEdit'
    $scope.wallpaper = {
        'id' : 0
    }
    
    $scope.sendData = () ->
        console.log('Send data: id=' + $scope.wallpaper.id)
        $http.post('/p/c/w/edit', $scope.wallpaper).success