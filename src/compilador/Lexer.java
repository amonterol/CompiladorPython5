/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package compilador;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import auxiliares.Token;
import auxiliares.Error;
import auxiliares.PalabraReservada;
import auxiliares.TipoDeToken;

/**
 *
 * @author abmon
 */
public class Lexer {

    private static List<String> contenidoDelArchivo; //Código que se analiza en forma de lista
    private static List<Token> listaDeTokens; //Almacena los tokens que se identifican
    private int cantidadComentarios; // almacena la cantidad de comentarios en el código
    private int numeroLineaActual;

    public Lexer(List<String> content) {
        this.contenidoDelArchivo = content;
        this.listaDeTokens = new ArrayList<>();
        this.cantidadComentarios = 0;
        this.numeroLineaActual = 1;
    }

    public void analizadorLexico(List<String> contenido) throws IOException {

        // System.out.println("\n3 LEXER > INICIO LINEA DE CODIGO CONVERTIDA A CARACTERES  " + lineaDeCodigo);
        char caracterActual = ' '; //Almacena el caracter que actualmente se lee en la línea de código
        char caracterSiguiente = ' '; //Almacena el caracter siguiente al que actualmente se lee en la línea de código
        String identificador = "";
        String string = "";
        String comentario = "";
        String numero = ""; //Almacena transitoriamente un cadena constituido por digitos

        listaDeTokens = new ArrayList<>();
        for (String lineaDeCodigo : contenidoDelArchivo) {

            //Verifica si la linea esta en blanco
            if (lineaDeCodigo.isBlank() || lineaDeCodigo.isEmpty()) {
                System.out.println("\n 1 LEXER BORRAR ES UN NUEVA LINEA DE CODIGO ESTA EN BLANCO " + lineaDeCodigo);
                System.out.println("\n 2 LEXER BORRAR ES UN NUEVA LINEA DE CODIGO ESTA EN BLANCO " + numeroLineaActual);
                continue;

            }

            //Verifica si la linea es un comentario o si hay comentarios al final de una linea de código
            if (existeComentario(lineaDeCodigo)) {
                cantidadComentarios++;
                lineaDeCodigo = lineaDeCodigo.split("#")[0].trim(); // Elimina la parte del comentario en la línea
            }

            //Convierte cada línea de código en un arreglo de caracteres para recorrer caracter por caracter e identificar su tipo
            char[] arregloCaracteres = lineaDeCodigo.toCharArray();

            for (int i = 0; i < arregloCaracteres.length; ++i) {

                caracterActual = arregloCaracteres[i];
                System.out.println("\n" + (i + 1) + "-> 3 LEEMOS EL CARACTER ACTUAL " + caracterActual + " posicion " + i + " size " + arregloCaracteres.length);
                switch (caracterActual) {

                    // Se ignoran los caracteres en blanco
                    case ' ':
                    case '\r':
                    case '\t':
                        System.out.println("5 caracter en blanco " + string.trim() + "  "
                                + caracterActual + " posicion " + i + " size " + arregloCaracteres.length);
                        break;

                    // Identifica el operador de asignación
                    case '=':
                        agregarNuevoToken(null, TipoDeToken.ASIGNACION, String.valueOf(caracterActual), this.numeroLineaActual);
                        break;

                    // Identifica números enteros, decimales, identificadores y palabras reservadas    
                    default: {
                        PalabraReservada palabraReservada = new PalabraReservada();
                        if (esCaracterAlfaNumerico(caracterActual) || caracterActual == '_') {
                            string = string + caracterActual;
                        }
                        if (i < arregloCaracteres.length - 1) {
                            caracterSiguiente = arregloCaracteres[i + 1];

                            while (esCaracterAlfaNumerico(caracterSiguiente) && i < (arregloCaracteres.length - 1) && caracterSiguiente != ' ') {
                                ++i;
                                caracterActual = arregloCaracteres[i];
                                string = string + caracterActual;
                                if (i < (arregloCaracteres.length - 1)) {
                                    caracterSiguiente = arregloCaracteres[i + 1];
                                }
                            }
                        }

                       
                        if (esNumeroEntero(string.trim())) {
                            agregarNuevoToken(string, TipoDeToken.NUMERO_ENTERO, string.trim(), this.numeroLineaActual);
                        } else if (esNumeroDecimal(string.trim())) {
                            agregarNuevoToken(string, TipoDeToken.NUMERO_DECIMAL, string.trim(), this.numeroLineaActual);
                        } else if (palabraReservada.esPalabraReservada(string.trim())) {
                            agregarNuevoToken(string, TipoDeToken.PALABRA_RESERVADA, string.trim(), this.numeroLineaActual);
                        } else if (verificarPrimerCaracterDeUnIdentificador(string.trim()) && verificarSecuenciaDeCaracteresDeUnIdentificador(string.trim())) {
                            agregarNuevoToken(string, TipoDeToken.IDENTIFICADOR, string.trim(), this.numeroLineaActual);
                        } else {
                            agregarNuevoToken(string, TipoDeToken.DESCONOCIDO, string.trim(), this.numeroLineaActual);
                        }

                        string = " ";

                        break;

                    }//fin default
                } //Fin switch
                numeroLineaActual++; //Aumenta con cada linea que es analizada
            } //Fin del for

            

        }

        System.out.println("\n\n<<< 124 Lexico> CANTIDAD DE TOKENS EN EL LEXICO>>> " + listaDeTokens.size());
            System.out.println("\n\n<<<CANTIDAD DE TOKENS>>> " + listaDeTokens.size());
            listaDeTokens.forEach((item) -> {
                System.out.println(item.getLexema() + " " + item.getTipoDeToken() + " " + item.getValor() + " " + item.getLinea());
            });

            System.out.println("\n\n<<<NUMERO DE COMENTARIOS>>> " + cantidadComentarios);
    }//Fin metodo analizadorLexico

