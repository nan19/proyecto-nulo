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
    
    public abstract boolean esCorrecta(Bloque c);
    
    public abstract void setParent(Bloque c);

}

/**
 * Clase que representa una Declaracion de variable
 */
class Decl extends Inst {
        
    //Nombre del tipo de la variable declarada
    private String Tipo;
    
    //Nombre de la variable declarada
    private String Var;
    
    /**
     * Constructor de una instruccion de Declaracion
     * @param t Tipo de la variable declarada
     * @param v Nombre de la variable declarada
     */
    public Decl(String t, String v) {
        this.Tipo = t;
        this.Var = v;
    }

	public String imprimir(int i) {
        return "";
    }
	
    public String toString() {
        return Tipo +" "+ Var+";";
    }

    public boolean esCorrecta(Bloque c) {
        if ( (c.getTS()).isDefinedLocally(this.Var) ) {
			System.out.println("Variable "+this.Var+" ya esta definida");
			return false;
		} else {
			return true;
		}
    }
    public void setParent(Bloque c) {
    }
}

/**
 * Clase que representa una instruccion de asignacion
 */
class InstAsig extends Inst {
    
    //Nombre de la variable a la que se realiza la asignacion
    private String Variable;
    
    //Expresion que se le asigna a la variable
    private Expresion E;
    
    /**
     * Constructor de una Instruccion de Asignacion
     * @param v identificador de la variable a ser asignada
     * @param e expresion a asignar a la variable
     */
    public InstAsig (String v, Expresion e) {
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

    public boolean esCorrecta(Bloque c) {
        if(!c.estaDefinida(this.Variable)){
            System.out.println("Variable "+this.Variable+" no ha sido definida");
            E.esCorrecta(c);
            return false;
        }else{
            return E.esCorrecta(c);
        }
        
    }

    public void setParent(Bloque c) {
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
		return s;
    }
    
    public String toString(){
        return "si "+Condicion.toString()+"\n"+inst+"fins;";
    }

    public boolean esCorrecta(Bloque c) {
        return this.Condicion.esCorrecta(c);
    }

    public void setParent(Bloque c) {
        this.inst.setParent(c);
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
		return s;
    }
	
    public String toString() {
        return super.toString().replaceFirst("fins","sino\n"+instElse+"fins");
    }

    public void setParent(Bloque c) {
        super.setParent(c);
        this.instElse.setParent(c);
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
		return s;
    }
    
    public String toString() {
        return "Hacer " + Condicion +"\n"+inst+"finh;";
    }

    public boolean esCorrecta(Bloque c) {
        return this.Condicion.esCorrecta(c);
    }

    public void setParent(Bloque c) {
        this.inst.setParent(c);
    }
    

}