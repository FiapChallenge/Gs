package br.com.fiap.models;

import java.awt.Image;

import javax.swing.ImageIcon;

public class Usuario {
    private String nome;
    private String email;
    private String senha;
    private String fotopath = "";

    public String getFotopath() {
        return fotopath;
    }

    ImageIcon foto;

    public Usuario(String nome, String email, String senha) {
        this.nome = nome;
        this.email = email;
        this.senha = senha;
    }

    public Usuario(String nome, String email, String senha, String fotopath) {
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.fotopath = fotopath;
        setFoto(fotopath);
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public void setFoto(String fotopath) {
        this.fotopath = fotopath;
        ImageIcon profile = new ImageIcon(fotopath);
        Image image = profile.getImage();
        Image newimg = image.getScaledInstance(80, 80, java.awt.Image.SCALE_SMOOTH);
        this.foto = new ImageIcon(newimg);
    }

    public ImageIcon getFoto() {
        return foto;
    }
}
