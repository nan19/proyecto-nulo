import com.sun.org.apache.bcel.internal.generic.SWITCH;
import java.io.*;
import java.lang.*;
import java.util.*;

/**
 * Universidad Simon Bolivar
 * Lenguajes de Programacion II
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
enum TipoF {INT,FLOAT,ID,BOOL}


enum TipoV {INT,FLOAT,BOOL,TYPE,FUN,PROC,USER}

/**
 * Clase abstracta que sirve como Superclase para los diversos tipos de 
 * expresiones del lenguaje
 */
abstract class Expresion {
    
	public String tipo;
    /**
     * Metodo desarrollado por conveniencia. Imprime la representacion en
     * <b>String</b> del objeto.
     */
    public abstract String imprimir(int i);
    
    public abstract boolean esCorrecta(Bloque c);
    
    public abstract String getTipo(Bloque c);
    
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
	
	public String tipo;
    
    //private Bloque control;
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

    public String getTipo(Bloque c) {
        switch(this.Op){
            case AND:
            case OR:
				if (this.ExprDer.getTipo(c).equals("booleano") && this.ExprIzq.getTipo(c).equals("booleano")) {
					return "booleano";
				} else {
					return "error";
				}
            case IGUAL:
            case DESIGUAL:
				if (this.ExprDer.getTipo(c).equals("entero") && this.ExprIzq.getTipo(c).equals("entero")) {
					return "booleano";
				} else if (this.ExprDer.getTipo(c).equals("real") && this.ExprIzq.getTipo(c).equals("real")) {
					return "booleano";					
				} else if (this.ExprDer.getTipo(c).equals("booleano") && this.ExprIzq.getTipo(c).equals("booleano")) {
					return "booleano";
				} else {
					return "error";
				}
            case MAYOR:
            case MENOR:
            case MAYORIGUAL:
            case MENORIGUAL:
                if (this.ExprDer.getTipo(c).equals("entero") && this.ExprIzq.getTipo(c).equals("entero")) {
					return "booleano";
				} else if (this.ExprDer.getTipo(c).equals("real") && this.ExprIzq.getTipo(c).equals("real")) {
					return "booleano";					
				} else {
					return "error";
				}
            case MOD:
            case DIVE:
                if (this.ExprDer.getTipo(c).equals("entero") && this.ExprIzq.getTipo(c).equals("entero")) {
					return "entero";
				} else {
					return "error";
				}
            case DIVR:
            case SUMA:
            case RESTA:
            case MULT:
                if(this.ExprDer.getTipo(c).equals("real") && this.ExprIzq.getTipo(c).equals("real")){
                    return "real";
                } else if(this.ExprDer.getTipo(c).equals("entero") && this.ExprIzq.getTipo(c).equals("entero")){
                    return "entero";
                } else if(this.ExprDer.getTipo(c).equals("real") && this.ExprIzq.getTipo(c).equals("entero")){
                    return "real";
                } else if(this.ExprDer.getTipo(c).equals("entero") && this.ExprIzq.getTipo(c).equals("real")){
                    return "real";
                } else {
					return "error";
				}
			default: 
				return "error";
            
        }       
    }    

    public boolean esCorrecta(Bloque c) {
        if ( (this.getTipo(c)).equals("error") ) {
			System.out.println("Error: tipos no compatibles");
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
    
    public boolean esCorrecta(Bloque c) {
        
        if (this.E.esCorrecta(c)){
            String tipoEsperado = "";
            switch (this.Op){
                case PISO:
                case REDONDEO:
                case TECHO:
                    tipoEsperado = "real";
                    break;
                case NOT:
                    tipoEsperado = "booleano";
                    break;
                case MENOS:
                    tipoEsperado = "real o entero";
                    break;
            }
            if(tipoEsperado.contains(this.E.getTipo(c))){
                this.showError(this.toString(),tipoEsperado, this.E.getTipo(c));
                return false;
            }else{
                return true;
            }
            
        }else{
            return false;
        }
    }

    public String getTipo(Bloque c) {
        switch (this.Op){
            case PISO:
            case REDONDEO:
            case TECHO:
                if(this.E.getTipo(c).equals("real")){
                    return "entero";
                } else {
					return "error";
				}
            case NOT:
                if(this.E.getTipo(c).equals("booleano")){
					return "booleano";
				} else {
					return "error";
				}
            case MENOS:
                if(this.E.getTipo(c).equals("real") || this.E.getTipo(c).equals("entero")){
                    return this.E.getTipo(c);
                }else{
                    return "error";
                }
			default: 
				return "error";
        }
    }
    

}

/**
 * Clase que representa a Expresiones atomicas
 */
class Factor extends Expresion {
    
    private Bloque control;
    
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

    public boolean esCorrecta(Bloque c) {
        //return c.estaDefinida((String)this.valor);
        if(this.tipo == tipo.ID && !c.estaDefinida((String)this.valor)){
            System.out.println(""+((String)this.valor) + " no esta definida");
            return false;
        }else{
            //System.out.println("Valor correcto: "+this.valor.toString());
            return true;
            
        }
    }

    public String getTipo(Bloque c) {
        switch (this.tipo){
            case BOOL:
                return "booleano";
            case FLOAT:
                return "real";
            case INT:
                return "entero";
            case ID:
                Informacion info = c.getInfo((String)this.valor);
                if(info != null){
                    return info.getTipo();
                }else{
                    return "error";
                }
                
        }
        return "error";
    }


}

