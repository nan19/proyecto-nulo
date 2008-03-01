//PRUEBA

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
 * Control.java: Especificacion de las distintas clases necesarias 
 * para generar el Arbol Abstracto y la Tabla de Simbolos
 * asociados al analisis sintactico.
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
 * Enumeracion que contiene los tipos basicos del lenguaje
 */
enum Tipo {INT,FLOAT,ID,BOOL}


/**
 * Clase abstracta que sirve como Superclase para los diversos tipos de 
 * expresiones del lenguaje
 */
abstract class Expresion {
    
    /**
     * Metodo desarrollado por conveniencia. Imprime la representacion en
     * <b>String</b> del objeto.
     */
    public void imprimir(){
        System.out.println(this.toString());
    }
    
    /**
     * Metodo que retorna los identificadores usados en esta expresion
     * @return Una lista de los identificadores usados en esta expresion
     */
    public abstract List<String> getIdentificadores();
    
    void ids(){
        Iterator i = this.getIdentificadores().iterator();
        while(i.hasNext()){
            System.out.print(i.next());
        }
        System.out.println("");
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

    public List<String> getIdentificadores() {
        List<String> l = new LinkedList<String>();
        l.addAll(this.ExprIzq.getIdentificadores());
        l.addAll(this.ExprDer.getIdentificadores());
        return l;
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

    public List<String> getIdentificadores() {
        return this.E.getIdentificadores();
    }
    
    
}

/**
 * Clase que representa a Expresiones atomicas
 */
class Factor extends Expresion {
    
    //Tipo de la Expresion
    private Tipo tipo;
    
    //Valor de la Expresion
    private Object valor;
    
    //Lista de identificadores en esta expresion
    private List<String> l;
    /**
     * Constructor de Expresiones Atomicas
     * @param t tipo de la Expresion
     * @param v valor de la Expresion
     */
    public Factor (Tipo t, Object v){
        this.tipo = t;
        this.valor = v;
        this.l = new LinkedList<String>();
    }
    
    public String toString(){
        return valor.toString();
    }

    /**
     * Agrega un identificador a la lista de identificadores
     * @param s Identificador a agregar
     * @return <b>true</b> si se realizo la operacion, <b>false</b> sino.
     */
    public boolean addID(String s){
        return this.l.add(s);
    }
    
    public List<String> getIdentificadores() {
        return this.l;
    }
    
}

/**
 * Clase abstracta que sirve de Superclase a todas las clases que representan
 * instrucciones del Lenguaje
 */
abstract class Inst {
    /**
     * Metodo desarrollado por conveniencia. Imprime la representacion en
     * <b>String</b> del objeto.
     */
    public void imprimir(){
        System.out.println(this.toString());
    }
}

/**
 * Clase que representa una Declaracion de variable
 */
class InstDecl extends Inst {
        
    //Nombre del tipo de la variable declarada
    private String Tipo;
    
    //Nombre de la variable declarada
    private String Var;
    
    /**
     * Constructor de una instruccion de Declaracion
     * @param t Tipo de la variable declarada
     * @param v Nombre de la variable declarada
     */
    public InstDecl(String t, String v) {
        this.Tipo = t;
        this.Var = v;
    }

    public String toString() {
        return Tipo +" "+ Var+";";
    }

}

/**
 * Clase que representa las declaraciones de variables y su asignacion
 * simultanea
 */
class InstDeclAsig extends Inst {
    
    //Nombre del Tipo de la variable declarada
    private String Tipo;
    
    //Nombre de la variable declarada
    private InstAsig I;
    
    /**
     * Constructor de intrucciones de declaracion y asignacion simultanea
     * @param t Tipo de la variable declarada
     * @param i Instruccion de Asignacion
     */
    public InstDeclAsig(String t, InstAsig i) {
        this.Tipo = t;
        this.I = i;
    }

    public String toString() {
        return Tipo+" "+I.toString();
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
    
    public String toString(){
        return Variable + " := " + E.toString() + ";";
    }
}

/**
 * Clase que representa una instruccion condicional simple
 */
class InstIf extends Inst {
    
    //Condicion del la instruccion
    private Expresion Condicion;
    
    //Instrucciones a ejecutar si la condicion se cumple
    private Control inst;
    
    /**
     * Constructor de una expresion condicional
     * @param e Expresion de la Condicion
     * @Param l Instrucciones a ejecutar si el condicional se cumple
     */
    public InstIf (Expresion e, Control c) {
        this.Condicion = e;
        this.inst = c;
    }
    
    public String toString(){
        return "si "+Condicion.toString()+"\n"+inst+"fins;";
    }
}

/**
 * Clase que representa un condicional con instrucciones alternativas
 */
class InstIfElse extends InstIf {
    
    //Intrucciones a ejecutar si la Condicion no es cierta
    private Control instElse;
    /**
     * Constructor de la instruccion condicional con instrucciones alternativas
     * @param e Condicion
     * @param l1 instrucciones a ejecutar si la Condicion es cierta
     * @param l2 instrucciones a ejecutar si la condicion es falsa
     */
    public InstIfElse (Expresion e, Control c1, Control c2) {
        super(e,c1);
        this.instElse = c2;
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
    private Control inst;
    
    /**
     * Constructor de la Instruccion de Iteracion
     * @param e Condicion de la iteracion
     * @param l Cuerpo de la Iteracion
     */
    public InstDo (Expresion e, Control c) {
        this.Condicion = e;
        this.inst = c;
    }
    
    public String toString() {
        return "Hacer " + Condicion +"\n"+inst+"finh;";
    }

}

/**
 * Clase que envuelve a la lista de instrucciones y la tabla de simbolos del
 * programa
 */
class Control{
    
    //Lista de Instrucciones
    private List<Inst> inst;
        
    //Tabla de Simbolos
    private HashMap<String,String> tabla;
    
    /**
     * Escribe en consola tanto las instrucciones del programa
     * como la tabla de simbolos
     */
    public void imprimir(){
        System.out.println(this.toString());
        
    }
    
    /**
     * Agrega los contenidos de otro objeto <b>Control</b> a este
     * @param c otro objeto de tipo <b>Control</b>
     */
    public void agregar(Control c){
        this.inst.addAll(c.inst);
        // quizas hacer esto con un mejor chequeo
        this.tabla.putAll(c.tabla);
    }
    
    /**
     * Agrega una instruccion a la lista de instrucciones
     * @param i una Instruccion a agregar
     */
    public void agregarInst(Inst i){
        this.inst.add(i);
    }
    
    /**
     * Agrega una variable y su tipo a la tabla de simbolos
     * @param id Nombre de la variable
     * @param tipo Tipo de la variable
     */
    public void agregarID(String id, String tipo){
        this.tabla.put(id,tipo);
    }
    
    /**
     * Verifica si una variable ya esta definida en la Tabla de Simbolos
     * @param s Nombre de la variable a verificar
     * @return <b>true</b> si esta definida, <b>false</b> sino.
     */
    public boolean estaDefinida(String s){
        return this.tabla.containsKey(s);
    }
    
    /**
     * Constructor del envoltorio para la Lista de Instrucciones y para la Tabla
     * de Simbolos
     */
    Control(){
        this.tabla = new HashMap<String,String>();
        this.inst = new LinkedList<Inst>();
    }
    
    /**
     * Constructor del envoltorio para la Lista de Instrucciones y para la Tabla
     * de Simbolos. Agrega una instrucciona al nuevo objeto.
     * @param i Instruccion a agregar
     */
    Control(Inst i){
        this();
        this.agregarInst(i);
    }
    
    /**
     * Constructor del envoltorio para la Lista de Instrucciones y para la Tabla
     * de Simbolos. Agrega una instrucciona al nuevo objeto y una declaracion.
     * @param i Instruccion a agregar
     * @param id Nombre de la variable
     * @param tipo Tipo de la variable
     */
    Control(Inst i, String id, String tipo){
        this(i);
        this.agregarID(id,tipo);
    }

    public String toString() {
        Inst i;
        String acum = "";
        for(int j=0; j<this.inst.size(); j++){
            i = this.inst.get(j);
            acum+= i.toString()+"\n";
            
        }
	acum += "Tabla:\n";
	Iterator k = this.tabla.keySet().iterator();
        Iterator v = this.tabla.values().iterator();
        int j = 0;
        while(k.hasNext()){
            acum += ""+(j+1)+" "+k.next()+ " : "+v.next()+"\n";
            j++;
        }
        return acum;
    }

}

/**
 * Clase que envuelve los valores Booleanos
 */
class Booleano{
    //valor del objeto
    private boolean b;
    
    /**
     * Constructor de Objeto Booleano
     */
    public Booleano(boolean b0){
        this.b = b0;
    }
    
    public String toString() {
        return (this.b)?"cierto":"falso";
    }
}