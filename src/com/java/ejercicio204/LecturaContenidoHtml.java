// Referencia bibliogr-afica: https://www.youtube.com/watch?v=RkIQ6n3QQQM

/*
 * Para copiar manteniendo el formato del código desde Eclipse, se debe desplegar 
 * o poner en visible todo el código, para esto se debe hacer click en el signo más
 * de la izquierda. Una vez que se muestre todo el código en el editor, se puede 
 * seleccionar todo y copiar a Word, por ejemplo. Normalmente, la parte del código 
 * de las librerías importadas se ponen en modo oculto de forma automática.
 * */

//Alternativa al servicio web API "Yahoo Weather"

/* Librer-ias utilizadas:
 * accessors-smart-2.4.8.jar
 * json-path-2.7.0.jar
 * json-smart-2.4.8.jar
 * slf4j-api-2.0.0.jar
 * slf4j-nop-2.0.0.jar
 * */

package com.java.ejercicio204;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
//import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;

import com.jayway.jsonpath.JsonPath;
//import java.util.Map;
//import java.util.HashMap;
//import net.minidev.json.writer.JsonReader;

public class LecturaContenidoHtml {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String pruebaConexionInternet= null; 
		final String DIRECCION = "https://api.open-meteo.com/v1/forecast?latitude=-0.22985&longitude=-78.52495&hourly=temperature_2m,relativehumidity_2m,windspeed_10m&timezone=America%2FGuayaquil";
		String codificacion = StandardCharsets.ISO_8859_1.toString();
		
		try {// Probar -catch- con cualquier argumento String en el constructor URL().
			URL url = new URL( DIRECCION );// Para probar la Excepci-on se puede poner esto: URL url = new URL( "DIRECCION" );
			URLConnection conexion = url.openConnection();
			
			//Determinaci-on del tipo de codificaci-on
			String tipoContenido = null;
			
			tipoContenido = conexion.getContentType();
			// getContentType() Puede devolver "null" si el tipo de contenido es desconocido.
			/* Hacer esto no es recomendable:
			 * try {
             * doSomething(); // int indiceInicioCodificacion = tipoContenido.indexOf("charset=");// "charset=" tiene 8 caracteres
        	 * } catch (NullPointerException e) {
             * System.out.print("Caught the NullPointerException");
        	 * }
			 * 
			 * */
			
			if ( tipoContenido == null ) 
			{
				pruebaConexionInternet = "No hay conexi-on a  https://api.open-meteo.com";
				System.out.println(pruebaConexionInternet);
			}else {
				pruebaConexionInternet = "Conexi-on a  https://api.open-meteo.com ¡establecida!";
				System.out.println(pruebaConexionInternet+"\n");
			
				int indiceInicioCodificacion = tipoContenido.indexOf("charset=");// "charset=" tiene 8 caracteres
				if( indiceInicioCodificacion != -1 )
				{//Otra posible codificaci-on puede ser UTF-8
					codificacion = tipoContenido.substring( indiceInicioCodificacion+8 );
					System.out.println(codificacion);
					// Al ver la impresi-on, se confirma que est-a codificado en UTF-8
				}
				
				// Lectura del contenido del documento HTML // Tambi-en sirve para contenido JSON
				InputStream is = new BufferedInputStream(conexion.getInputStream());
				Reader r = new InputStreamReader(is, codificacion);
				
				int caracter; String documento = new String(); StringBuilder docSB = new StringBuilder();
				// la variable -documento- debe ser creada como un nuevo objeto.
				// String documento= null; // esto hace que se asignen los caracteres 
				// iniciales como "null", por lo que
				// al comprobar imprimiendo, aparece al principio la cadena "null"
				while( ( caracter = r.read() ) != -1 ) 
				{
					// System.out.println( ( char )caracter );// Vista vertical
					//System.out.print( ( char )caracter );// Vista horizontal
					documento += (char)caracter; //Funciona bien. Tener en cuenta que -documento- debe ser objeto (usar "new").
					docSB.append( (char)caracter );// docSB = docSB.append( (char)caracter );
				}// La salida ser-a un caracter en cada l-inea xq se usa println()
				// System.out.println(documento);// Comprobado
				System.out.println(docSB);// Es la forma m-as utilizada. Pero es de tipo BuidString, entonces se debe pasar a String con toString().
				
				
				// ----------Probando JSONPath-------
				// -- RECORDAR que los datos son recogidos en tiempo real de la web,
				// -- un archivo de respaldo solo servir-ia para revisar el formato JSON.
				
				String zonaHoraria = new String();
				zonaHoraria = JsonPath.read( documento, "$.timezone" );// Igual como un vector, su -indice empieza en cero [0].
				System.out.println( "\nZona Horaria: "+zonaHoraria );
				// La variable -documento- ya es de tipo String por lo que
				// ya no se requiere usar toString().
				
				//---------------------------------
				String docSB_String = new String();
				docSB_String = docSB.toString();
				
				//--Informaci-on a cada hora (hourly)
				String tiempo = new String();
				tiempo = JsonPath.read( docSB_String, "$.hourly.time[0]" );// Igual como un vector, su -indice empieza en cero [0].
				System.out.println( "\t"+tiempo );
				
				Double temperatura_2m;// = new Double(); // es "deprecated"
				temperatura_2m = JsonPath.read( docSB_String, "$.hourly.temperature_2m[0]" );
				System.out.println( "\tTemperatura: "+temperatura_2m );
				
				Double humedadrelativa_2m;// = new Double(); // es "deprecated"
				humedadrelativa_2m = JsonPath.read( docSB_String, "$.hourly.relativehumidity_2m[0]" );
				System.out.println( "\t"+humedadrelativa_2m );
				
				Double velocidadviento_10m;// = new Double(); // es "deprecated"
				velocidadviento_10m = JsonPath.read( docSB_String, "$.hourly.windspeed_10m[0]" );
				System.out.println( "\t"+velocidadviento_10m );
			}
			
		}catch ( MalformedURLException e){// catch (Exception e){
			System.out.println("\nLa URL est-a mal formada");
			throw new RuntimeException(e);
		}catch( IOException e ) {
			System.out.println( "Error de entrada/salida: "+e.getMessage() );
			throw new RuntimeException(e);
		}

	}
}
