/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package anita.so2.etapa2;

/**
 *
 * @author anita
 */
import java.util.Date;

import anita.so2.etapa2.Utils;

public class Pessoa {

    private static int counter = 1;

    private int numeroPessoa ;
    private String name;
    private String cpf;
    private String agencia;
    private String email;
    private Date accountCreationDate;

    public Pessoa() { }

    public Pessoa(String name, String cpf,String agencia, String email) {
        this.numeroPessoa = Pessoa.counter;
        this.name = name;
        this.cpf = cpf;
        this.agencia = agencia;
        this.email = email;
        this.accountCreationDate = new Date();
        Pessoa.counter += 1;
    }

    public int getNumeroPessoa() {
        return this.numeroPessoa;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
     public String getAgencia() {
        return agencia;
    }
    public void setAgencia(String agencia) {
        this.agencia = agencia;
    }
    public String getCpf() {
        return cpf;
    }
    public void setCpf(String cpf) {
        this.cpf = cpf;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public Date getAccountCreationDate() {
        return this.accountCreationDate;
    }

    public String toString() {
        return  "\nName: " + this.getName() +
                "\nCPF: " + this.getCpf() +
                "\nEmail: " + this.getEmail() +
                "\nAgencia: " + this.getAgencia() +
                "\nAccount Creation Date: " + Utils.dateToString(this.getAccountCreationDate());
    }


}