import com.sun.org.apache.bcel.internal.generic.SWITCH;
import java.io.*;
import java.lang.*;
import java.util.*;

/**
 * Universidad Simon Bolivar
 * Lenguajes de Programacion II
 * Entrega final 7/04/2008
 * 
 * Maria Sol Ferrer 04-36975
 * Jamil Navarro 04-37334
 *
 * Proyecto: Procesador de lenguaje imperativo.
 * 
 * Expresion.java: clase Expresion
 * 
 */
 
/**
 * Enumeracion que contiene todos los operadores binarios contenidos en la
 * gramatica del lenguaje. Maneja ademas la representacion en <b>String</b>
 * de cada uno de ellos.
 */
enum OperadorB {
    SUMA("+"), 
    RESTA("-"), 
    MULT("*"), 
    DIVR("/"), 
    DIVE("div"), 
    MOD("mod"), 
    AND("&"), 
    OR("|"), 
    IGUAL("="), 
    DESIGUAL("!="), 
    MAYOR(">"), 
    MENOR("<"), 
    MAYORIGUAL(">="), 
    MENORIGUAL("<=");
    
    //Variable que almacena el valor de la representacion en <b>String</b>
    private String s;
    public String toString() {
        return this.s;
    }    
    /**
	* Constructor usado implicitamente para asociar cada operador con su String
	* @param str de tipo <b>String</b> representacion del operador
	*/
    OperadorB(String str){
        this.s = str;
    }
}

/**
 * Enumeracion que contiene todos los operadores unarios contenidos en la
 * gramatica del lenguaje. Maneja ademas la representacion en <b>String</b>
 * de cada uno de ellos.
 */
enum OperadorU {
    MENOS("-"), 
    NOT("!"),
    TECHO("techo"),
    PISO("piso"),
    REDONDEO("redondeo");    
    //Variable que almacena el valor de la representacion en <b>String</b>
    private String s;
    public String toString() {
        return this.s;
    }    
    /**
	* Constructor usado implicitamente para asociar cada operador con su String
	* @param str de tipo <b>String</b> representacion del operador
	*/
    OperadorU(String str){
        this.s = str;
    }
}

/**
 * Enumeracion que contiene los posibles tipos de los Factores
 * @see Factor
 */
enum TipoF {INT,INTtoFLOAT,FLOAT,ID,BOOL,PROC,FUNC,VOID,ARRAY,REG,VAR,ERROR,NODEF}

/**
 * Clase abstracta que sirve como Superclase para los diversos tipos de 
 * expresiones del lenguaje
 */
abstract class Expresion {

    /**
	* Metodo desarrollado por conveniencia. Imprime la representacion en
	* <b>String</b> del objeto.
	*/
    public abstract String imprimir(int i);    
    public abstract boolean esCorrecta(Bloque c, int line);    
    public abstract TipoF getTipo(Bloque c);    
    void showError(String e, String tipoEsperado, String tipoHallado){
        String error = "Error de tipo en la expresion"+e+" se esperaba ";
        error += tipoEsperado+" y se hallo "+tipoHallado;
        System.out.println(error);
    }    
}

/**
 * Clase que representa expresiones cuyos operadores son binarios
 */
class ExprBin extends Expresion {
        
    //Sub-Expresion mas a la izquierda
    private Expresion ExprIzq;	    
    //Operador
    private OperadorB Op;    
    //Sub-Expresion mas a la derecha
    private Expresion ExprDer;

