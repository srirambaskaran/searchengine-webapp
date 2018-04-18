var start_time = null;

function createSearchResult(response) {
	var result = $("<div></div>").addClass("search-result").addClass("col-12").css("margin-bottom","20px");
	var title = $("<span></span>").addClass("search-title").text(response.title);
	var title_link = $("<a></a>").attr("href",response.link).attr("target","_blank").append(title);
	var br = $("<br/>")
	var link = $("<span></span>").addClass("search-link").text(response.link);
	var para = $("<span></span>").addClass("search-para").text(response.para);
	result.append(title_link).append(br).append(link).append(para);
	return result;
}



function fetchResults(query, rankingType) {
	
	var query_model = {
			"query": query,
			"rankingType": rankingType
	} 
	
	$.post("search", JSON.stringify(query_model), function(response){
		var search_results = JSON.parse(response)
		if (search_results.status) {
			displayResults(search_results);
		} else {
			alert("Search did not complete succesfully. Message to user: "+search_results.message);
			$("#search-results").hide();
		}
	})
}



function displayResults(returned_json){
	var d = new Date();
	var end_time = d.getTime();
	var diff = (end_time-start_time)/1000;
	$("#search-results").show();
	$("#spinner").hide();
	$("#time-for-search").text(diff+" seconds")
	$("#num_results").text(returned_json.numResult);
	$("#results").empty();
	for(var response in returned_json.responses){
		$("#results").append(createSearchResult(returned_json.responses[response]));
	}
}

function search() {
	var rankingType = $("#rankingType input:radio:checked").val();
	var query = $("#query").val();
	var d = new Date();
	start_time = d.getTime();
	$("#spinner").show();
	fetchResults(query, rankingType);
}

$( function() {
	$("#search-results").hide();
	$("#spinner").hide();
	$("#search").on("click", search);
});

$( "#birds" ).autocomplete({
    source: function( request, response ) {
      $.ajax( {
        url: "suggest",
        dataType: "jsonp",
        data: {
          term: request.term
        },
        success: function( data ) {
          response( data );
        }
      } );
    },
    minLength: 2,
    select: function( event, ui ) {
      log( "Selected: " + ui.item.value + " aka " + ui.item.id );
    }
  } );

