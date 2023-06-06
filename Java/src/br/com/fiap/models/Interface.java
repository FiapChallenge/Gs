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
                new String[] { "0", "1", "2", "3", "4", "5", "6", "7", "8" }, "1");
        return opcao;
    }

    public static void weather() {
        String city = JOptionPane.showInputDialog(null, "Digite o nome da cidade:", "AgroSolution",
                JOptionPane.QUESTION_MESSAGE);

        List<String> info = OpenWeather.getInfo(city);
        String infoString = "Temperatura: " + info.get(0) + "°C\nTemperatura Máxima: " + info.get(1) + "°C\nTemperatura Mínima: " + info.get(2) + "°C\nUmidade: " + info.get(3) + "%\nClima: " + info.get(4) + "\nVelocidade do Vento: " + info.get(5) + "m/s\nNascer do Sol: " + info.get(6) + "\nPor do Sol: " + info.get(7);
        JOptionPane.showMessageDialog(null, infoString);
    }
}
