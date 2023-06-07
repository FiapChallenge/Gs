import br.com.fiap.models.*;
import br.com.fiap.utilities.*;

public class App {
    static boolean debugApp = true;

    public static void main(String[] args) throws Exception {
        Sistema sb = new Sistema();
        Usuario usuarioLogado = null;
        OpenWeather.getInfo("São Paulo");

        if (debugApp) {
            usuarioLogado = sb.buscarUsuario("augustobb@live.com");
            System.out.println(usuarioLogado);
        } else {
            usuarioLogado = Interface.login(sb);
            System.out.println(usuarioLogado);
        }

        String menu = "0 - Sair\n1 - Configurações\n2 - Clima\n3 - Diário Agrícola\n4 - Ver Posts\n5 - Criar um Post\n6 - Remover um Post\n7 - FAQ\n8 - Sugestões de Melhoria\n9 - Trocar de Usuário\n\nEscolha uma opção:";
        while (true) {
            int opcao = Interface.menu(usuarioLogado, menu);
            switch (opcao) {
                case -1:
                    System.exit(0);
                    break;
                case 0:
                    sb.saveData();
                    System.exit(0);
                    break;
                case 1:
                    break;
                case 2:
                    Interface.weather();
                    break;
                case 3:
                    break;
                case 4:
                    Interface.show_posts(sb);
                    break;
                case 5:
                    Interface.create_post(sb, usuarioLogado);
                    break;
                case 6:
                    Interface.delete_post(sb, usuarioLogado);
                    break;
                case 7:
                    Interface.faq();
                    break;
                case 8:
                    Interface.suggestions();
                    break;
                case 9:
                    usuarioLogado = Interface.login(sb);
                    System.out.println(usuarioLogado);
                    break;
                default:
                    System.exit(0);
                    break;

            }
        }

    }
}
