/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package auxiliares;

import java.util.HashMap;

/**
 *
 * @author abmon
 */
public class Error {
        
     HashMap<Integer, String> tiposDeErrores = new HashMap();

    public Error() {

        tiposDeErrores.put(100, "No se ha incluido el archivo con el código fuente en Python.");
        tiposDeErrores.put(101, "Se ha incluido más de un archivo para analizar.");
        tiposDeErrores.put(102, "El archivo para analizar debe tener la extensión .py ");
        tiposDeErrores.put(103, "El archivo para analizar no contiene información.");
     
    }

    public String obtenerDescripcionDeError(Integer key) {
        return tiposDeErrores.get(key);
    }
}
