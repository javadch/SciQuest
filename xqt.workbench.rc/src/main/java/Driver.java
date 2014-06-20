
import com.jidesoft.plaf.LookAndFeelFactory;
import com.jidesoft.utils.PortingUtils;
import javax.swing.SwingUtilities;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author standard
 */
public class Driver {
    public static void main(String[] args) {
        com.jidesoft.utils.Lm.verifyLicense("Friedrich Schiller University of Jena", "SciQuest", "iBVmHbKikKMgQhcRthIhOwcUROnqer3");
        SwingUtilities.invokeLater(() -> {
            PortingUtils.prerequisiteChecking();
            LookAndFeelFactory.installDefaultLookAndFeelAndExtension();
            Shell shell = new Shell();
            shell.showUp(true);
        });
    }
}