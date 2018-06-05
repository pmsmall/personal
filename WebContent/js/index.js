function autoAdjustFormMarginTop() {
	var form = $(".form");
	var height = form.outerHeight();
	form.css("margin-top", -height / 2);
}

$('.form').find('input, textarea').on('input porpertychange blur focus', function(e) {
	var $this = $(this),
		label = $this.prev('label');

	if(e.type === 'porpertychange' || e.type === 'input') {
		if($this.val() === '') {
			label.removeClass('active highlight');
		} else if($this.val() !== '') {
			label.addClass('active highlight');
		}
	} else if(e.type === 'blur') {
		if($this.val() === '') {
			label.removeClass('active highlight');
		} else {
			label.removeClass('highlight');
		}
	} else if(e.type === 'focus') {
		if($this.val() === '') {
			label.removeClass('highlight');
		} else if($this.val() !== '') {
			label.addClass('highlight');
		}
	}

});

function showTable(table) {
	$('.tab-content > div').not(table).hide();
	$(table).fadeIn(600);
	autoAdjustFormMarginTop();
}

function hashchangeHandle(isNew) {
	switch(window.location.hash) {
		case "#":
		case "":
			if(isNew) {
				$("#signup-menu").removeClass('active');
				$("#login-menu").addClass('active');
				showTable($("#login"));
			}
			break;
		case "#signup":
			$("#login-menu").removeClass('active');
			$("#signup-menu").addClass('active');
			showTable($("#signup"));
			break;
		case "#login":
			$("#signup-menu").removeClass('active');
			$("#login-menu").addClass('active');
			showTable($("#login"));
			break;
		default:
			window.location.hash = "";
			break;
	}
}

$(document).ready(function() {
	if(window != top)
		top.location.href = location.href;
	hashchangeHandle(true);
	//autoAdjustFormMarginTop();
	$(window).on("hashchange", hashchangeHandle);
});