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
 * Bloque.java: Especificacion de las distintas clases necesarias 
 * para generar el Arbol Abstracto y la Tabla de Simbolos
 * asociados al analisis sintactico.
 * 
 */

/**
 * Clase que envuelve a la lista de instrucciones y la tabla de simbolos del
 * programa
 */
class Bloque{
    
    //Lista de Instrucciones
    private List<Inst> inst;
        
    //Tabla de Simbolos
    //private HashMap<String,Informacion> tabla;
    
    private TablaSim tabla;
    
    //Bloque externo a este
    private Bloque bloqueExterno;

    public void setParent(Bloque bloqueExterno) {
        this.bloqueExterno = bloqueExterno;
    }

    public Bloque getParent() {
        return bloqueExterno;
    }
    
    public TablaSim getTS(){
        return this.tabla;
    }
    
    /**
     * Escribe en consola tanto las instrucciones del programa
     * como la tabla de simbolos
     */
    public void imprimirB(int i){
        System.out.println(this.imprimir(i));
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
        this.tabla.add(id,info);
    }
    
    /**
     * Verifica si una variable ya esta definida en la Tabla de Simbolos
     * @param s Nombre de la variable a verificar
     * @return <b>true</b> si esta definida, <b>false</b> sino.
     */
    
    public boolean estaDefinida(String s){
        return this.tabla.isDefined(s); 
    }
    
    public Informacion getInfo(String s){
        return this.tabla.get(s);
    }
    
    Bloque(Bloque bExt){
        this.bloqueExterno = bExt;
        TablaSim tsp = (bExt != null ) ? bExt.tabla : null;
        this.tabla = new TablaSim(tsp);
        this.inst = new LinkedList<Inst>();
    }
    
    
    /**
     * Constructor del envoltorio para la Lista de Instrucciones y para la Tabla
     * de Simbolos
     */
    Bloque(){
        this.bloqueExterno = null;
        this.tabla = new TablaSim(null);
        this.inst = new LinkedList<Inst>();
    }   
       
    /**
     * Constructor del envoltorio para la Lista de Instrucciones y para la Tabla
     * de Simbolos. Agrega una instrucciona al nuevo objeto y una declaracion.
     * @param i Instruccion a agregar
     * @param id Nombre de la variable
     * @param tipo Tipo de la variable
     */
    Bloque(Inst i, String id, Informacion info){
        this();
        this.agregarInst(i);
        this.agregarID(id,info);
    }   
    
    public String toString() {
        Inst i;
        String acum = "";
		for(int j=0; j<this.inst.size(); j++){
            i = this.inst.get(j);
			if (i!=null)
				acum+= i.imprimir(0)+"\n";            
        }      
        return acum;
    }
	
	public String imprimir(int i) {
        Inst in;
        String acum = "";
		for(int j=0; j<this.inst.size(); j++){
            in = this.inst.get(j);
			if (in!=null) {				
				acum+= in.imprimir(i+1);            
			}
        }      
        return acum;
    }

}

class TablaSim{
    private HashMap<String,Informacion> tabla;
    private TablaSim parent;
    
    public TablaSim(TablaSim p){
        this.parent = p;
        this.tabla = new HashMap<String,Informacion>();
    }
    
    public void add(String id, Informacion i){
        this.tabla.put(id,i);
    }
    
    public void addAll(TablaSim ts){
        this.tabla.putAll(ts.tabla);
    }
	
	public void addList(Vector v){
        Iterator i = v.iterator();
		Informacion info;
		while (i.hasNext()) {
			info = (Informacion)i.next();
			this.add(info.getNombre(), info);
		}
			
    }
    
    public Informacion get(String id){
        if (this.tabla.containsKey(id)){
            return this.tabla.get(id);
        }else if (this.parent != null) {
            return this.parent.get(id);
        }else{
            return null;
        }
        
    }
    
    public boolean isDefinedLocally(String id){
        return this.tabla.containsKey(id);
    }
    
    public boolean isDefined(String id){
        if(this.isDefinedLocally(id)){
            return true;
        }else if(this.parent != null){
            return this.parent.isDefined(id);
        }else{
            return false;
        }
    }
    
    public TablaSim getParent(){
        return this.parent;
    }

    public String toString() {
        String acum = "Tabla:\n";
		Iterator k = this.tabla.keySet().iterator();
        Iterator v = this.tabla.values().iterator();
        int j = 0;
        while(k.hasNext()){
            acum += ""+(j+1)+" "+k.next()+ " : "+v.next()+"\n";
            j++;
        }
        return acum;
    }
    
    public void set(String id,Informacion info){
        if(this.isDefinedLocally(id)){
            this.tabla.put(id,info);
        }else if(this.parent != null){
            this.parent.set(id,info);
        }else{
            //error
        }
    }
}


class Informacion {
    String nombre;
    TipoF tipo;
    Object valor;
	int status;	//0: normal, 1: DD, 2: ND
    
    public Informacion(String n, TipoF t, Object v, int i){
        this.nombre = n;
        this.tipo = t;
        this.valor = v;   
        this.status = i;   
    }

    public String getNombre() {
        return nombre;
    }

    public String getTipo() {
        return "";
    }

    public Object getValor() {
        return valor;
    }

	public int getStatus() {
        return status;
    }
	
    public void setValor(Object valor) {
        this.valor = valor;
    }
	
	public void setStatus(int status) {
        this.status = status;
    }
    
    public String toString(){
        return nombre + " : " + tipo + " : " + valor;
    }
}

class valorArreglo {
	int longitud;
	Vector arreglo;
	
	public valorArreglo(int longitud, Vector arreglo) {
		this.longitud = longitud;
		this.arreglo = arreglo;
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
