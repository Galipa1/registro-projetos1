import com.empresa.registroprojetos.config.AppContext;
import com.empresa.registroprojetos.ui.JanelaPrincipal;
import com.empresa.registroprojetos.ui.components.Tema;
import com.empresa.registroprojetos.util.DatabaseManager;
import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {

        DatabaseManager.inicializarBanco();

        Tema.aplicar();

        AppContext ctx = new AppContext();

        SwingUtilities.invokeLater(() -> {
            JanelaPrincipal janela = new JanelaPrincipal(ctx);
            janela.setVisible(true);
        });
    }
}