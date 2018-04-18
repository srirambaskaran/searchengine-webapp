package org.searchengine.servlets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.codehaus.jackson.map.ObjectMapper;
import org.searchengine.exceptions.SearchEngineException;
import org.searchengine.models.QueryModel;
import org.searchengine.models.ResultModel;

/**
 * Servlet implementation class LandingServlet
 */
@WebServlet("/search")
public class SearchAPI extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SearchAPI() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	    BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream()));
	    ObjectMapper mapper = new ObjectMapper();
	    String queryString = br.readLine();
        
        QueryModel queryModel = mapper.readValue(queryString, QueryModel.class);
        Search search = new Search(queryModel);
        ResultModel resultModel = null;
        try {
            resultModel = search.run();
        } catch (SearchEngineException e) {
            ResultModel errorModel = new ResultModel();
            errorModel.setNumResult(0);
            errorModel.setStatus(false);
            errorModel.setMessage(e.getMessage());
            String responseJSON = mapper.writeValueAsString(errorModel);
            response.getWriter().write(responseJSON);
        }
        String responseJSON = mapper.writeValueAsString(resultModel);
        response.getWriter().write(responseJSON);
	    
	    
	    
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
