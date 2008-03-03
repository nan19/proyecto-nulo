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
    
    public abstract boolean esCorrecta(Control c);
    
    public abstract String getTipo(Control c);
    
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
    
    //private Control control;
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

    public String getTipo(Control c) {
        switch(this.Op){
            case AND:
            case OR:
            case IGUAL:
            case DESIGUAL:
            case MAYOR:
            case MENOR:
            case MAYORIGUAL:
            case MENORIGUAL:
                return "booleano";
            case MOD:
            case DIVE:
                return "entero";
            case DIVR:
                return "real";
            case SUMA:
            case RESTA:
            case MULT:
                if(this.ExprDer.getTipo(c).equals("real") || this.ExprIzq.getTipo(c).equals("real")){
                    return "real";
                }else{
                    return "entero";
                }
            
        }
        return "error";
    }
    

    public boolean esCorrecta(Control c) {
        if (this.ExprIzq.esCorrecta(c) && this.ExprDer.esCorrecta(c)){
            String tipoEsperado = "error";
            String tipoi = this.ExprIzq.getTipo(c);
            String tipod = this.ExprDer.getTipo(c);
            switch(this.Op){
                case AND:
                case OR:
                    tipoEsperado = "booleano";
                    break;
                case IGUAL:
                case DESIGUAL:
                    if(tipoi.equals(tipod)){
                        return true;
                    }else{
                        tipoEsperado = "entero o real";
                        break;
                    }
                    
                case MOD:
                case DIVE:
                    tipoEsperado = "entero";
                    break;
                case DIVR:
                    tipoEsperado = "real";
                    break;
                case MAYOR:
                case MENOR:
                case MAYORIGUAL:
                case MENORIGUAL:
                case SUMA:
                case RESTA:
                case MULT:
                    tipoEsperado = "entero o real";
                    break;

            }
            
            boolean izq = tipoEsperado.contains(tipoi);
            boolean der = tipoEsperado.contains(tipod);
            
            if( izq && der ){
                return true;
            }else{
                this.showError(this.toString(),tipoEsperado, tipoi+" y "+tipod);
                return false;
            }
        
        }else{
            return false;
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
    
    public boolean esCorrecta(Control c) {
        
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

    public String getTipo(Control c) {
        switch (this.Op){
            case PISO:
            case REDONDEO:
            case TECHO:
                return "entero";
            case NOT:
                return "booleano";
            case MENOS:
                if(this.E.getTipo(c).equals("real")||this.E.getTipo(c).equals("entero")){
                    return this.E.getTipo(c);
                }else{
                    return "error";
                }
        }
        return "error";
    }
    

}

/**
 * Clase que representa a Expresiones atomicas
 */
class Factor extends Expresion {
    
    private Control control;
    
    //Tipo de la Expresion
    private Tipo tipo;
    
    //Valor de la Expresion
    private Object valor;
    
    /**
     * Constructor de Expresiones Atomicas
     * @param t tipo de la Expresion
     * @param v valor de la Expresion
     */
    public Factor (Tipo t, Object v){
        this.tipo = t;
        this.valor = v; 
    }
    
    public String toString(){
        return valor.toString();
    }

    public boolean esCorrecta(Control c) {
        //return c.estaDefinida((String)this.valor);
        if(this.tipo == tipo.ID && !c.estaDefinida((String)this.valor)){
            System.out.println(""+((String)this.valor) + " no esta definida");
            return false;
        }else{
            //System.out.println("Valor correcto: "+this.valor.toString());
            return true;
            
        }
    }

    public String getTipo(Control c) {
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
    
    public abstract boolean esCorrecta(Control c);
    
    public abstract void setParent(Control c);

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

    public boolean esCorrecta(Control c) {
        return true;
    }

    public void setParent(Control c) {
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
    private String Variable;
    
    //Expresion que se le asigna a la variable
    private Expresion E;
    
    /**
     * Constructor de intrucciones de declaracion y asignacion simultanea
     * @param t Tipo de la variable declarada
     * @param i Instruccion de Asignacion
     */
    public InstDeclAsig(String t, String v, Expresion e) {
        this.Tipo = t;
        this.Variable = v;
        this.E =e;
    }

    public String toString() {
        return Tipo+" "+Variable+" := "+E+";";
    }

    public boolean esCorrecta(Control c) {
        return E.esCorrecta(c);
    }

    public void setParent(Control c) {
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

    public boolean esCorrecta(Control c) {
        if(!c.estaDefinida(this.Variable)){
            System.out.println("Variable "+this.Variable+" no ha sido definida");
            E.esCorrecta(c);
            return false;
        }else{
            return E.esCorrecta(c);
        }
        
    }

    public void setParent(Control c) {
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

    public boolean esCorrecta(Control c) {
        return this.Condicion.esCorrecta(c);
    }

    public void setParent(Control c) {
        this.inst.setBloqueExterno(c);
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

    public void setParent(Control c) {
        super.setParent(c);
        this.instElse.setBloqueExterno(c);
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

    public boolean esCorrecta(Control c) {
        return this.Condicion.esCorrecta(c);
    }

    public void setParent(Control c) {
        this.inst.setBloqueExterno(c);
    }
    

}

class Informacion{
    String nombre;
    String tipo;
    Object valor;
    
    public Informacion(String n, String t, Object v){
        this.nombre = n;
        this.tipo = t;
        this.valor = v;   
    }

    public String getNombre() {
        return nombre;
    }

    public String getTipo() {
        return tipo;
    }

    public Object getValor() {
        return valor;
    }

    public void setValor(Object valor) {
        this.valor = valor;
    }
    
    public String toString(){
        return nombre + " : " + tipo + " : " + valor;
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
    private HashMap<String,Informacion> tabla;
    
    //Bloque externo a este
    private Control bloqueExterno;

    public void setBloqueExterno(Control bloqueExterno) {
        this.bloqueExterno = bloqueExterno;
    }

    public Control getBloqueExterno() {
        return bloqueExterno;
    }
    
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
    public boolean agregar(Control c){
        //this.bloqueExterno = c.bloqueExterno;
        boolean flag = true;
        Iterator<Inst> it = c.inst.iterator();
        while(it.hasNext()){
            Inst instr = it.next();
            flag = instr.esCorrecta(this) && flag;
            instr.setParent(this);
            this.inst.add(instr);
        }
        
        Iterator<String> i = c.tabla.keySet().iterator();
        while(i.hasNext()){
            String n = i.next();
            if(this.tabla.containsKey(n)){
                System.out.println("Variable "+n+" definida mas de una vez");
                flag = false;
            }else{
                this.tabla.put(n,c.tabla.get(n));
            }
        }
        return flag;
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
    public void agregarID(String id, Informacion info){
        this.tabla.put(id,info);
    }
    
    /**
     * Verifica si una variable ya esta definida en la Tabla de Simbolos
     * @param s Nombre de la variable a verificar
     * @return <b>true</b> si esta definida, <b>false</b> sino.
     */
    public boolean estaDefinida(String s){
        
        if ( this.tabla.containsKey(s) ){
            return true;
        }else if ( this.bloqueExterno != null ){
            return this.bloqueExterno.estaDefinida(s);
        }else{
            return false;
        }
        
        
        
    }
    
    public Control getTemplate(){
        Control c = new Control();
        c.bloqueExterno = this;
        return c;
    }
    
    public Informacion getInfo(String s){
        if ( this.tabla.containsKey(s) ){
            return this.tabla.get(s);
        }else if ( this.bloqueExterno != null ){
            return this.bloqueExterno.getInfo(s);
        }else{
            return null;
        }
    }
    
    /**
     * Constructor del envoltorio para la Lista de Instrucciones y para la Tabla
     * de Simbolos
     */
    Control(){
        this.tabla = new HashMap<String,Informacion>();
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
    Control(Inst i, String id, Informacion info){
        this(i);
        this.agregarID(id,info);
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
