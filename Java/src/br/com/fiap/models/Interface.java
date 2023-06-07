package br.com.fiap.models;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.Dimension;
import java.awt.Insets;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import br.com.fiap.utilities.OpenWeather;

public class Interface {
    public static Usuario login(Sistema sb) {
        ImageIcon icon = new ImageIcon("GFX/logo/logo.png");
        while (true) {
            Image image = icon.getImage();
            Image newimg = image.getScaledInstance(80, 80, java.awt.Image.SCALE_SMOOTH);
            icon = new ImageIcon(newimg);

            JPanel panel = new JPanel(new GridBagLayout());
            panel.setPreferredSize(new java.awt.Dimension(200, 100));
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.anchor = GridBagConstraints.LINE_START;
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.weightx = 1.0;
            panel.add(new JLabel("Digite seu email:"), gbc);
            gbc.gridy++;
            JTextField textField = new JTextField(10);
            textField.setPreferredSize(new java.awt.Dimension(200, 24));
            panel.add(textField, gbc);
            int result = JOptionPane.showOptionDialog(null, panel, "AgroSolution",
                    JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE,
                    icon, new String[] { "OK", "Cadastrar", "Cancelar" }, "OK");
            if (result == -1) {
                System.exit(0);
            }
            if (result == 2) {
                System.exit(0);
            }

            if (result == 1) {
                cadastrar(sb);
            }
            if (result == 0) {

                String email = textField.getText();
                if (email == null) {
                    System.exit(0);
                }
                String senha = (String) JOptionPane.showInputDialog(null, "Digite sua senha:", "AgroSolution",
                        JOptionPane.QUESTION_MESSAGE, icon, null, null);
                if (senha == null) {
                    System.exit(0);
                }
                Usuario usuario = sb.buscarUsuario(email);
                if (usuario == null || !usuario.getSenha().equals(senha)) {
                    JOptionPane.showMessageDialog(null, "Usuário ou senha inválidos");
                    continue;
                }
                return usuario;
            }
        }
    }

    public static void cadastrar(Sistema sb) {
        while (true) {

            ImageIcon icon = new ImageIcon("GFX/logo/logo.png");
            Image image = icon.getImage();
            Image newimg = image.getScaledInstance(80, 80, java.awt.Image.SCALE_SMOOTH);
            icon = new ImageIcon(newimg);

            JPanel panel = new JPanel(new GridBagLayout());
            panel.setPreferredSize(new java.awt.Dimension(200, 150));
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.anchor = GridBagConstraints.LINE_START;
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.weightx = 1.0;
            panel.add(new JLabel("Digite seu nome:"), gbc);
            gbc.gridy++;
            JTextField textField = new JTextField(10);
            textField.setPreferredSize(new java.awt.Dimension(200, 24));
            panel.add(textField, gbc);
            gbc.gridy++;
            panel.add(new JLabel("Digite seu email:"), gbc);
            gbc.gridy++;
            JTextField textField2 = new JTextField(10);
            textField2.setPreferredSize(new java.awt.Dimension(200, 24));
            panel.add(textField2, gbc);
            gbc.gridy++;
            panel.add(new JLabel("Digite sua senha:"), gbc);
            gbc.gridy++;
            JTextField textField3 = new JTextField(10);
            textField3.setPreferredSize(new java.awt.Dimension(200, 24));
            panel.add(textField3, gbc);
            gbc.gridy++;
            panel.add(new JLabel("(Opcional) Adicione uma foto:"), gbc);
            gbc.gridy++;
            JButton button = new JButton("Selecionar");
            button.setPreferredSize(new java.awt.Dimension(200, 24));
            panel.add(button, gbc);
            JFileChooser file = new JFileChooser();
            file.setFileSelectionMode(JFileChooser.FILES_ONLY);
            button.addActionListener(e -> {
                file.showOpenDialog(null);
                if (file.getSelectedFile() != null) {
                    button.setText(file.getSelectedFile().getName());
                }
            });
            gbc.gridy++;
            int result = JOptionPane.showOptionDialog(null, panel, "AgroSolution",
                    JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE,
                    icon, new String[] { "OK", "Cancelar" }, "OK");

            if (result == 1) {
                System.exit(0);
            }

            String nome = textField.getText();
            String email = textField2.getText();
            String senha = textField3.getText();
            if (nome == null || email == null || senha == null) {
                System.exit(0);
            }
            if (sb.buscarUsuario(email) != null) {
                JOptionPane.showMessageDialog(null, "Email já cadastrado");
                continue;
            }

            if (file.getSelectedFile() != null) {
                sb.addUsuario(
                        new Usuario(nome, email, senha,
                                file.getSelectedFile().getAbsolutePath()));
                JOptionPane.showMessageDialog(null, "Usuário cadastrado com sucesso");
                sb.saveData();
                return;
            }
            sb.addUsuario(
                    new Usuario(nome, email, senha,
                            "GFX/profiles/defaultAvatar.png"));
            JOptionPane.showMessageDialog(null, "Usuário cadastrado com sucesso");
            sb.saveData();
            return;
        }
    }

