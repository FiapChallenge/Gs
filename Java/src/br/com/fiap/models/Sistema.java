package br.com.fiap.models;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Sistema {
    // private List<Posts> posts = new ArrayList<Posts>();
    private List<Usuario> usuarios = new ArrayList<Usuario>();

    public void saveData() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("data/users.txt"))) {
            for (Usuario usuario : usuarios) {
                String row = usuario.getNome() + ";" + usuario.getEmail() + ";" + usuario.getSenha() + ";"
                        + usuario.getFotopath();
                bw.write(row);
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println("Erro ao salvar arquivo");
        }
    }

    public void loadData() {
        List<List<String>> data = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader("data/users.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(";");
                data.add(Arrays.asList(values));
            }
        } catch (Exception e) {
            System.out.println("Erro ao ler arquivo");
        }
        for (List<String> row : data) {
            Usuario usuario = new Usuario(row.get(0), row.get(1), row.get(2), row.get(3));
            usuarios.add(usuario);
        }
    }

    public void addUsuario(String nome, String email, String senha) {
        Usuario usuario = new Usuario(nome, email, senha);
        usuarios.add(usuario);
    }

    public void addUsuario(Usuario usuario) {
        usuarios.add(usuario);
    }

    public Usuario buscarUsuario(String email) {
        for (Usuario usuario : usuarios) {
            if (usuario.getEmail().equals(email)) {
                return usuario;
            }
        }
        return null;
    }
}