    public List<Token> getListaDeTokens() {
        return listaDeTokens;
    }

    public int getCantidadDeComentarios() {
        return cantidadComentarios;
    }

    //FUNCIONES AUXILIARES
    //Verifica si la linea de codigo que se esta leyendo contiene un comentario
    public boolean existeComentario(String lineaActual) {
        Character caracterDeComentario = '#';

        System.out.println(" Existe el caracter de comentarios " + lineaActual.contains(String.valueOf(caracterDeComentario)));
        return lineaActual.contains(String.valueOf(caracterDeComentario));

    }

    public static boolean esFinalLinea(char[] arregloCaracteres, int contador) {
        return contador >= arregloCaracteres.length;
    }

    public static void agregarNuevoToken(String nombreToken, TipoDeToken tipoDeToken, String valor, int numeroLinea) {
        Token nuevoToken = new Token(nombreToken, tipoDeToken, valor, numeroLinea);

        listaDeTokens.add(nuevoToken);
    }

    private static boolean esCaracterAlfabetico(char c) {
        return (c >= 'a' && c <= 'z')
                || (c >= 'A' && c <= 'Z')
                || c == '_';
    }

    private static boolean esCaracterNumerico(char c) {
        return c >= '0' && c <= '9';
    }

    private static boolean esCaracterAlfaNumerico(char c) {
        return esCaracterAlfabetico(c) || esCaracterNumerico(c);
    }

    //Valida si el token corresponde a un identificador valido
    public static boolean verificarPrimerCaracterDeUnIdentificador(String string) {

        
        if (string == null || string.isEmpty()) {
            return false;
        }
               
        if(string.matches("^[a-zA-Z].*")){
            return true;
        } else {
              System.out.println("191 Borrar " + Error.obtenerDescripcionDeError(201));
              return false;
        }       
        
        
    }

    //Verifica que el identificador sea una secuencia de letras y
    //numeros sin caracteres especiales a partir del segundo caracter
    public boolean verificarSecuenciaDeCaracteresDeUnIdentificador(String string) {
        // Verificar los caracteres restantes
        if (string == null || string.isEmpty()) {
            return false;
        }
               
        if(string.matches("[a-zA-Z0-9_]*$")){
            return true;
        } else {
              System.out.println("209 Borrar " + Error.obtenerDescripcionDeError(201));
              return false;
        }   
       
    }

    //Valida si el token corresponde a una palabra reservada
    public static boolean esNumeroEntero(String str) {
        if (str == null || str.isEmpty()) {
            return false;
        }

        try {
            Integer.valueOf(str);
            return true; // El token es un número entero
        } catch (NumberFormatException e) {
            return false; // El token no es entero 
        }
    }

    //Valida si el token corresponde a un numero decimal o entero
    public static boolean esNumeroDecimal(String str) {
        if (str == null || str.isEmpty()) {
            return false;
        }

        try {
            Double.valueOf(str);
            return true; // El token es un número decimal
        } catch (NumberFormatException e) {
            return false; // El token no es un número decimal
        }
    }

    public void imprimirListas(List<String> contenidoArchivo) {
        /*
        Iterator iter = lista.iterator();

        while (iter.hasNext()) {
            System.out.print(iter.next());
        }
         */
        for (String linea : contenidoArchivo) {
            System.out.println(linea);
        }
    }

}
