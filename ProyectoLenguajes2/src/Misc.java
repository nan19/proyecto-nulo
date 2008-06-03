
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
    public static String codigoExpBin(ExprBin e, int registro, String op){
        String salv="";
        String rest= "";
        String result="";
        String aux = Misc.newLabel();
        int registro2 = registro+1;
        if(!(registro2<Misc.NReg)){
            registro2 = registro2 % Misc.NReg;
            salv+= "mv sp "+Misc.getRegister(registro2)+"\n"+"add sp sp -4\n";
            rest+= "add sp sp 4\n"+"mv "+Misc.getRegister(registro2)+" sp";
        }
        result += e.getExprIzq().toCode(aux, aux,registro)+salv;
        result += e.getExprDer().toCode(aux, aux,registro2);
        result += op +" " + Misc.getRegister(registro)+" "+Misc.getRegister(registro);
        result += " "+Misc.getRegister(registro2)+"\n"+rest;
        return result;
    }
}
