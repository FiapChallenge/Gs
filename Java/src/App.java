import br.com.fiap.models.*;

public class App {
    static boolean debugApp = false;

    public static void main(String[] args) throws Exception {
        Sistema sb = new Sistema();
        Usuario usuarioLogado = null;

        if (debugApp) {
            usuarioLogado = sb.buscarUsuario("augustobb@live.com");
            System.out.println(usuarioLogado);
        } else {
            usuarioLogado = Interface.login(sb);
            System.out.println(usuarioLogado);
        }

        String menu = "0 - Sair\n1 - Clima\n2 - Ver Posts\n3 - Criar um Post\n4 - Remover um Post\n5 - FAQ\n6 - Sugestões de Melhoria\n7 - Trocar de Usuário\n\nEscolha uma opção:";
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
                    Interface.weather();
                    break;
                case 2:
                    Interface.show_posts(sb);
                    break;
                case 3:
                    Interface.create_post(sb, usuarioLogado);
                    break;
                case 4:
                    Interface.delete_post(sb, usuarioLogado);
                    break;
                case 5:
                    Interface.faq();
                    break;
                case 6:
                    Interface.suggestions();
                    break;
                case 7:
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
