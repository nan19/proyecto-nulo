
import java.util.Collection;

/*
 * Misc.java
 *
 * Created on April 5, 2008, 10:50 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

/**
 *
 * @author jamil
 */
public class Misc {
    
    /** Creates a new instance of Misc */
    public static Bloque acumulador;
    java.util.List l;
    
    public static int max(Collection<TablaSim> c){
        int total = 0;
        while(c.iterator().hasNext()){
            total = Math.max(total, c.iterator().next().getSize());
        }
        return total;
    }
    
    private static int etiquetador = -1;
    public static String newLabel(){
        etiquetador++;
        return "_lab"+etiquetador;
    }
    
    public static String getRegister(int i){
        return "r"+i;
    }
    public final static int NReg = 32;
}