    /**
	* Constructor de Expresiones cuyo operdaor es binario
	* @param EI subexpresion mas hacia la izquierda
	* @param O operador binario
	* @param ED subexpresion mas hacia la derecha
	*/
    public ExprBin (Expresion EI, OperadorB O, Expresion ED) {
        this.ExprIzq = EI;
        this.Op = O;
        this.ExprDer = ED;
    }
    public String toString(){
        return "(" + ExprIzq +" "+ Op +" "+ ExprDer + ")";
    }	
	public String imprimir(int i){
		String s = "";
        for (int j=0;j<i;j++) { s+="-"; }
		s = "("+Op+")\n";
		for (int j=0;j<i;j++) { s+="-"; }
		s += "-" + ExprIzq.imprimir(i+1);
		for (int j=0;j<i;j++) { s+="-"; }
		s += "-" + ExprDer.imprimir(i+1);
		return s;
    }
    public TipoF getTipo(Bloque c) {
        switch(this.Op){
            case AND:
            case OR:
				if (this.ExprDer.getTipo(c).equals(TipoF.BOOL) && 
						this.ExprIzq.getTipo(c).equals(TipoF.BOOL)) {
					return TipoF.BOOL;
				} else {
					return TipoF.ERROR;
				}
            case IGUAL:
            case DESIGUAL:
				if ( (this.ExprDer.getTipo(c).equals(TipoF.INT) && 
						this.ExprIzq.getTipo(c).equals(TipoF.INT)) ||
					(this.ExprDer.getTipo(c).equals(TipoF.FLOAT) && 
						this.ExprIzq.getTipo(c).equals(TipoF.FLOAT)) ||
					(this.ExprDer.getTipo(c).equals(TipoF.INT) && 
						this.ExprIzq.getTipo(c).equals(TipoF.FLOAT)) ||
					(this.ExprDer.getTipo(c).equals(TipoF.FLOAT) && 
						this.ExprIzq.getTipo(c).equals(TipoF.INT)) ||
					(this.ExprDer.getTipo(c).equals(TipoF.BOOL) && 
						this.ExprIzq.getTipo(c).equals(TipoF.BOOL)) )
					return TipoF.BOOL;					
				else
					return TipoF.ERROR;				
            case MAYOR:
            case MENOR:
            case MAYORIGUAL:
            case MENORIGUAL:
                if ( (this.ExprDer.getTipo(c).equals(TipoF.INT) && 
						this.ExprIzq.getTipo(c).equals(TipoF.INT)) ||
					(this.ExprDer.getTipo(c).equals(TipoF.FLOAT) && 
						this.ExprIzq.getTipo(c).equals(TipoF.FLOAT)) ||
					(this.ExprDer.getTipo(c).equals(TipoF.INT) && 
						this.ExprIzq.getTipo(c).equals(TipoF.FLOAT)) ||
					(this.ExprDer.getTipo(c).equals(TipoF.FLOAT) && 
						this.ExprIzq.getTipo(c).equals(TipoF.INT)) )
					return TipoF.BOOL;					
				else
					return TipoF.ERROR;
            case MOD:
            case DIVE:
                if (this.ExprDer.getTipo(c).equals(TipoF.INT) && 
						this.ExprIzq.getTipo(c).equals(TipoF.INT))
					return TipoF.INT;
				else
					return TipoF.ERROR;
            case DIVR:
				if ( (this.ExprDer.getTipo(c).equals(TipoF.INT) && 
						this.ExprIzq.getTipo(c).equals(TipoF.INT)) ||
					(this.ExprDer.getTipo(c).equals(TipoF.FLOAT) && 
						this.ExprIzq.getTipo(c).equals(TipoF.FLOAT)) ||
					(this.ExprDer.getTipo(c).equals(TipoF.INT) && 
						this.ExprIzq.getTipo(c).equals(TipoF.FLOAT)) ||
					(this.ExprDer.getTipo(c).equals(TipoF.FLOAT) && 
						this.ExprIzq.getTipo(c).equals(TipoF.INT)) )
					return TipoF.FLOAT;
				else 
					return TipoF.ERROR;
            case SUMA:
            case RESTA:
            case MULT:
                if (this.ExprDer.getTipo(c).equals(TipoF.INT) && 
						this.ExprIzq.getTipo(c).equals(TipoF.INT))
					return TipoF.INT;
				else if ( (this.ExprDer.getTipo(c).equals(TipoF.FLOAT) && 
							this.ExprIzq.getTipo(c).equals(TipoF.FLOAT)) ||
					(this.ExprDer.getTipo(c).equals(TipoF.INT) && 
						this.ExprIzq.getTipo(c).equals(TipoF.FLOAT)) ||
					(this.ExprDer.getTipo(c).equals(TipoF.FLOAT) && 
						this.ExprIzq.getTipo(c).equals(TipoF.INT)) )
					return TipoF.FLOAT;
				else 
					return TipoF.ERROR;
			default: 
				return TipoF.ERROR;
            
        }       
    }    
    public boolean esCorrecta(Bloque c, int line) {
        if ( (this.getTipo(c)).equals(TipoF.ERROR) ) {			
			return false;
		} else {
			return true;
		}
    
    }

}

