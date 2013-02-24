app = angular.module('myApp', [])

app.controller 'TestTable', ($scope, $http) ->
    console.log 'Initialize TestTable'
    
    $scope.data = []
    
    $scope.refresh = () ->
      $http.post(jsRoutes.controllers.EditWallpaperPublic.view().url, '{}').success (response) ->
        console.log('Refresh: request data from server')
        $scope.data = response

    $scope.accept = (entry) ->
      $http.post('/p/c/w/accept', entry).success (response) ->
        console.log('Accept ' + entry.entry_id)

    $scope.reject = (entry) ->
      $http.post('/p/c/w/reject', entry).success (response) ->
        console.log('Reject ' + entry.entry_id)

    $http.post(jsRoutes.controllers.EditWallpaperPublic.view().url, '{}').success (response) ->
        console.log 'Init: request data from server'
        $scope.data = response
        
app.controller 'TestEdit', ($scope, $http) ->
    console.log 'Initialize TestEdit'
    $scope.wallpaper = {
        'id' : 0
    }
    
    $scope.sendData = () ->
        console.log('Send data: id=' + $scope.wallpaper.id)
        $http.post(jsRoutes.controllers.EditWallpaperPublic.edit().url, $scope.wallpaper).success