    public static int menu(Usuario usuario, String menu) {
        String info = "";
        int opcao = JOptionPane.showOptionDialog(null, (info + menu), "AgroSolution - " + usuario.getNome(), 0,
                JOptionPane.QUESTION_MESSAGE, usuario.getFoto(),
                new String[] { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9" }, "1");
        return opcao;
    }

    public static void weather() {
        String city = JOptionPane.showInputDialog(null, "Digite o nome da cidade:", "AgroSolution",
                JOptionPane.QUESTION_MESSAGE);

        if (city == null) {
            return;
        }

        List<String> info = OpenWeather.getInfo(city);
        String infoString = "Temperatura: " + info.get(0) + "°C\nTemperatura Máxima: " + info.get(1)
                + "°C\nTemperatura Mínima: " + info.get(2) + "°C\nUmidade: " + info.get(3) + "%\nClima: " + info.get(4)
                + "\nVelocidade do Vento: " + info.get(5) + "m/s\nNascer do Sol: " + info.get(6) + "\nPor do Sol: "
                + info.get(7);
        JOptionPane.showMessageDialog(null, infoString);
    }

    public static void show_posts(Sistema sb) {
        String[] columns = { "Título", "Conteúdo", "Data", "Autor", "Tags" };
        String[][] data = new String[sb.getPosts().size()][5];
        int i = 0;
        for (Posts post : sb.getPosts()) {
            data[i][0] = post.getTitulo();
            data[i][1] = post.getConteudo();
            data[i][2] = post.getData();
            data[i][3] = post.getAutor().getNome();
            data[i][4] = post.getTags().toString().replace("[", "").replace("]", "");
            i++;
        }

        JTable table = new JTable(data, columns);
        table.setPreferredScrollableViewportSize(new Dimension(600, 70));
        table.setFillsViewportHeight(true);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        table.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        table.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);
        table.getColumnModel().getColumn(2).setCellRenderer(centerRenderer);
        table.getColumnModel().getColumn(3).setCellRenderer(centerRenderer);
        table.getColumnModel().getColumn(4).setCellRenderer(centerRenderer);

        table.getColumnModel().getColumn(0).setPreferredWidth(120);

        JScrollPane scrollPane = new JScrollPane(table);

        JOptionPane.showOptionDialog(table, scrollPane, "AgroSolution", 0, JOptionPane.QUESTION_MESSAGE, null,
                new String[] { "Ver Post Selecionado", "Retornar" }, "Ver Post Selecionado");

        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            return;
        }
        Posts post = sb.getPosts().get(selectedRow);
        post.setConteudo(post.getConteudo().replace("\\n", "\n"));

        String tags = "";
        for (String tag : post.getTags()) {
            tags += tag + ", ";
        }

        tags = tags.substring(0, tags.length() - 2);

        String info = "Título: " + post.getTitulo() + "\n\nConteúdo: \n" + post.getConteudo() + "\n\nData: "
                + post.getData()
                + "\n\nAutor: " + post.getAutor().getNome() + "\n\nTags: " + tags;
        JTextArea textArea = new JTextArea(info);
        textArea.setPreferredSize(new Dimension(500, 500));
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setEditable(false);
        JScrollPane scrollPane2 = new JScrollPane(textArea);
        JOptionPane.showMessageDialog(null, scrollPane2);

    }

