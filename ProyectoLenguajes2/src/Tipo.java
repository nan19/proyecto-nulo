
import java.util.HashMap;
import javax.swing.event.ListSelectionEvent;

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
 * Tipo.java: clase Tipo
 * 
 */
 
 /**
 * Enumeracion que contiene los posibles tipos de los Factores
 * @see Factor
 */
enum TipoF {
    INT(4, "entero"),
    INTtoFLOAT(0, "INTtoFloat"),
    FLOAT(8, "real"),
    ID(0, "identificador"),
    BOOL(1, "booleano"),
    PROC(0, "procedimiento"),
    FUNC(0, "funcion"),
    VOID(0, "VOID"),
    LVAL(0, "LVAL"),
    ARRAY(0, "ARRAY"),
    ERROR(0, "ERROR"),
    NODEF(0, "NODEF");
    
    private int size;
    private String name;
    
    public int getSize(){
        return this.size;
    }
    
    private TipoF(int s, String n){
        this.size = s;
        this.name = n;
    }
    
    public String toString(){
        return "{"+this.name+", "+this.size+"}";
    }
}

/**
 *
 * @author jamil
 */
public abstract class Tipo {
    
    public abstract int getSize();
    @Override
    public abstract String toString();
}


class TBasico extends Tipo{
    public TipoF tipo;
    
    public TBasico(TipoF t){
        this.tipo = t;
    }
	public String toString(){
        return "" + this.tipo;
    }
    @Override
    public int getSize() {
        return this.tipo.getSize();
    }
    
}


class TArreglo extends Tipo{
    private Tipo tipo;
    private int size;
    
    public TArreglo(Tipo t, int s){
        this.tipo = t;
        this.size = s;
    }
	public String toString(){
        return "Arreglo ["+this.size+ "]de "+this.tipo;
    }

    @Override
    public int getSize() {
        return this.size * this.tipo.getSize();
    }
        
}

class TRegistro extends Tipo{
    private TablaSim tabla;
    
    public TRegistro(TablaSim ts){
        this.tabla = ts;
    }
	public String toString(){
        return "Registro: "+this.tabla.toString()+" finr\n";
    }

    @Override
    public int getSize() {
        return this.tabla.getSize();
    }
        
}
class TVariante extends Tipo{
    private TablaSim tabla;
    private String disc;
    private HashMap<Object,TablaSim> discr;
    
    public TVariante(TablaSim ts, String d, HashMap<Object,TablaSim> dr){
        this.tabla = ts;
        this.discr = dr;
        this.disc = d;
    }
    public String toString(){
        String aux = "";
        while(discr.values().iterator().hasNext()){
            aux += discr.values().iterator().next().toString()+"/n";
        }
        return "Variante "+this.tabla+" casos:\n"+aux+"finv\n";
    }

    @Override
    public int getSize() {
        int maximo = Misc.max(this.discr.values());
        return maximo + this.tabla.getSize();
    }
        
}
enum TipoB{
    ENTERO,
    REAL,
    BOOLEANO,
    ERROR
}

enum TipoES{
    IN,
    OUT,
    IN_OUT
}

class TParam extends Tipo{
    private Tipo tipo;
    private TipoES es;
    
    public TParam(Tipo t, TipoES e){
        this.tipo = t;
        this.es = e;
    }

    @Override
    public int getSize() {
        return this.tipo.getSize();
    }

    @Override
    public String toString() {
        return ""+ this.es + this.tipo;
    }
    
}

