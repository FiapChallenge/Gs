package br.com.fiap.models;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Sistema {
    private List<Posts> posts = new ArrayList<Posts>();
    private List<Usuario> usuarios = new ArrayList<Usuario>();

    public Sistema() {
        loadData();
    }

    public List<Posts> getPosts() {
        return posts;
    }

    public void addPost(Posts post) {
        posts.add(post);
    }

    public List<Usuario> getUsuarios() {
        return usuarios;
    }

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

        try (BufferedWriter bw = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream("data/posts.txt"), "UTF-8"))) {
            for (Posts post : posts) {
                post.setConteudo(post.getConteudo().replaceAll("\n", "\\n"));
                String tags = String.join(",", post.getTags());
                String row = post.getTitulo() + ";" + post.getConteudo() + ";" + post.getData() + ";"
                        + post.getAutor().getEmail() + ";" + tags;
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
        data = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(new FileInputStream("data/posts.txt"), "UTF-8"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(";");
                data.add(Arrays.asList(values));
            }
        } catch (Exception e) {
            System.out.println("Erro ao ler arquivo");
        }
        for (List<String> row : data) {
            Usuario autor = buscarUsuario(row.get(3));
            List<String> tags = new ArrayList<String>();
            for (String tag : row.get(4).split(",")) {
                tags.add(tag);
            }
            Posts post = new Posts(row.get(0), row.get(1), row.get(2), autor, tags);
            posts.add(post);
        }
    }

    // Sobrecarga de método
    public void addUsuario(String nome, String email, String senha) {
        Usuario usuario = new Usuario(nome, email, senha);
        usuarios.add(usuario);
    }

    // Sobrecarga de método
    public void addUsuario(Usuario usuario) {
        usuarios.add(usuario);
    }

    // Método para buscar um usuário pelo email
    public Usuario buscarUsuario(String email) {
        for (Usuario usuario : usuarios) {
            if (usuario.getEmail().equals(email)) {
                return usuario;
            }
        }
        return null;
    }

}
