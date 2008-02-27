import java.io.*;
import java_cup.runtime.*;

/**
 * Universidad Simon Bolivar
 * Lenguajes de Programacion II
 * 
 * Maria Sol Ferrer 04-36975
 * Jamil Navarro 04-37334
 *
 * Proyecto: Procesador de lenguaje imperativo.
 * 
 * Prueba.java: Programa de Prueba que recibe un
 * archivo y lo analiza sintacticamente.
 *
 * Este programa esta basado en un ejemplo encontrado en
 * http://www.cs.pitt.edu/~mock/cs1622/PA3/pa3.html
 * 
 */

public class Prueba {
    public static void main(String[] args)
	throws IOException
    {
	// Chequea los argumentos pasados por la linea de comandos
	if (args.length != 1) {
	    System.err.println("Falta nombre de archivo a ser analizado.");
	    System.exit(-1);
	}

	//Abre el archivo de Entrada
	FileReader archivo = null;
	try {
	    archivo = new FileReader(args[0]);
	} catch (FileNotFoundException ex) {
	    System.err.println("Archivo: " + args[0] + " no encontrado.");
	    System.exit(-1);
	}

	parser P = new parser(new Yylex(archivo));

	Symbol root = null; 

	try {
	    root = P.parse(); 
	    System.out.println ("El programa esta sintacticamente correcto.");
	} catch (Exception ex){
	    System.out.println(ex);
	    System.exit(0);
	}
	return;
    }
}
