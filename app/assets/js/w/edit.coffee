console = window.console

Editing =
  makeEditable: (id, type) ->
    console.info 'making hover'
    elem = $ id
    elem.mouseenter () -> elem.css 'color', 'red'
    elem.mouseleave () -> elem.css 'color', ''
    elem.click () ->
      console.info 'click open'
      Editing.type = type
      Editing.editDialogValue.val elem.text()
      Editing.editDialog.dialog 'open'
  init: () ->
    Editing.editDialogValue = $ '#edit-dialog-value'
    Editing.editDialog = $ '#edit-dialog'
    editDialogClose = $ '#edit-dialog-close'
    editDialogSave = $ '#edit-dialog-save'

    editDialogClose.click () ->
      Editing.editDialog.dialog 'close'
    editDialogSave.click () ->
      $.ajax
        url: context_path + '/p/c/w/edit'
        dataType: 'json'
        contentType: 'application/json; charset=UTF-8'
        type: 'POST'
        data:
          JSON.stringify
            'value': Editing.editDialogValue.val()
            'field': Editing.type
            'id': wallpaper_id
        success: (data) ->
          console.info 'resp = [' + data + ']'
          Editing.editDialog.dialog 'close'

$ ->
  $.ajax
    url: context_path + '/p/a/tmpl/w/edit.htm'
    success: (data) ->
      $(data).dialog
        modal: true
        autoOpen: false
      Editing.init()
      Editing.makeEditable '#description', 'description'