    public static void create_post(Sistema sb, Usuario usuarioLogado) {
        JLabel labelTitle = new JLabel("Título do Post:");
        JTextField title = new JTextField();
        title.setPreferredSize(new Dimension(200, 24));

        JLabel labelContent = new JLabel("Conteúdo do Post:");
        JTextArea content = new JTextArea();
        content.setLineWrap(true);
        content.setWrapStyleWord(true);

        JScrollPane scrollPane = new JScrollPane(content);
        scrollPane.setPreferredSize(new Dimension(200, 100)); // Set preferred size for the scroll pane

        JLabel labelTags = new JLabel("Tags do Post (Separe com virgula):");
        JTextField tags = new JTextField();
        tags.setPreferredSize(new Dimension(200, 24));

        JPanel panel = new JPanel(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(2, 2, 2, 2);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.LINE_START;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        panel.add(labelTitle, gbc);

        gbc.gridy++;
        panel.add(title, gbc);

        gbc.gridy++;
        panel.add(labelContent, gbc);

        gbc.gridy++;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        panel.add(scrollPane, gbc);

        gbc.gridy++;
        panel.add(labelTags, gbc);

        gbc.gridy++;
        panel.add(tags, gbc);

        int result = JOptionPane.showOptionDialog(
                null,
                panel,
                "AgroSolution",
                JOptionPane.YES_NO_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE,
                null,
                new String[] { "OK", "Cancelar" },
                "OK");

        if (result == 1) {
            return;
        }

        if (title.getText().equals("") || content.getText().equals("") || tags.getText().equals("")) {
            JOptionPane.showMessageDialog(null, "Preencha todos os campos");
            return;
        }

        String titulo = title.getText();
        titulo = titulo.replace(";", ",");
        titulo = titulo.replace("\n", "\\n");
        String conteudo = content.getText();
        conteudo = conteudo.replace(";", ",");
        conteudo = conteudo.replace("\n", "\\n");
        String data = new SimpleDateFormat("dd/MM/yyyy").format(new Date());
        List<String> tagsList = new ArrayList<String>();
        for (String tag : tags.getText().split(",")) {
            tag = tag.trim();
            tagsList.add(tag);
        }

        Posts post = new Posts(titulo, conteudo, data, usuarioLogado, tagsList);

        sb.addPost(post);

        JOptionPane.showMessageDialog(null, "Post criado com sucesso");
        sb.saveData();
    }

    public static void delete_post(Sistema sb, Usuario usuarioLogado) {
        // only show posts from the logged user
        List<Posts> posts = new ArrayList<Posts>();
        for (Posts post : sb.getPosts()) {
            if (post.getAutor().getEmail().equals(usuarioLogado.getEmail())) {
                posts.add(post);
            }
        }
        if (posts.size() == 0) {
            JOptionPane.showMessageDialog(null, "Você não possui posts");
            return;
        }
        

        String[] columns = { "Título", "Conteúdo", "Data", "Autor", "Tags" };
        String[][] data = new String[posts.size()][5];
        int i = 0;
        for (Posts post : posts) {
            data[i][0] = post.getTitulo();
            data[i][1] = post.getConteudo();
            data[i][2] = post.getData();
            data[i][3] = post.getAutor().getNome();
            data[i][4] = post.getTags().toString().replace("[", "").replace("]", "");
            i++;
        }

        JTable table = new JTable(data, columns);
        table.setPreferredScrollableViewportSize(new Dimension(500, 70));
        table.setFillsViewportHeight(true);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        table.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        table.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);
        table.getColumnModel().getColumn(2).setCellRenderer(centerRenderer);

        JScrollPane scrollPane = new JScrollPane(table);
        int opcao = JOptionPane.showOptionDialog(table, scrollPane, "AgroSolution", 0, JOptionPane.QUESTION_MESSAGE,
                null, new String[] { "Remover Post Selecionado", "Retornar" }, "Remover Post Selecionado");

        if (opcao == 1) {
            return;
        }

        if (opcao == -1) {
            return;
        }

        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            return;
        }