/**
 * Clase que representa Expresiones cuyo operador es unario
 */
class ExprUna extends Expresion {
    
    //Operador unario de la expresion
    private OperadorU Op;    
    //Sub-Expresion a la que se le aplica el operador unario
    private Expresion E;
    
    /**
	* Constructor de expresiones cuyo operador es unario
	* @param O operador unario
	* @param Exp subexpresion a la que se le aplica el operador
	*/
    public ExprUna (OperadorU O, Expresion Exp) {
        this.Op = O;
        this.E = Exp;
    }
    public String toString(){
        return "" + Op + E;
    }	
	public String imprimir(int i){
        return "" + Op + E;
    }    
    public boolean esCorrecta(Bloque c, int line) {        
        if (this.getTipo(c).equals(TipoF.ERROR))
			return false;
		else
			return true;
    }
    public TipoF getTipo(Bloque c) {
        switch (this.Op){
            case PISO:
            case REDONDEO:
            case TECHO:
                if(this.E.getTipo(c).equals(TipoF.FLOAT)){
                    return TipoF.INT;
                } else {
					return TipoF.ERROR;
				}
            case NOT:
                if(this.E.getTipo(c).equals(TipoF.BOOL)){
					return TipoF.BOOL;
				} else {
					return TipoF.ERROR;
				}
            case MENOS:
                if(this.E.getTipo(c).equals(TipoF.FLOAT) || 
					this.E.getTipo(c).equals(TipoF.INT)){
                    return this.E.getTipo(c);
                }else{
                    return TipoF.ERROR;
                }
			default: 
				return TipoF.ERROR;
        }
    }
}

/**
 * Clase que representa a Expresiones atomicas
 */
class Factor extends Expresion {
    
    //Tipo de la Expresion
    private TipoF tipo;    
    //Valor de la Expresion
    private Object valor;
    
    /**
	* Constructor de Expresiones Atomicas
	* @param t tipo de la Expresion
	* @param v valor de la Expresion
	*/
    public Factor (TipoF t, Object v){
        this.tipo = t;
        this.valor = v; 
    }    
    public String toString(){
        return valor.toString();
    }	
	public String imprimir(int i){
        return valor.toString()+"\n";
    }	
    public boolean esCorrecta(Bloque c, int line) {       
        if(this.tipo == tipo.ID && !c.estaDefinida((String)this.valor)){
            System.out.println("ERROR (linea "+line+") Variable '"+
			((String)this.valor) + "' no ha sido definida.");
			Informacion info = 
				new Informacion((String)this.valor,TipoF.ID,null,2);
			c.getTS().add((String)this.valor, info);
            return false;
        }else{           
            return true;            
        }
    }
    public TipoF getTipo(Bloque c) {
		if (this.tipo.equals(TipoF.ID)) {
			return c.getTS().get((String)this.valor).tipo;
		} else 		
			return this.tipo;        
    }
}

class Arreglo extends Expresion {
      
    //Tipo del arreglo
    private TipoF tipo;	
	//Tamaño del arreglo
	private int tam;
	//Lista de Elementos
    private LinkedList<Expresion> listaElem;
	    
    /**
	* Constructor de Expresiones Atomicas
	* @param t tipo de la Expresion
	* @param v valor de la Expresion
	*/
    public Arreglo (TipoF t, int tam, LinkedList<Expresion> listaElem){
        this.tipo = t;
        this.tam = tam; 
		this.listaElem = listaElem;
    }    
    public String toString(){
        return "" + listaElem;
    }	
	public String imprimir(int i){
        return "["+listaElem.toString()+"]\n";
    }	
    public boolean esCorrecta(Bloque c, int line) {                      
            return true;        
    }
    public TipoF getTipo(Bloque c) {
		return this.tipo;        
    }
}