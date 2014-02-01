app = angular.module('cms', [])

app.controller 'ItemsTable', ($scope, $http) ->
  console.log 'Initialize TestTable'

  $http.get("/cms/comment.json").then (response) ->
    $scope.headers = response.data
    $scope.items = [{"id": "a1", "id_photo": "b1", "text": "c1"}, {"id": "a2", "id_photo": "b2", "text": "c2"}]
    console.log "got it"