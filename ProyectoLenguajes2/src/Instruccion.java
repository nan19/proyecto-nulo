import java.io.*;
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
 * Instruccion.java: clase Instruccion.
 * 
 */

/**
 * Clase abstracta que sirve de Superclase a todas las clases que representan
 * instrucciones del Lenguaje
 */
abstract class Inst {
    /**
	* Metodo desarrollado por conveniencia. Imprime la representacion en
	* <b>String</b> del objeto.
	*/
    public abstract String imprimir(int i);    
    public abstract boolean esCorrecta(Bloque c, Informacion info, int linea);    
}

/**
 * Clase que representa una Declaracion de variable
 */
class Decl extends Inst {
        
    //Nombre del tipo de la variable declarada
    private Tipo Tipo;    
    //Nombre de la variable declarada
    private String Var;
    
    /**
	* @param t Tipo de la variable declarada
	* @param v Nombre de la variable declarada
	*/
    public Decl(Tipo t, String v) {
        this.Tipo = t;
        this.Var = v;
    }
	public String imprimir(int i) {
        return "";
    }	
    public String toString() {
        return Tipo +" "+ Var+";";
    }
    public boolean esCorrecta(Bloque c, Informacion info, int linea) {  		
		if( (c.getTS()).isDefinedLocally(this.Var) && 
				((((c.getTS()).getLocally(this.Var)).getStatus()) == 0) ) {
			System.out.println("ERROR (linea "+linea+") Variable '"+this.Var
				+"' ya esta definida.");			
			info.setStatus(1);
			c.getTS().add(this.Var ,info);
			return false;
		} else 
			return true;
    }
    public void setParent(Bloque c) {
    }
}

/**
 * Clase que representa una instruccion de asignacion
 */
class InstAsig extends Inst {
    
    //Nombre de la variable a la que se realiza la asignacion
    private LValue Variable;    
    //Expresion que se le asigna a la variable
    private Expresion E;
    
    /**
	* Constructor de una Instruccion de Asignacion
	* @param v identificador de la variable a ser asignada
	* @param e expresion a asignar a la variable
	*/
    public InstAsig (LValue v, Expresion e) {
        this.Variable = v;
        this.E = e;
    }
    public String imprimir(int i){
        String s = "";
		for (int j=0;j<i;j++) { s+="-"; }
		s+= "Asignacion\n";
		for (int j=0;j<i;j++) { s+="-"; }
		s+= "-" + Variable + "\n";
		for (int j=0;j<i;j++) { s+="-"; }
		s+= "-" + E.imprimir(i+1);
		return s;
    }
    public String toString(){
        return Variable + " := " + E.toString() + ";";
    }		
    public boolean esCorrecta(Bloque c, Informacion info, int linea) {
       	boolean ok = true;
		//Chequeo de variable definida.
		String var = this.Variable.obtenerId();
        if(!c.estaDefinida(var)){
			System.out.println("ERROR (linea "+linea+") Variable '"+var +"' no ha sido definida.");
			info.setTipo(TipoF.NODEF);
			c.getTS().add(var,info);
            ok = false;
        }
		Tipo tipoE = this.E.getTipo(c);		
		Tipo tipoV = c.getTS().get(var).tipo;
		//chequeo de la expresion.
		if (tipoE.equals(TipoF.ERROR)) {
			System.out.println("ERROR (linea "+linea+") Error de tipo en la expresion "+this.E+".");
			ok = false;
		}
		//chequeo de compatibilidad entre la expresion y la variable
		if (!((TBasico)tipoV).tipo.equals(TipoF.NODEF)) {
			if (((TBasico)tipoV).tipo.equals(TipoF.FLOAT)) {
				if ( !(((TBasico)tipoE).tipo.equals(TipoF.FLOAT) || ((TBasico)tipoE).tipo.equals(TipoF.INT)) ) {
					System.out.println("ERROR (linea "+linea+") La Asignacion a la variable '"+this.Variable+
						"' no es posible. Los tipos no son compatibles.");
					ok = false;
				}
			} else if (!(((TBasico)tipoV).tipo.equals(((TBasico)tipoE).tipo))) {
				System.out.println("ERROR (linea "+linea+") La Asignacion a la variable '"+var+
						"' no es posible. Los tipos no son compatibles.");
				ok = false;
			}
		}
		return ok;  	
    }
}

/**
 * Clase que representa una instruccion condicional simple
 */
class InstIf extends Inst {
    
    //Condicion del la instruccion
    protected Expresion Condicion;    
    //Instrucciones a ejecutar si la condicion se cumple
    protected Bloque inst;
    