        Posts post = posts.get(selectedRow);
        sb.getPosts().remove(post);
        JOptionPane.showMessageDialog(null, "Post removido com sucesso");
        sb.saveData();
    }

    public static void faq() {
        // questions = {
        // 1: {
        // "q": "Por que o clima não aparece?",
        // "a": "Siga esses passos:\n1 - Verifique se o nome da cidade está escrito
        // corretamente\n2 - Verifique se está conectado a internet\n3 - Se o probelma
        // persistir, talvez a API esteja fora do ar ou tenha atingido o número máximo,
        // por favor tente novamente mais tarde",
        // },
        // 2: {
        // "q": "Por que a tradução não funciona?",
        // "a": "Siga esses passos:\n1 - Verifique se o idioma está escrito
        // corretamente\n2 - Verifique se está conectado a internet\n3 - Se o probelma
        // persistir, talvez a API esteja fora do ar ou tenha atingido o número máximo,
        // por favor tente novamente mais tarde",
        // },
        // 3: {
        // "q": "Por que a tradução não é perfeita?",
        // "a": "A API de tradução não é perfeita, ela apenas traduz o texto, não o
        // contexto, por isso, algumas traduções podem não fazer sentido",
        // },
        // 4: {
        // "q": "Por que os posts não traduzem?",
        // "a": "Por a tradução ainda estar em fase de testes, os posts não são
        // traduzidos, apenas o menu e as mensagens do programa, para que não haja
        // problemas de tradução de contexto",
        // },

        // create a hashmap with the questions and answers

        HashMap<Integer, HashMap<String, String>> questions = new HashMap<Integer, HashMap<String, String>>();
        HashMap<String, String> question1 = new HashMap<String, String>();
        question1.put("q", "Por que o clima não aparece?");
        question1.put("a",
                "Siga esses passos:\n1 - Verifique se o nome da cidade está escrito corretamente\n2 - Verifique se está conectado a internet\n3 - Se o probelma persistir, talvez a API esteja fora do ar ou tenha atingido o número máximo, por favor tente novamente mais tarde");
        questions.put(1, question1);
        HashMap<String, String> question2 = new HashMap<String, String>();
        question2.put("q", "Por que a tradução não funciona?");
        question2.put("a",
                "Siga esses passos:\n1 - Verifique se o idioma está escrito corretamente\n2 - Verifique se está conectado a internet\n3 - Se o probelma persistir, talvez a API esteja fora do ar ou tenha atingido o número máximo, por favor tente novamente mais tarde");
        questions.put(2, question2);
        HashMap<String, String> question3 = new HashMap<String, String>();
        question3.put("q", "Por que a tradução não é perfeita?");
        question3.put("a",
                "A API de tradução não é perfeita, ela apenas traduz o texto, não o contexto, por isso, algumas traduções podem não fazer sentido");
        questions.put(3, question3);
        HashMap<String, String> question4 = new HashMap<String, String>();
        question4.put("q", "Por que os posts não traduzem?");
        question4.put("a",
                "Por a tradução ainda estar em fase de testes, os posts não são traduzidos, apenas o menu e as mensagens do programa, para que não haja problemas de tradução de contexto");
        questions.put(4, question4);

        String message = "";
        for (int i = 1; i <= questions.size(); i++) {
            message += i + " - " + questions.get(i).get("q") + "\n";
        }

        // create a show option dialog with the questions and options numbers

        List<String> options = new ArrayList<String>();
        for (int i = 1; i <= questions.size(); i++) {
            options.add(Integer.toString(i));
        }
        options.add("Retornar");
        int opcao = JOptionPane.showOptionDialog(null, message, "AgroSolution - FAQ", 0, JOptionPane.QUESTION_MESSAGE,
                null, options.toArray(), "1");

        if (opcao == -1) {
            return;
        }

        if (opcao == questions.size()) {
            return;
        }

        // show the answer of the selected question
        for (int i = 1; i <= questions.size(); i++) {
            if (opcao == i - 1) {
                JOptionPane.showMessageDialog(null, questions.get(i).get("a"));
                return;
            }
        }

    }

    public static void suggestions() {
        // suggestion with TextArea and ScrollPane
        JLabel label = new JLabel("Sugestão:");
        JTextArea textArea = new JTextArea();
        textArea.setPreferredSize(new Dimension(500, 100));
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(500, 300));
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        JPanel panel = new JPanel(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(2, 2, 2, 2);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.LINE_START;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        panel.add(label, gbc);

        gbc.gridy++;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        panel.add(scrollPane, gbc);

        int result = JOptionPane.showOptionDialog(
                null,
                panel,
                "AgroSolution",
                JOptionPane.YES_NO_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE,
                null,
                new String[] { "Enviar", "Cancelar" },
                "Enviar");

        if (result == 1) {
            return;
        }

        if (textArea.getText().equals("")) {
            JOptionPane.showMessageDialog(null, "Preencha o campo de sugestão");
            return;
        }

        String suggestion = textArea.getText();
        suggestion = suggestion.replace(";", ",");
        suggestion = suggestion.replace("\n", "\\n");
        try (BufferedWriter writer = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream("data/suggestions.txt", true), "UTF-8"))) {
            writer.append(suggestion);
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }

        JOptionPane.showMessageDialog(null, "Sugestão enviada com sucesso");
    }
}