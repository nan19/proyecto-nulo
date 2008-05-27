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
    SUMA("+") {
        public TipoF getTipo(Expresion e1, Expresion e2){
            return TipoF.ERROR;}
    }, 
    
    RESTA("-") {
        public TipoF getTipo(Expresion e1, Expresion e2){
            return TipoF.ERROR;}
    }, 
    
    MULT("*") {
        public TipoF getTipo(Expresion e1, Expresion e2){
            return TipoF.ERROR;}
    }, 
    
    DIVR("/") {
        public TipoF getTipo(Expresion e1, Expresion e2){
            return TipoF.ERROR;}
    }, 
    
    DIVE("div") {
        public TipoF getTipo(Expresion e1, Expresion e2){
            return TipoF.ERROR;}
    }, 
    
    MOD("mod") {
        public TipoF getTipo(Expresion e1, Expresion e2){
            return TipoF.ERROR;}
    }, 
    
    AND("&") {
        public TipoF getTipo(Expresion e1, Expresion e2){
            return TipoF.ERROR;}
    }, 
    
    OR("|") {
        public TipoF getTipo(Expresion e1, Expresion e2){
            return TipoF.ERROR;}
    }, 
    
    IGUAL("=") {
        public TipoF getTipo(Expresion e1, Expresion e2){
            return TipoF.ERROR;}
    }, 
    
    DESIGUAL("!=") {
        public TipoF getTipo(Expresion e1, Expresion e2){
            return TipoF.ERROR;}
    }, 
    
    MAYOR(">") {
        public TipoF getTipo(Expresion e1, Expresion e2){
            return TipoF.ERROR;}
    }, 
    
    MENOR("<") {
        public TipoF getTipo(Expresion e1, Expresion e2){
            return TipoF.ERROR;}
    }, 
    
    MAYORIGUAL(">=") {
        public TipoF getTipo(Expresion e1, Expresion e2){
            return TipoF.ERROR;}
    }, 
    
    MENORIGUAL("<=") {
        public TipoF getTipo(Expresion e1, Expresion e2){
            return TipoF.ERROR;}
    }, 
    
    INDICE("[]") {
        public TipoF getTipo(Expresion e1, Expresion e2){
            return TipoF.ERROR;}
    }, 
    
    MIEMBRO(".") {
        public TipoF getTipo(Expresion e1, Expresion e2){
            return TipoF.ERROR;}
    };
    
    //Variable que almacena el valor de la representacion en <b>String</b>
    private String s;
    public String toString() {
        return this.s;
    }
    
    public abstract TipoF getTipo(Expresion e1, Expresion e2);
    /**
	* Constructor usado implicitamente para asociar cada operador con su String
	* @param str de tipo <b>String</b> representacion del operador
	*/
    OperadorB(String str){
        this.s = str;
    }
    
    private TipoF getTipoAuxEnteroOReal(Expresion e1, Expresion e2, Bloque c){
        return TipoF.ERROR;
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
 * Clase abstracta que sirve como Superclase para los diversos tipos de 
 * expresiones del lenguaje
 */
public abstract class Expresion {

    /**
	* Metodo desarrollado por conveniencia. Imprime la representacion en
	* <b>String</b> del objeto.
	*/
    public abstract String imprimir(int i);    
    public abstract boolean esCorrecta(Bloque c, int line);    
    public abstract Tipo getTipo(Bloque c);
    void showError(String e, String tipoEsperado, String tipoHallado){
        String error = "Error de tipo en la expresion"+e+" se esperaba ";
        error += tipoEsperado+" y se hallo "+tipoHallado;
        System.out.println(error);
    }  
    
    public abstract String toCode(String yes, String no);
    
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
    public Tipo getTipo(Bloque c) {
        //return new TBasico(TipoF.ERROR);
        Tipo b = new TBasico(TipoF.BOOL);
		Tipo i = new TBasico(TipoF.INT);
		Tipo f = new TBasico(TipoF.FLOAT);
		
        switch(this.Op){
            case AND:
            case OR:				
				if (this.ExprDer.getTipo(c).equals(b) && 
						this.ExprIzq.getTipo(c).equals(b)) {
					return new TBasico(TipoF.BOOL);
				} else {					
					return new TBasico(TipoF.ERROR);
				}
            case IGUAL:
            case DESIGUAL:				
				if ( (this.ExprDer.getTipo(c).equals(i) && 
						this.ExprIzq.getTipo(c).equals(i)) ||
					(this.ExprDer.getTipo(c).equals(f) && 
						this.ExprIzq.getTipo(c).equals(f)) ||
					(this.ExprDer.getTipo(c).equals(i) && 
						this.ExprIzq.getTipo(c).equals(f)) ||
					(this.ExprDer.getTipo(c).equals(f) && 
						this.ExprIzq.getTipo(c).equals(i)) ||
					(this.ExprDer.getTipo(c).equals(b) && 
						this.ExprIzq.getTipo(c).equals(b)) )
					return new TBasico(TipoF.BOOL);
				else
					return new TBasico(TipoF.ERROR);
            case MAYOR:
            case MENOR:
            case MAYORIGUAL:
            case MENORIGUAL:
                if ( (this.ExprDer.getTipo(c).equals(i) && 
						this.ExprIzq.getTipo(c).equals(i)) ||
					(this.ExprDer.getTipo(c).equals(f) && 
						this.ExprIzq.getTipo(c).equals(f)) ||
					(this.ExprDer.getTipo(c).equals(i) && 
						this.ExprIzq.getTipo(c).equals(f)) ||
					(this.ExprDer.getTipo(c).equals(f) && 
						this.ExprIzq.getTipo(c).equals(i)) )
					return new TBasico(TipoF.BOOL);
				else
					return new TBasico(TipoF.ERROR);
            case MOD:
            case DIVE:			
                if (((TBasico)(this.ExprDer.getTipo(c))).tipo.equals(TipoF.INT) && 
						((TBasico)(this.ExprIzq.getTipo(c))).tipo.equals(TipoF.INT))
					return new TBasico(TipoF.INT);
				else					
					return new TBasico(TipoF.ERROR);
            case DIVR:
				if ( (this.ExprDer.getTipo(c).equals(i) && 
						this.ExprIzq.getTipo(c).equals(i)) ||
					(this.ExprDer.getTipo(c).equals(f) && 
						this.ExprIzq.getTipo(c).equals(f)) ||
					(this.ExprDer.getTipo(c).equals(i) && 
						this.ExprIzq.getTipo(c).equals(f)) ||
					(this.ExprDer.getTipo(c).equals(f) && 
						this.ExprIzq.getTipo(c).equals(i)) )
					return new TBasico(TipoF.FLOAT);
				else
					return new TBasico(TipoF.ERROR);
            case SUMA:
            case RESTA:
            case MULT:
                if (((TBasico)(this.ExprDer.getTipo(c))).tipo.equals(TipoF.INT) && 
						((TBasico)(this.ExprIzq.getTipo(c))).tipo.equals(TipoF.INT))
					return new TBasico(TipoF.INT);
				else if ( (((TBasico)(this.ExprDer.getTipo(c))).tipo.equals(TipoF.FLOAT) && 
							((TBasico)(this.ExprIzq.getTipo(c))).tipo.equals(TipoF.FLOAT)) ||
					(((TBasico)(this.ExprDer.getTipo(c))).tipo.equals(TipoF.INT) && 
						((TBasico)(this.ExprIzq.getTipo(c))).tipo.equals(TipoF.FLOAT)) ||
					(((TBasico)(this.ExprDer.getTipo(c))).tipo.equals(TipoF.FLOAT) && 
						((TBasico)(this.ExprIzq.getTipo(c))).tipo.equals(TipoF.INT)) )
					return new TBasico(TipoF.FLOAT);
				else
					return new TBasico(TipoF.ERROR);
					
			default: 
				return new TBasico(TipoF.ERROR);
           
        }         
    }    
	
    public boolean esCorrecta(Bloque c, int line) {
        if ( (this.getTipo(c)).equals(TipoF.ERROR) ) {			
			return false;
		} else {
			return true;
		}
    
    }

    @Override
    public String toCode(String yes, String no) {
        String aux;
        switch(Op){
            case AND:
                aux = Misc.newLabel();
                return ExprIzq.toCode(aux,no)+"\n"+aux+": "+ExprDer.toCode(yes,no);
            case OR:
                aux = Misc.newLabel();
                return ExprIzq.toCode(yes,aux)+"\n"+aux+": "+ExprDer.toCode(yes,no);
        }
        return "\n#Error en toCode ExprBin u operacion no implementada\n";
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
        return "" + "("+ Op + ")" + E + "\n";
    }    
    public boolean esCorrecta(Bloque c, int line) {        
        if (this.getTipo(c).equals(TipoF.ERROR))
			return false;
		else
			return true;
    }
    public Tipo getTipo(Bloque c) {
        return new TBasico(TipoF.ERROR);
        /*
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
        */
    }

    @Override
    public String toCode(String yes, String no) {
        switch(this.Op){
            case NOT:
                return this.E.toCode(no, yes);
        }
        return "\n#Error toCode ExprUna u Operacion no implementada aun\n";
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
		if ((this.tipo).equals(TipoF.LVAL)) {
			return true;
		}
		else {
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
    }
    public Tipo getTipo(Bloque c) {      
        if (this.tipo.equals(TipoF.ID)) {			
            return c.getTS().get((String)this.valor).tipo;
        } else 					
			return new TBasico(this.tipo);
       
        }

    @Override
    public String toCode(String yes, String no) {
        switch(this.tipo){
            case BOOL:
                if(((Booleano)this.valor).getValue()){
                    return "goto "+yes+"\n";
                }else{
                    return "goto "+no+"\n";
                }
        }
        return "\n#Error en Factor toCode o Caso no previsto\n";
    }
    
        
}
abstract class LValue {
	public abstract String toString();
	public abstract String obtenerId();
}
class ElemArreglo extends LValue {
	private LValue arreglo;
	private Expresion indice;
	
	public ElemArreglo (LValue a, Expresion i) {
		this.arreglo = a;
		this.indice = i;
	}
	public String toString() {
		return "" + arreglo + "[" + indice + "]";
	}	
	public String obtenerId() {
		return (this.arreglo).obtenerId();
	}
}
class CampoRegistro extends LValue {
	private LValue registro;
	private String campo;
	private Tipo tipoCampo;	
	
	public CampoRegistro (LValue r, String c) {
		this.registro = r;
		this.campo = c;		
	}
	public String toString() {
		return "" + registro + "." + campo;
	}
	public String obtenerId() {
		return (this.registro).obtenerId();
	}
}
class Identificador extends LValue {
	private String id;	
	public Identificador (String id) {
		this.id = id;
	}
	public String toString() {
		return this.id;
	}
	public String obtenerId() {
		return this.id;
	}
}

class Arreglo extends Expresion {
      
    //Tipo del arreglo
    private TipoF tipo;	
	//Tamanno del arreglo
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
    public Tipo getTipo(Bloque c) {
        return new TBasico(TipoF.ERROR);
		//return this.tipo;        
    }

    @Override
    public String toCode(String yes, String no) {
        //throw new UnsupportedOperationException("Not supported yet.");
        return "\n#Codigo de Arreglo:Expresion\n";
    }
    
    
}