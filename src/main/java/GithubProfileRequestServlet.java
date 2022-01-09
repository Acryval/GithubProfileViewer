

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 * Servlet implementation class GithubProfileRequestServlet
 */
@WebServlet("/GithubProfileRequestServlet")
public class GithubProfileRequestServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final String WEBAPP_README = "https://github.com/Acryval/GithubProfileViewer";
	
	private static final String GITHUB_USERS_URL = "https://api.github.com/users/";
	private static final int GITHUB_REPOS_PER_REQ = 100;
	private static final int DEFAULT_TOP_LAN_NUM = 5;
	
	private String authToken;
	private int top_lang_num;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GithubProfileRequestServlet() {
        super();
    }
    
    @Override
    public void init() throws ServletException {
    	super.init();
    	authToken = getServletConfig().getInitParameter("oauth_token");
    	String _top_cnt = getServletConfig().getInitParameter("top_count");
    	if(_top_cnt == null) {
    		top_lang_num = DEFAULT_TOP_LAN_NUM;
    	}else {
    		top_lang_num = Integer.parseInt(_top_cnt);
    	}
    }
    
    public Object[] getJSONFromGithubApi(String url) throws IOException {
    	Object ret = new Object();
    	int response_code = 0;
    	
    	HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
    	conn.setRequestMethod("GET");
    	conn.setRequestProperty("Accept", "application/vnd.github.v3+json");
    	
    	if(authToken != null) {
    		conn.setRequestProperty("Authorization", "token " + authToken);
    	}
    	
    	response_code = conn.getResponseCode();
    	
    	if(response_code >= 400) { // any http error
    		conn.disconnect();
    		
    		if(response_code == 403) { // forbidden -> github api request limit exceeded
    			String[] s = authToken == null ? 
    					new String[] {
    							"60",
    							"<br>Aby podniesc limit do 5000 zapytan skonfiguruj aplikacje zgodnie z <a href=\"redirect.html?redirect=" + WEBAPP_README + "\">instrukcja</a>"
    					} :
    					new String[] {
    							"5000", 
    							"<br>Prosze sprobowac pozniej"
    					};
        		return new Object[] {null, "Przekroczono limit " + s[0] + " zapitan w ciagu godziny do Github API" + s[1]};
    		}else if(response_code == 404) { // not found
    			return new Object[] {null, "Taki uzytkownik nie istnieje"};
    		}
    		return new Object[] {null, response_code};
    	}
    	
		try {
			ret = new JSONParser().parse(new BufferedReader(new InputStreamReader(conn.getInputStream())));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		conn.disconnect();
    	return new Object[]{ret, response_code};
    }
    
    public boolean connectionErrorCheck(Object[] o, PrintWriter out) {
    	if(o[0] == null) {
			out.println("<b><h1>Blad: " + o[1] + "</h1></b>");
			out.println("</body></html>");
			return true;
    	}
    	return false;
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		
		long total_stars = 0;
		long total_repos;
		long repos_fetch_count;
		
		JSONArray user_repos_Jarr;
		JSONObject user_profile_Jobj;
		JSONObject temp_lang_Jobj;
		JSONObject temp_repo_Jobj;
		Object[] data = {null, null};
		
		HashMap<String, Long> lang_map = new HashMap<String, Long>();
		
		StringBuilder repos_out_str = new StringBuilder();
		String github_username = request.getParameter("github_user");
		String user_profile_url = GITHUB_USERS_URL + github_username;
		String user_html_url;
		String user_repos_url;
		String user_login;
		
		// begin html response
		out.println("<!DOCTYPE html><html><head><meta charset=\"UTF-8\" /><title>Github Profile Viewer</title></head><body>");
		out.println("<p><a href=\"index.html\">Powrot do wyboru uzytkownika</a></p>");
		
		// get user profile
		data = getJSONFromGithubApi(user_profile_url);
		if(connectionErrorCheck(data, out))  return;
		user_profile_Jobj = (JSONObject) data[0];
		
		total_repos = (Long)user_profile_Jobj.get("public_repos");
		repos_fetch_count = total_repos / GITHUB_REPOS_PER_REQ;
		user_html_url = (String) user_profile_Jobj.get("html_url");
		user_repos_url = (String) user_profile_Jobj.get("repos_url");
		user_login = (String) user_profile_Jobj.get("login");
		
		out.println("<h1>Informacje o profilu uzytkownika: <a href=\"redirect.html?redirect_url=" + user_html_url + "\">" + user_login + "</a></h1>");
		out.println("<h2>Ilosc publicznych repozytoriow: " + total_repos + "</h2>");
		
		for(int i = 0; i <= repos_fetch_count; i++) {
			data = getJSONFromGithubApi(user_repos_url + "?per_page=" + GITHUB_REPOS_PER_REQ + "&page=" + (i+1));
			if(connectionErrorCheck(data, out))  return;
			user_repos_Jarr = (JSONArray) data[0];
			
			int repo_nr = GITHUB_REPOS_PER_REQ*i;
			
			for(Object o : user_repos_Jarr) {
				temp_repo_Jobj = (JSONObject) o;
				
				if(authToken != null) {
					data = getJSONFromGithubApi((String) temp_repo_Jobj.get("languages_url"));
					if(connectionErrorCheck(data, out))  return;
					temp_lang_Jobj = (JSONObject) data[0];
					
					temp_lang_Jobj.forEach((k, v) -> lang_map.merge((String)k, (Long)v, (oldV, newV) -> oldV + newV));
				}
				
				long stars = (Long)temp_repo_Jobj.get("stargazers_count");
				String repo_name = (String) temp_repo_Jobj.get("name");
				String repo_url = (String) temp_repo_Jobj.get("html_url");
				
				repos_out_str.append("<p>");
				
				repos_out_str.append("Nr: " + (++repo_nr) + " : ");
				repos_out_str.append("<b><a href=\"redirect.html?redirect_url=" + repo_url + "\">" + repo_name + "</a></b>");
				repos_out_str.append(", liczba gwiadzek: <b>" + stars + "</b>");
				
				repos_out_str.append("</p>");
				
				total_stars += stars;
			}
		}
		
		out.println("<h2>Calkowita liczba zdobytych gwiazdek: " + total_stars + "</h2>");
		
		if(total_repos > 0) {
			if(authToken != null) {
				out.println("<h2>Ilosc jezykow programowania uzytych we wszystkich repozytoriach: " + lang_map.size() + "</h2>");
				
				List<Entry<String, Long>> list = new ArrayList<Entry<String, Long>>(lang_map.entrySet());
				list.sort((o1, o2) -> o2.getValue().compareTo(o1.getValue()));
				
				int top = list.size() < top_lang_num ? list.size() : top_lang_num;
				
				out.println("<h2>Top " + top + " jezykow:</h2>");
				for(int i = 0; i < top; i++) {
					Entry<String, Long> e = list.get(i);
					out.println("<p>" + e.getKey() + ": " + e.getValue() + " bajtow</p>");
				}
			}
			out.println("<h2>Lista repozytoriow:</h2>");
			out.println(repos_out_str);
		}
		
		out.println("</body></html>");
	}
}
