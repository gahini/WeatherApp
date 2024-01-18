package mypackage;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Date;
import java.util.Scanner;

import com.google.gson.Gson;
import com.google.gson.JsonObject;


public class MyServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    
    public MyServlet() {
        super();
        
    }

	
	public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		// TODO Auto-generated method stub
		res.getWriter().append("Served at: ").append(req.getContextPath());
	}

		public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		
			String inputData=req.getParameter("userInput");
			//API SETUP
			String apiKey="ef2d0e8310fb3878e7aa8d4b845c5634";
			//GET THE CITY FROM THE INPUT
			String city=req.getParameter("city");
			// CREATE THE URL FOR THE OPENWEATHER API REQ
	        String apiUrl = "https://api.openweathermap.org/data/2.5/weather?q=" + city + "&appid=" + apiKey;
	        try {
	        	// API INTIGRATION
	            URL url = new URL(apiUrl);
	            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
	            connection.setRequestMethod("GET");
	            //reading the data from network
	            InputStream inputStream   =connection.getInputStream();
	            InputStreamReader reader = new InputStreamReader(inputStream);
	            //WANT TO STORE IN sTRING
	            StringBuilder responseContent=new StringBuilder();
	            //input 
	            Scanner scanner=new Scanner(reader);
	            while(scanner.hasNext())  {
	            	responseContent.append(scanner.nextLine());

	        }
	            scanner.close();
	           
	           
	            
	            Gson gson = new Gson();
	            JsonObject jsonObject = gson.fromJson(responseContent.toString(), JsonObject.class);	 
	            System.out.println(jsonObject);
	            
	            //Date & Time
	            long dateTimestamp = jsonObject.get("dt").getAsLong() * 1000;
	            String date = new Date(dateTimestamp).toString();
	            
	            //Temperature
	            double temperatureKelvin = jsonObject.getAsJsonObject("main").get("temp").getAsDouble();
	            int temperatureCelsius = (int) (temperatureKelvin - 273.15);
	           
	            //Humidity
	            int humidity = jsonObject.getAsJsonObject("main").get("humidity").getAsInt();
	            
	            //Wind Speed
	            double windSpeed = jsonObject.getAsJsonObject("wind").get("speed").getAsDouble();
	            
	            //Weather Condition
	            String weatherCondition = jsonObject.getAsJsonArray("weather").get(0).getAsJsonObject().get("main").getAsString();
	            
	            // Set the data as request attributes (for sending to the jsp page)
	            req.setAttribute("date", date);
	            req.setAttribute("city", city);
	            req.setAttribute("temperature", temperatureCelsius);
	            req.setAttribute("weatherCondition", weatherCondition); 
	            req.setAttribute("humidity", humidity);    
	            req.setAttribute("windSpeed", windSpeed);
	            req.setAttribute("weatherData", responseContent.toString());
	            
	            connection.disconnect();
	   
	            
	        }  
	            
	            catch(IOException e) {
	        	e.printStackTrace();
	        }
	        
		
	        req.getRequestDispatcher("index.jsp").forward(req, res);

		}
}

			
			
	


