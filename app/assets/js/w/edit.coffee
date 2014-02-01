console = window.console

Editing =
  makeEditable: (id, type, title) ->
    console.info 'making hover'
    elem = $ id
    elem.mouseenter () ->
      elem.append '<img alt="редактировать" style="margin-left: 15px;" border="0" src="' + context_path + '/img/options.jpg">'
      elem.css 'color', 'red'
      elem.css 'cursor', 'pointer'
    elem.mouseleave () ->
      elem.children('img').remove()
      elem.css 'color', ''
    elem.click () ->
      console.info 'click open'
      Editing.type = type
      Editing.editDialogValue.val elem.text()
      Editing.editDialog.dialog 'option', 'title', 'Редактировать ' + title
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
            'id': '' + wallpaper_id
        success: (data) ->
          console.info 'resp = [' + data + ']'
          Editing.editDialog.dialog 'close'
      .fail (jqXHR, textStatus) ->
        alert 'Произошла ошибка: ' + textStatus

$ ->
  $.ajax
    url: context_path + '/p/a/tmpl/w/edit.htm'
    success: (data) ->
      $(data).dialog
        modal: true
        autoOpen: false
      Editing.init()
      Editing.makeEditable '#description', 'description', 'описание'
