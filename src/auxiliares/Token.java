/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package auxiliares;

/**
 *
 * @author abmon
 */
public class Token {

   private String lexema; //private TokenType type;
    private String tipoDeToken;
    private String literal; //private Object literal;
    private int numeroLinea;

    public Token() {
    }

    public Token(String lexema, String tipo, String literal, int linea) {
        this.lexema = lexema;
        this.tipoDeToken = tipo;
        this.literal = literal;
        this.numeroLinea = linea;
    }

    public String getLexema() {
        return lexema;
    }

    public String getTipoDeToken() {
        return tipoDeToken;
    }

    public String getValor() {
        return literal;
    }

    public int getLinea() {
        return numeroLinea;
    }

    @Override
    public String toString() {
        return getTipoDeToken() + " " + getLexema() + " " + getValor() + " " + getLinea();
    }
}
