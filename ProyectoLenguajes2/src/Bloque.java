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
public class Bloque{
    
    //Lista de Instrucciones
    private List<Inst> inst;        
    //Tabla de Simbolos    
    private TablaSim tabla;    
    //Bloque externo a este
    private Bloque bloqueExterno;
	private int id;
	
	Bloque(Bloque bExt, boolean b, int id ){
        this.bloqueExterno = bExt;
		if (b) {
	        TablaSim tsp = (bExt != null ) ? bExt.tabla : null;
	        this.tabla = new TablaSim(tsp);
		} else {
			this.tabla = new TablaSim(null);
		}
        this.inst = new LinkedList<Inst>();
		this.id = id;
    }	    
    /**
	* Constructor del envoltorio para la Lista de Instrucciones y para la Tabla
	* de Simbolos
	*/
    Bloque(int id){
        this.bloqueExterno = null;
        this.tabla = new TablaSim(null);
        this.inst = new LinkedList<Inst>();
		this.id = id;
    }          
    /**
     * Constructor del envoltorio para la Lista de Instrucciones y para la Tabla
     * de Simbolos. Agrega una instrucciona al nuevo objeto y una declaracion.
     * 
     * @param i Inst a agregar
     * @param id Nombre de la variable
     * @param tipo Tipo de la variable
     */
    Bloque(Inst i, String id, Informacion info){
        this(0);
        this.agregarInst(i);
        this.agregarID(id,info);
    }       

	public int getId() {
		return this.id;
	}
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
        System.out.println(this.imprimir(i)+this.tabla);
        System.out.println(this.toCode(Misc.newLabel()));
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
    /**
     * Agrega una instruccion a la lista de instrucciones
     * 
     * @param i una Inst a agregar
     */
    public void agregarInst(Inst i){
        if (i != null)
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
    
    public String toString() {
        Inst i;
        
        String acum = "";
		for(int j=0; j<this.inst.size(); j++){
            i = this.inst.get(j);
			if (i!=null)
				acum+= i.imprimir(0)+"\n";            
        }
        acum += this.toCode(Misc.newLabel());
        return acum;
    }
    
    public String toCode(String next){
        String codigo = "";
        if(this.getParent()==null){
            codigo +=  ".data\n";
            codigo +=".space "+this.getTS().getSize();
            codigo +="\n.code";
            codigo += "r2 := &globl\n";
        }
        codigo += "#Comienzo codigo bloque "+next+"\n";
        Iterator<Inst> it = this.inst.iterator();
        String aux1 = Misc.newLabel();
        String aux0;
        while(it.hasNext()){
            Inst i;
            i = it.next();    //una instruccion
            aux0 = aux1;
            aux1 = Misc.newLabel();
            if(it.hasNext()){
                codigo += i.toCode(aux0, aux1);
            }else{
                codigo += i.toCode(aux0, next);
            }
            /*
            if(it.hasNext()){
                i.setNext(Misc.newLabel());
            }else{
                i.setNext(this.getNext());
            }
             */
            //codigo += i.toCode() + "\n"+i.getNext()+": ";
            
            
        }
        codigo += "#@Fin Codigo Bloque "+next+"\n";
        if(this.getParent()==null){
            codigo += next+": halt\n";
        }
        
        return codigo;
    }
}
class TablaSim{
    private HashMap<String,Informacion> tabla;
    private TablaSim parent;
    private int size;
    
    public TablaSim(){
		this.parent = null;
		this.tabla = new HashMap<String,Informacion>();
                this.size = 0;
	}
    
