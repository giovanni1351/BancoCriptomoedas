/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author unifgmorassi
 */
public class Carteira {
    private Ethereum eth;
    private Real real;
    private Ripple ripple;
    private Bitcoin bitcoin;

    public Carteira(double bitcoin,double ripple,double ethereum,double real){
        eth = new Ethereum(ethereum);
        this.real = new Real(real);
        this.ripple = new Ripple(ripple);
        this.bitcoin = new Bitcoin(bitcoin);
    }
    
    public Ethereum getEth() {
        return eth;
    }

    public void setEth(Ethereum eth) {
        this.eth = eth;
    }

    public Real getReal() {
        return real;
    }

    public void setReal(Real real) {
        this.real = real;
    }

    public Ripple getRipple() {
        return ripple;
    }

    public void setRipple(Ripple ripple) {
        this.ripple = ripple;
    }

    public Bitcoin getBitcoin() {
        return bitcoin;
    }

    public void setBitcoin(Bitcoin bitcoin) {
        this.bitcoin = bitcoin;
    }
    
    

    
}
