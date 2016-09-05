import com.jidesoft.docking.DockContext;
import com.jidesoft.docking.DockableFrame;
import com.jidesoft.navigation.NavigationTree;
import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/**
 */
public class ShellDockableFrameFactory {
    private static DockableFrame projectFrame;
    private static JTextArea dummyTextArea;
    private static JScrollPane treeArea;
    
    public static void resetProjectViewFrame() {
        if(dummyTextArea == null)
            dummyTextArea = new JTextArea();
        treeArea = Utilities.createScrollPane(new JTextArea());
        projectFrame.add(treeArea);
    }
    
    public static DockableFrame openProjectViewFrame(NavigationTree tree) {
        if(projectFrame == null)
            createProjectViewFrame();
        projectFrame.remove(treeArea);
        treeArea = Utilities.createScrollPane(tree);
        projectFrame.add(treeArea);
        projectFrame.setPreferredSize(new Dimension(250, 200));
        return projectFrame;
    }
    
    public static DockableFrame clearProjectViewFrame() {
        if(projectFrame == null)
            createProjectViewFrame();
        projectFrame.remove(treeArea);
        treeArea = Utilities.createScrollPane(new JTextArea());
        projectFrame.add(treeArea);
        projectFrame.setPreferredSize(new Dimension(250, 200));
        return projectFrame;
    }
    
    public static DockableFrame createProjectViewFrame() {
        projectFrame = new DockableFrame("Project", ShellIconsFactory.getImageIcon(ShellIconsFactory.Standard.SOLUTION));
        projectFrame.getContext().setInitMode(DockContext.STATE_FRAMEDOCKED);
        projectFrame.getContext().setInitSide(DockContext.DOCK_SIDE_WEST);
        resetProjectViewFrame();
        projectFrame.setPreferredSize(new Dimension(250, 200));
        return projectFrame;
    }

    public static DockableFrame createInspectionFrame() {
        DockableFrame frame = new DockableFrame("Inspection", ShellIconsFactory.getImageIcon(ShellIconsFactory.Standard.SERVER));
        frame.getContext().setInitMode(DockContext.STATE_AUTOHIDE);
        frame.getContext().setInitSide(DockContext.DOCK_SIDE_EAST);
        frame.getContext().setInitIndex(0);
        frame.add(Utilities.createScrollPane(new JTextArea()));
        frame.setPreferredSize(new Dimension(200, 200));
        return frame;
    }

    public static DockableFrame createOutputFrame(JTextArea textArea) {
        DockableFrame frame = new DockableFrame("Output", ShellIconsFactory.getImageIcon(ShellIconsFactory.Standard.OUTPUT));
        frame.getContext().setInitMode(DockContext.STATE_AUTOHIDE);
        frame.getContext().setInitSide(DockContext.DOCK_SIDE_SOUTH);
        frame.getContext().setInitIndex(0);
        frame.add(Utilities.createScrollPane(textArea), BorderLayout.CENTER);
        //frame.setPreferredSize(new Dimension(200, 100));
        return frame;
    }

    public static DockableFrame createFindResultFrame() {
        DockableFrame frame = new DockableFrame("Find Results", ShellIconsFactory.getImageIcon(ShellIconsFactory.Standard.FINDRESULT));
        frame.getContext().setInitMode(DockContext.STATE_AUTOHIDE);
        frame.getContext().setInitSide(DockContext.DOCK_SIDE_SOUTH);
        frame.getContext().setInitIndex(0);
        JTextArea textArea = new JTextArea();
        frame.add(Utilities.createScrollPane(textArea));
        textArea.setText("");
        frame.setPreferredSize(new Dimension(200, 200));
        return frame;
    }

}
