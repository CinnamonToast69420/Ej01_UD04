import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Scanner;

import org.json.JSONException;
import org.json.JSONObject;
public class JSONURLReader {
	private HttpURLConnection conx;
	private String url;
	private String cantidadEuros;
	
	public JSONURLReader(String cantidadEuros) throws JSONException, IOException
	{
		this.url = "https://api.apilayer.com/exchangerates_data/convert?to=JPY&from=EUR&amount=";
		this.cantidadEuros = cantidadEuros;
		peticionConversionEuroAYen(cantidadEuros);
		
	}

	/*método que acepta un Reader y lo convierte en texto*/
	public String leerLinea(Reader rb)throws IOException 
	{
		 /* Empleo StringBuilder para leer el contenido del Json(a través de BufferedReader) que me devuelvolverá la petición URL
		  * Lo convierto a String y lo devuelvo. */
		StringBuilder miSB = new StringBuilder();
		int cp;
		while((cp = rb.read())!= -1) 
		{
			miSB.append((char)cp);
		}
		
		return miSB.toString();
	}
	
	
	/* Este método se encarga de hacer una petición GET a una api específica de conversión de monedas,
	 * primero creo una instancia de URL a la que le concateno la cantidad en euros especificada por el usuario,
	 * después creo  una instancia de HTTPURLConnection a partir del método open connection de mi objeto URL
	 * establezco el método de petición y la propiedad(¡muy importante!)apikey para poder hacer la petición<-Me quedan <100 peticiones!!-
	 * creo mi objeto JSON a partir del método devolverJson(está explicado en el siguiente comentario) y obtengo
	 * el valor de los datos de conversión en mi JSONObject a través de sus métodos getObject(nombreobjeto{})(para los objetos JSON dentro del documento)
	 * y getString("nombre clave")
	 * Finalmente los imprimo por consola.*/
	public void peticionConversionEuroAYen(String cantidad) throws JSONException, IOException
	{
	    try {
	    	
	        URL urlGet = new URL(url.concat(cantidadEuros));
	        conx = (HttpURLConnection) urlGet.openConnection();
	        conx.setRequestMethod("GET");
	        conx.setRequestProperty("apikey", "Hyw4b5rkB1Z6lspt08UoumKShcpLPKNz");
	        JSONObject jsonExchange = devolverJson();
	        
	        System.out.println("Símbolo Euros: " + jsonExchange.getJSONObject("query").getString("from"));
	        System.out.println("Símbolo Yenes : " + jsonExchange.getJSONObject("query").getString("to"));
	        System.out.println("Tasa de conversión: " + jsonExchange.getJSONObject("info").getString("rate"));
	        
	        
			System.out.println("Resultado de la conversión: " + jsonExchange.getString("result"));
			
			
	        conx.disconnect();
	    } catch (MalformedURLException e) {
	        e.printStackTrace();
	    }
	    }
	
	/*devolverJson emplea el método getInputStream sobre mi objeto HTTPURLConnection y le pone las otras dos capas
	 * (InputStreamReader, BufferedReader) para leer el json recibido a través del método leerLinea y crear mi objeto de JSON
	 * pasándole por su constructor el texto recibido producto de la petición.
	 * Finalmente lo devuelvo para emplearlo en el método peticionConversionEuroaYen()
	 * 
	 */
	public JSONObject devolverJson() throws IOException, JSONException
	{
		InputStream is = conx.getInputStream();
		BufferedReader br = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
		String textoJson = leerLinea(br);
		JSONObject miJson = new JSONObject(textoJson);
		br.close();
		is.close();
		return miJson;
		
		
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			Scanner sr = new Scanner(System.in);
			
			System.out.println("Esta es una aplicación de conversión de Euros a Yenes japoneses.\nA continuación teclearás la cantidad a convertir con decimales separados por puntos:\n");
			String cantidadUsuario = sr.next();
			JSONURLReader urlConversiondeEuroaDolar = new JSONURLReader(cantidadUsuario);
			
		} catch (JSONException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
	}
	}

