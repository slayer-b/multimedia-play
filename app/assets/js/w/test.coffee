###
asdasd
###
class Test3
    constructor: (first, last) ->
        @firstName = ko.observable(first)
        @lastName =  ko.observable(last)
        @fullName = ko.computed ->
            @firstName() + " " + @lastName()
            
fill = (container, liquid = "coffee") ->
  "Filling the #{container} with #{liquid}..."
    
result = 0xf

$ ->
    ko.applyBindings(new ViewModel("CoffeeScript", "Fan"))