	public TablaSim(TablaSim p){
        this.parent = p;
        this.tabla = new HashMap<String,Informacion>();
        this.size = 0;
    }    
    public void add(String id, Informacion i){
        this.tabla.put(id,i);
        this.size += i.getTipo().getSize();
    }    
    public void addAll(TablaSim ts){
        this.tabla.putAll(ts.tabla);
        this.size += ts.size;
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
	public Informacion getLocally(String id){
        if (this.tabla.containsKey(id)){
            return this.tabla.get(id);        
        } else {
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
    public int getSize(){
        return this.size;
    }
	public HashMap<String,Informacion> getTabla() {
		return tabla;
	}
}

class Informacion {
	//Nombre del identificador
    String nombre;
	//Tipo del identificador
    Tipo tipo; //TipoF
	//Valor
    Object valor;
	//Estatus de declaracion: 0 normal, 1 Doble Declaracion, 2 No Declarada
    int status;
    
    public Informacion(String n, Tipo t, Object v, int i){
        this.nombre = n;
        this.tipo = t;
		this.valor = v;   
        this.status = i;   
    }
    
    public Informacion(String n, TipoF t, Object v, int i){
        this.nombre = n;
        this.tipo = new TBasico(t);
		this.valor = v;   
        this.status = i;   
    }
    
    public String getNombre() {
        return nombre;
    }
    public Tipo getTipo() {
        return tipo;
    }
    public Object getValor() {
        return valor;
    }
	public int getStatus() {
        return status;
    }	
	public void setTipo(Tipo tipo) {
		this.tipo = tipo;
	}	
        
    public void setTipo(TipoF tipo) {
        this.tipo = new TBasico(tipo);
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

enum Metodo { E, ES, S }

class InfoSub {
	private String id;
    private Bloque bloque;
	private TablaSim ts;
    private LinkedList<Param> params;	
	private TipoF tipoSP;
	private Tipo tipoFunc;
    
    public InfoSub(String id, Bloque b, TablaSim ts, LinkedList<Param> p, TipoF tipoSP, Tipo t){
        this.id = id;
		this.bloque = b;
		this.ts = ts;
        this.params = p;
        this.tipoSP = tipoSP;
        this.tipoFunc = t;
		if ((p == null) && (ts != null)) {		
			for (Iterator iter = ts.getTabla().entrySet().iterator(); iter.hasNext();)
			{ 
			    Map.Entry entry = (Map.Entry)iter.next();
			    String idP = (String)entry.getKey();
			    Informacion info = (Informacion)entry.getValue();			
				this.params.add((Param)info.getValor());
				System.out.println(idP);
			}			
		}
    }        
	public void setTipoFunc(Tipo t) {
		this.tipoFunc = t;
	}
	public void setBloque(Bloque b) {
		this.bloque = b;
	}
	public Bloque getBloque() {
		return this.bloque;
	}
	public TablaSim getTabla() {
		return this.ts;
	}
	public List<Param> getParams() {
		return this.params;
	}
	public TipoF getTipo() {
		return this.tipoSP;
	}
	public boolean chequearParams(LinkedList<Expresion> l, Bloque b, int linea) {
		
		if (this.params.size() != l.size() ) {
			System.out.println("ERROR (linea "+linea+
					") El pasaje de parametros al subprograma '"+this.id+
					"' no es correcto. El numero de parametros debe ser igual a "
					+this.params.size()+".");
			return false;
		} else {
			for(int i=0;i<this.params.size();i++) {
				if (!((this.params.get(i).getTipo()).comparar((l.get(i)).getTipo(b)))) {	
					Class c1 = this.params.get(i).getTipo().getClass();
					Class c2 = (l.get(i)).getTipo(b).getClass();					
					System.out.println("ERROR (linea "+linea+
					") El pasaje de parametros al subprograma '"+this.id+
					"' no es correcto para el parametro en la posicion "+(i+1)+".");
					return false;						
				}			
			}
		}
		return true;	
	}
	public String toString() {
		String s = "Parametros: ";
		for(int i=0;i<this.params.size();i++) {
			s += this.params.get(i).getId() + " ";
			
		}
		s += "\nInstrucciones: \n" +this.bloque.imprimir(0)+this.bloque.getTS()+"\n";		
		return s;
		
	}
}

class Param {

	private String id;
	private Tipo tipo;
	private Metodo metodo;
	
	
	public Param (String id, Tipo tipo, Metodo metodo) {
		this.id = id;
		this.tipo = tipo;
		this.metodo = metodo;
	}
	public void setMetodo (Metodo metodo) {
		this.metodo = metodo;
	}
	public String getId() {
		return this.id;
	}
	public Tipo getTipo() {
		return this.tipo;
	}
	public Metodo getMetodo() {
		return this.metodo;
	}
	
}

class InfoDefTipo {
	private String id;
	private Tipo t;
	
	public InfoDefTipo (String id, Tipo t) {
		this.id = id;
		this.t = t;
	}	
	public String toString() {
		return ""+ this.t;
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
    
    public boolean getValue(){
        return this.b;
    }
}