    /**
	* Constructor de una expresion condicional
	* @param e Expresion de la Condicion
	* @Param l Instrucciones a ejecutar si el condicional se cumple
	*/
    public InstIf (Expresion e, Bloque c) {
        this.Condicion = e;
        this.inst = c;
    }
    public String imprimir(int i){
        String s = "";
		for (int j=0;j<i;j++) { s+="-"; }
		s+= "Condicional\n";
		for (int j=0;j<i;j++) { s+="-"; }
		s+= "-" + Condicion.imprimir(i+1);		
		s+= inst.imprimir(i);
		for (int j=0;j<i;j++) { s+="-"; }
		s+= "-"+inst.getTS();
		return s;
    }    
    public String toString(){
        return "si "+Condicion.toString()+"\n"+inst+"fins;";
    }
    public boolean esCorrecta(Bloque c, Informacion info, int linea) {
        return this.Condicion.esCorrecta(c, 0);
    }
}

/**
 * Clase que representa un condicional con instrucciones alternativas
 */
class InstIfElse extends InstIf {
    
    //Intrucciones a ejecutar si la Condicion no es cierta
    private Bloque instElse;
	
    /**
	* Constructor de la instruccion condicional con instrucciones alternativas
	* @param e Condicion
	* @param l1 instrucciones a ejecutar si la Condicion es cierta
	* @param l2 instrucciones a ejecutar si la condicion es falsa
	*/
    public InstIfElse (Expresion e, Bloque c1, Bloque c2) {
        super(e,c1);
        this.instElse = c2;
    }
     public String imprimir(int i){
        String s = "";
		for (int j=0;j<i;j++) { s+="-"; }
		s+= "Condicional\n";
		for (int j=0;j<i;j++) { s+="-"; }
		s+= "-" + Condicion.imprimir(i+1);
		for (int j=0;j<i;j++) { s+="-"; }
		s+= "-Inst Si\n" + inst.imprimir(i+1);
		for (int j=0;j<i;j++) { s+="-"; }
		s+= "-Inst Sino\n" + instElse.imprimir(i+1);
		for (int j=0;j<i;j++) { s+="-"; }
		s+= "-"+instElse.getTS();
		return s;
    }	
    public String toString() {
        return super.toString().replaceFirst("fins","sino\n"+instElse+"fins");
    }
}

/**
 * Clase que representa una instruccion iterativa
 */
class InstDo extends Inst {
    
    //Condicion de la Iteracion
    private Expresion Condicion;    
    //Cuerpo de la iteracion
    private Bloque inst;
    
    /**
	* Constructor de la Instruccion de Iteracion
	* @param e Condicion de la iteracion
	* @param l Cuerpo de la Iteracion
	*/
    public InstDo (Expresion e, Bloque c) {
        this.Condicion = e;
        this.inst = c;
    }
    public String imprimir(int i){
        String s = "";
		for (int j=0;j<i;j++) { s+="-"; }
		s+= "Iteracion\n";
		for (int j=0;j<i;j++) { s+="-"; }
		s+= "-" + Condicion.imprimir(i+1);		
		s+= inst.imprimir(i);
		for (int j=0;j<i;j++) { s+="-"; }
		s+= "-"+inst.getTS();
		return s;
    }    
    public String toString() {
        return "Hacer " + Condicion +"\n"+inst+"finh;";
    }
    public boolean esCorrecta(Bloque c, Informacion info, int linea) {
        return this.Condicion.esCorrecta(c, 0);
    }
}

/**
 * Clase que representa una instruccion de llamada a un procedimiento
 */
class InstProc extends Inst {
    
    //Identificador del procedimiento
    private String id;    
    //Lista de parametros
    private LinkedList<Expresion> param;
    
    /**
	* Constructor de la Instruccion de Llamada a Procedimientos
	* @param id Identificador del procedimiento
	* @param param Lista de parametros
	*/
    public InstProc (String id, LinkedList<Expresion> param) {
        this.id = id;
        this.param = param;
    }
    public String imprimir(int i){
        String s = "";
		for (int j=0;j<i;j++) { s+="-"; }
		s+= "Llamada a procedimiento\n";
		for (int j=0;j<i;j++) { s+="-"; }
		s+= "-" + "Identificador\n";
		for (int j=0;j<i;j++) { s+="-"; }
		s+= "--" + id +"\n";
		for (int j=0;j<i;j++) { s+="-"; }
		s+= "-Lista de Parametros\n";
		for (int j=0;j<i;j++) { s+="-"; }
		s+="--";
		s+="(";
		for (int j=0;j<param.size();j++) {
			s+= param.get(j) +", ";
		}		
		s+= ")\n";
		return s;
    }    
    public String toString() {
        return "Llamada a procedimiento" + id +"\n"+param+";";
    }
    public boolean esCorrecta(Bloque c, Informacion info, int linea) {
        return true;
    }
}
