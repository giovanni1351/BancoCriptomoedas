/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.util.ArrayList;

/**
 *
 * @author unifgmorassi
 */
public class Carteira {
    private Ethereum eth;
    private Real real;
    private Ripple ripple;
    private Bitcoin bitcoin;
    ArrayList<Moedas> genericas = new ArrayList<Moedas>();

    public ArrayList<Moedas> getGenericas() {
        return genericas;
    }

    public void setGenericas(ArrayList<Moedas> genericas) {
        this.genericas = genericas;
    }
    
    public Moedas getMoeda(int index){
        index+=1;
        switch(index){
            case 1: return bitcoin;
            case 2: return eth;
            case 3: return ripple;
            default: return genericas.get(index-4);
        }
    }

    public Carteira(double bitcoin,double ripple,double ethereum,double real){
        eth = new Ethereum(ethereum);
        this.real = new Real(real);
        this.ripple = new Ripple(ripple);
        this.bitcoin = new Bitcoin(bitcoin);
    }

    public ArrayList<Moedas> getArrayList() {
        return genericas;
    }

    public void setArrayList(ArrayList<Moedas> arrayList) {
        this.genericas = arrayList;
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

    @Override
    public String toString() {
        return "Carteira{" + "eth=" + eth + ", real=" + real + ", ripple=" + ripple + ", bitcoin=" + bitcoin + '}';
    }
    
    

    
}
