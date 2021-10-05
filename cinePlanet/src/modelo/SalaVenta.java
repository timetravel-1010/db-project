/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

/**
 *
 * @author DANILO
 */
public class SalaVenta {
    
    private String idSala;
    private int boletas;

    public SalaVenta(String idSala, int boletas) {
        this.idSala = idSala;
        this.boletas = boletas;
    }

    public String getIdSala() {
        return idSala;
    }

    public void setIdSala(String idSala) {
        this.idSala = idSala;
    }

    public int getBoletas() {
        return boletas;
    }

    public void setBoletas(int boletas) {
        this.boletas = boletas;
    }
    
    
    
}
