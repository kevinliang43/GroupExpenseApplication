$('#login').click(function() {
    $('#' + $(this).prop('id') + 'Modal').modal({show:true, backdrop:'static', keyboard:false});
});

$('#signup').click(function() {
    $('#' + $(this).prop('id') + 'Modal').modal({show:true, backdrop:'static', keyboard:false});
});

$('#add').click(function() {
    $('#' + $(this).prop('id') + 'Modal').modal({show:true, backdrop:'static', keyboard:false});
});

$('#addExpense').click(function() {
    $('#' + $(this).prop('id') + 'Modal').modal({show:true, backdrop:'static', keyboard:false});
});

$('#removeExpense').click(function() {
    $('#' + $(this).prop('id') + 'Modal').modal({show:true, backdrop:'static', keyboard:false});
});

$('#editInfo').click(function() {
    $('#' + $(this).prop('id') + 'Modal').modal({show:true, backdrop:'static', keyboard:false});
});

$(document).on('click', '.btn-add', function(e) {
        e.preventDefault();

        var controlForm = $('.controls form:first'),
            currentEntry = $(this).parents('.entry:first'),
            newEntry = $(currentEntry.clone()).appendTo(controlForm);

        newEntry.find('input').val('');
        controlForm.find('.entry:not(:last) .btn-add')
            .removeClass('btn-add').addClass('btn-remove')
            .removeClass('btn-success').addClass('btn-danger')
            .html('<span class="glyphicon glyphicon-minus"></span>');
    }).on('click', '.btn-remove', function(e)
    {
		$(this).parents('.entry:first').remove();

		e.preventDefault();
		return false;
	});

function showHome() {
	document.getElementById('homeDisplay').style.display = "block";
	document.getElementById('igroupDisplay').style.display = "none";
}

function showigroup() {
	document.getElementById('igroupDisplay').style.display = "block";
	document.getElementById('homeDisplay').style.display = "none";
}
