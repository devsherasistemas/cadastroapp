/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package br.com.cadastroapp.model;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;

/**
 * 
 * @author Everton Molina <everton.molina@herasistemas.com.br>
 */
@Entity(name = "validadorconnection")
public class ValidadorConnection implements Serializable {
    @Id
    private int id;
    @Column(name = "razao_social")
    private String razaoSocial;
    private String username;
    private String datawarehouse;
    private Boolean cancelado;
    private Boolean admin;
    private String cnpj;
    private Boolean bloqueado;
    private String password;
    @Transient
    private String senhaDescriptografada;
    

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDatawarehouse() {
        return datawarehouse;
    }

    public void setDatawarehouse(String datawarehouse) {
        this.datawarehouse = datawarehouse;
    }

    public Boolean getCancelado() {
        return cancelado;
    }

    public void setCancelado(Boolean cancelado) {
        this.cancelado = cancelado;
    }

    public Boolean getAdmin() {
        return admin;
    }

    public void setAdmin(Boolean admin) {
        this.admin = admin;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public Boolean getBloqueado() {
        return bloqueado;
    }

    public void setBloqueado(Boolean bloqueado) {
        this.bloqueado = bloqueado;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRazaoSocial() {
        return razaoSocial;
    }

    public void setRazaoSocial(String razaoSocial) {
        this.razaoSocial = razaoSocial;
    }

    public String getSenhaDescriptografada() {
        return senhaDescriptografada;
    }

    public void setSenhaDescriptografada(String senhaDescriptografada) {
        this.senhaDescriptografada = senhaDescriptografada;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 71 * hash + this.id;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ValidadorConnection other = (ValidadorConnection) obj;
        return this.id == other.id;
    }
}
