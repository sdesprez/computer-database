$(document).ready(function() {
		$("#form").validate({
			messages: {
                computerName: "Please enter a computer name",
                introducedDate: "The date format is not valid, valid format is yyyy-MM-dd. You can also leave this field emtpy",
                discontinuedDate: "The date format is not valid, valid format is yyyy-MM-dd. You can also leave this field emtpy"
            }
		});
});