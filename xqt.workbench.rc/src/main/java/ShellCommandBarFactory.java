import com.jidesoft.action.CommandBar;
import com.jidesoft.action.CommandBarFactory;
import com.jidesoft.action.CommandMenuBar;
import com.jidesoft.action.DefaultDockableBarDockableHolder;
import com.jidesoft.action.DockableBarContext;
import com.jidesoft.alert.Alert;
import com.jidesoft.docking.DockableHolder;
import com.jidesoft.docking.DockingManager;
import com.jidesoft.status.MemoryStatusBarItem;
import com.jidesoft.swing.JideBoxLayout;
import com.jidesoft.swing.JideMenu;
import com.jidesoft.swing.PersistenceUtils;

import javafx.scene.control.Alert.AlertType;

import java.awt.Component;
import java.awt.Container;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import javax.swing.AbstractAction;
import javax.swing.AbstractButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;

/**
 */
public class ShellCommandBarFactory extends CommandBarFactory {
    public static String _lastDirectory = ".";
   
    public static CommandBar createMenuCommandBar(Container container) {
        CommandBar commandBar = new CommandMenuBar("Menu Bar");
        commandBar.setInitSide(DockableBarContext.DOCK_SIDE_NORTH);
        commandBar.setInitIndex(0);
        commandBar.setPaintBackground(false);
        commandBar.setStretch(true);
        commandBar.setFloatable(true);
        commandBar.setHidable(false);

        commandBar.add(createFileMenu(container));
        commandBar.add(createEditMenu(container));
        commandBar.add(createProjectMenu(container));
        commandBar.add(createToolsMenu(container));
        commandBar.add(createWindowsMenu(container));
        commandBar.add(createHelpMenu(container));
       
        return commandBar;
    }

    public static CommandBar createStandardToolBar(final Container container) {
        CommandBar commandBar = new CommandBar("Standard");
        commandBar.setInitSide(DockableBarContext.DOCK_SIDE_NORTH);
        commandBar.setInitMode(DockableBarContext.STATE_HORI_DOCKED);
        commandBar.setInitIndex(1);

        commandBar.add(createSplitButton(ShellIconsFactory.getImageIcon(ShellIconsFactory.Standard.ADD_NEW_ITEMS)));
//        commandBar.add(createMenu(VsnetIconsFactory.getImageIcon(VsnetIconsFactory.Standard.ADD_NEW_ITEMS)));
//        commandBar.add(createButton(VsnetIconsFactory.getImageIcon(VsnetIconsFactory.Standard.ADD_NEW_ITEMS)));
        
              
        AbstractButton openButton = createButton(ShellIconsFactory.getImageIcon(ShellIconsFactory.Standard.OPEN));
        openButton.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openDialog(container, JFileChooser.DIRECTORIES_ONLY);
            }
        });
        commandBar.add(openButton);
        
        AbstractButton saveButton = createButton(ShellIconsFactory.getImageIcon(ShellIconsFactory.Standard.SAVE));
        saveButton.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveCurrentOpenFile(container);
            }
        });
        commandBar.add(saveButton);
        
        AbstractButton saveAllButton = createButton(ShellIconsFactory.getImageIcon(ShellIconsFactory.Standard.SAVE_ALL));
        saveAllButton.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveAllOpenFiles(container);
            }
        });
        commandBar.add(saveAllButton);

        commandBar.addSeparator();

        commandBar.add(createButton(ShellIconsFactory.getImageIcon(ShellIconsFactory.Standard.CUT)));
        commandBar.add(createButton(ShellIconsFactory.getImageIcon(ShellIconsFactory.Standard.COPY)));
        commandBar.add(createButton(ShellIconsFactory.getImageIcon(ShellIconsFactory.Standard.PASTE)));
        commandBar.addSeparator();

        AbstractButton undoButton = createSplitButton(ShellIconsFactory.getImageIcon(ShellIconsFactory.Standard.UNDO));
        undoButton.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                undoChanges(container);
            }
        });
        commandBar.add(undoButton);

        AbstractButton redoButton = createSplitButton(ShellIconsFactory.getImageIcon(ShellIconsFactory.Standard.REDO));
        undoButton.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                redoChanges(container);
            }
        });
        commandBar.add(redoButton);

//        commandBar.add(createSplitButton(ShellIconsFactory.getImageIcon(ShellIconsFactory.Standard.NAVIGATE_BACKWARD)));
//        commandBar.add(createSplitButton(ShellIconsFactory.getImageIcon(ShellIconsFactory.Standard.NAVIGATE_FORWARD)));
        commandBar.addSeparator();

        AbstractButton runButton = createSplitButton(ShellIconsFactory.getImageIcon(ShellIconsFactory.Standard.START));
        runButton.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                this.setEnabled(false);
                runAllOpenProcesses(container);
                this.setEnabled(true);
            }
        });
        commandBar.add(runButton);
        commandBar.addSeparator();

        commandBar.add(createButton(ShellIconsFactory.getImageIcon(ShellIconsFactory.Standard.FIND_IN_FILES)));
        commandBar.addSeparator();

//        JideSplitButton splitButton = (JideSplitButton) commandBar.add(createSplitButton(ShellIconsFactory.getImageIcon(ShellIconsFactory.Standard.CLASSVIEW)));
//
//        splitButton.add(new JMenuItem("Command Window", ShellIconsFactory.getImageIcon(ShellIconsFactory.Standard.COMMAND)));
//        splitButton.add(new JMenuItem("Output", ShellIconsFactory.getImageIcon(ShellIconsFactory.Standard.OUTPUT)));
//        splitButton.addSeparator();
//
//        splitButton.add(new JMenuItem("Find Result 1", ShellIconsFactory.getImageIcon(ShellIconsFactory.Standard.FINDRESULT1)));
//        splitButton.add(new JMenuItem("Find Result 2", ShellIconsFactory.getImageIcon(ShellIconsFactory.Standard.FINDRESULT2)));
//        splitButton.add(new JMenuItem("Find Symbol", ShellIconsFactory.getImageIcon(ShellIconsFactory.Standard.FINDSYMBOL)));
//        splitButton.addSeparator();
//
//        splitButton.add(new JMenuItem("Favorites", ShellIconsFactory.getImageIcon(ShellIconsFactory.Standard.FAVORITES)));
        final MemoryStatusBarItem gc = new MemoryStatusBarItem();
        gc.setPreferredWidth(100);
        commandBar.add(gc, JideBoxLayout.VARY);
        return commandBar;
    }

    private static JMenu createFileMenu(final Container container) {
        JMenuItem item;

        JMenu menu = new JideMenu(ResourceManager.RB.getString("Shell.Menu.File.title"));
        menu.setMnemonic(ResourceManager.RB.getString("Shell.Menu.FiSystem.exit(0);le.mnemonic").charAt(0));

        item = new JMenuItem(ResourceManager.RB.getString("Shell.Menu.File.NewProject.title"), 
                ShellIconsFactory.getImageIcon(ShellIconsFactory.Standard.ADD_NEW_ITEMS));
        item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P, InputEvent.CTRL_DOWN_MASK)); // Added by arefin
        item.addActionListener(new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                createProjectDialog(container, JFileChooser.DIRECTORIES_ONLY);
            }
        });
        menu.add(item);

        item = new JMenuItem(ResourceManager.RB.getString("Shell.Menu.File.NewProcess.title"), 
                ResourceManager.RB.getString("Shell.Menu.File.NewProcess.mnemonic").charAt(0));
        item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_DOWN_MASK)); // Added by arefin
        item.addActionListener(new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                createProcessDialog(container, JFileChooser.DIRECTORIES_ONLY);
            }
        });
        menu.add(item);

        item = new JMenuItem(ResourceManager.RB.getString("Shell.Menu.File.OpenProject.title"), 
                ShellIconsFactory.getImageIcon(ShellIconsFactory.Standard.OPEN));
        item.setMnemonic(ResourceManager.RB.getString("Shell.Menu.File.OpenProject.title").charAt(0));
        item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.ALT_DOWN_MASK)); // Added by arefin
        item.addActionListener(new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                openDialog(container, JFileChooser.DIRECTORIES_ONLY);
            }
        });
        menu.add(item);

        item = new JMenuItem(ResourceManager.RB.getString("Shell.Menu.File.CloseProject.title"), 
                ResourceManager.RB.getString("Shell.Menu.File.CloseProject.mnemonic").charAt(0));
        item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, InputEvent.ALT_DOWN_MASK)); // Added by arefin
        menu.add(item);

        menu.addSeparator();

        item = new JMenuItem(ResourceManager.RB.getString("Shell.Menu.File.Exit.title"), 
                ResourceManager.RB.getString("Shell.Menu.File.Exit.mnemonic").charAt(0));
        item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, InputEvent.ALT_DOWN_MASK)); // Added by arefin
        item.addActionListener(new AbstractAction() {
            public void actionPerformed(ActionEvent e) {System.exit(0);
                // check for open dirty process files and ask for save
            	/// Added By Arefin
            	//default icon, custom title
            	int n = JOptionPane.showConfirmDialog(null,
            	    "Do you want to save the Process Files?",
            	    "Exit!",
            	    JOptionPane.YES_NO_OPTION);
            		
            		if(n==0){
            			// save the layout silently
            		((IShell)container).saveAllDocuments();
            		System.exit(0);
            		}
            		else
                    System.exit(0);
            	///
            }
        });
        menu.add(item);
        return menu;
    }

    private static JMenu createEditMenu(Container container) {
        JMenu menu = new JideMenu("Edit");
        menu.setMnemonic('E');
        JMenuItem item;

        item = new JMenuItem("Undo");
        item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z, InputEvent.CTRL_DOWN_MASK));        
        menu.add(item);

        item = new JMenuItem("Redo");
        item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Y, InputEvent.CTRL_DOWN_MASK));        
        menu.add(item);
        menu.addSeparator();

        item = new JMenuItem("Cut");
        item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, InputEvent.CTRL_DOWN_MASK));        
        menu.add(item);
        
        item = new JMenuItem("Copy");
        item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, InputEvent.CTRL_DOWN_MASK));        
        menu.add(item);

        item = new JMenuItem("Paste");
        item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V, InputEvent.CTRL_DOWN_MASK));        
        menu.add(item);

        item = new JMenuItem("Select All");
        item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, InputEvent.CTRL_DOWN_MASK));        
        menu.add(item);

        menu.addSeparator();

        item = new JMenuItem("Find");
        item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F, InputEvent.CTRL_DOWN_MASK));        
        menu.add(item);

        item = new JMenuItem("Find Next");
        item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F3, 0));        
        menu.add(item);
        
        item = new JMenuItem("Find Previous");
        item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z, InputEvent.SHIFT_DOWN_MASK));        
        menu.add(item);
        
        return menu;
    }

    private static JMenu createProjectMenu(final Container container) {
        JMenu menu = new JideMenu("Project");
        menu.setMnemonic('J');
        JMenuItem item;
        
        item = new JMenuItem("Run");
        item.setMnemonic('R');
        item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F5, 0)); // add Alt+X too
        //item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F6, InputEvent.SHIFT_MASK));        
        //item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_1, InputEvent.CTRL_MASK | InputEvent.ALT_MASK));        
        item.addActionListener(new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                if (container instanceof DockableHolder) {
                    DockingManager dockingManager = ((DockableHolder) container).getDockingManager();
                    String frameKey = dockingManager.getNextFrame(dockingManager.getActiveFrameKey());
                    if (frameKey != null) {
                        //dockingManager.showFrame(frameKey);
                        //run the script of the active doc!
                    }
                }
            }
        });
        menu.add(item);
        
        return menu;
    }

    private static JMenu createToolsMenu(Container container) {
        JMenu menu = new JideMenu("Tools");
        menu.setMnemonic('T');
        JMenuItem item;
        
        menu.add(createLookAndFeelMenu(container)); // delete candidate
        menu.addSeparator();
        //menu.add(createOptionMenu(container));
        
        item = new JMenuItem("Adapters");
        menu.add(item);
        return menu;
    }

    private static JMenu createWindowsMenu(final Container container) {
        JMenu menu = new JideMenu("Window");
        menu.setMnemonic('W');

        JMenuItem item;

        item = new JMenuItem("Reset Layout");
        item.addActionListener(new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                if (container instanceof DefaultDockableBarDockableHolder) {
                    ((DefaultDockableBarDockableHolder) container).getLayoutPersistence().resetToDefault();
                }
            }
        });
        menu.add(item);

        menu.addSeparator();

        item = new JMenuItem("Export Layout as XML");
        item.addActionListener(new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                if (container instanceof DefaultDockableBarDockableHolder && ((DefaultDockableBarDockableHolder) container).getLayoutPersistence() != null) {
                    try {
                        JFileChooser chooser = new JFileChooser() {
                            @Override
                            protected JDialog createDialog(Component parent) throws HeadlessException {
                                JDialog dialog = super.createDialog(parent);
                                dialog.setTitle("Save the layout as an \".xml\" file");
                                return dialog;
                            }
                        };
                        chooser.setCurrentDirectory(new File(_lastDirectory));
                        int result = chooser.showDialog(((JMenuItem) e.getSource()).getTopLevelAncestor(), "Save");
                        if (result == JFileChooser.APPROVE_OPTION) {
                            _lastDirectory = chooser.getCurrentDirectory().getAbsolutePath();
                            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                            DocumentBuilder builder = factory.newDocumentBuilder();
                            Document document = builder.newDocument();
                            ((DefaultDockableBarDockableHolder) container).getLayoutPersistence().saveLayoutTo(document);
                            PersistenceUtils.saveXMLDocumentToFile(document, chooser.getSelectedFile().getAbsolutePath(), PersistenceUtils.getDefaultXmlEncoding());
                        }
                    }
                    catch (ParserConfigurationException e1) {
                        e1.printStackTrace();
                    }
                    catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        });
        menu.add(item);

        item = new JMenuItem("Import Layout from XML");
        item.addActionListener(new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                if (container instanceof DefaultDockableBarDockableHolder) {
                    JFileChooser chooser = new JFileChooser() {
                        @Override
                        protected JDialog createDialog(Component parent) throws HeadlessException {
                            JDialog dialog = super.createDialog(parent);
                            dialog.setTitle("Load an \".xml\" file");
                            return dialog;
                        }
                    };
                    chooser.setCurrentDirectory(new File(_lastDirectory));
                    int result = chooser.showDialog(((JMenuItem) e.getSource()).getTopLevelAncestor(), "Open");
                    if (result == JFileChooser.APPROVE_OPTION) {
                        _lastDirectory = chooser.getCurrentDirectory().getAbsolutePath();
                        ((DefaultDockableBarDockableHolder) container).getLayoutPersistence().loadLayoutDataFromFile(chooser.getSelectedFile().getAbsolutePath());
                    }
                }
            }
        });
        menu.add(item);

        return menu;
    }

    private static JMenu createHelpMenu(Container container) {
        JMenu menu = new JideMenu("Help");
        menu.setMnemonic('H');
        JMenuItem item;

        item = new JMenuItem("About");
        menu.add(item);

        item = new JMenuItem("Licenses");
        menu.add(item);

        item = new JMenuItem("FAQ");
        menu.add(item);

        item = new JMenuItem("Manuals");
        menu.add(item);

        return menu;
    }
    
    public static AbstractButton createRunButton(){
        return createButton(ShellIconsFactory.getImageIcon(ShellIconsFactory.Standard.START));
    }
    
    private static void openDialog(Component container, int mode) {
        final JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogType(JFileChooser.OPEN_DIALOG);
        fileChooser.setFileSelectionMode(mode);
        int returnVal = fileChooser.showOpenDialog(container);
        if ( returnVal == JFileChooser.OPEN_DIALOG ){
            File fileName = fileChooser.getSelectedFile();
            File path=fileChooser.getCurrentDirectory();
            if ( ( fileName == null ) || ( fileName.getName().equals( "" ) ) )
            {
               //JOptionPane.showMessageDialog( this, "Invalid File Name", "Invalid File Name", JOptionPane.ERROR_MESSAGE );
            }
            else if (mode == JFileChooser.DIRECTORIES_ONLY){
                ((IShell)container).openProject(fileName.toString(), true);
            } else {
                // Show an error message. user should have chosen a directory
                //String currentPath=path.getPath()+"\\"+fileName.getName();
            }
        }         
    }    
    
    private static void createProjectDialog(Component container, int mode) {
        final JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogType(JFileChooser.SAVE_DIALOG);
        fileChooser.setFileSelectionMode(mode);
        int returnVal = fileChooser.showSaveDialog(container);
        if ( returnVal == JFileChooser.APPROVE_OPTION ){
            File path=fileChooser.getSelectedFile();
            ((IShell)container).createProject(path.getAbsolutePath().toString());
        }         
    }   
      
    private static void createProcessDialog(Component container, int mode) {
//        final JFileChooser fileChooser = new JFileChooser();
//        fileChooser.setDialogType(JFileChooser.SAVE_DIALOG);
//        fileChooser.setFileSelectionMode(mode);
//        int returnVal = fileChooser.showOpenDialog(container);
//        if ( returnVal == JFileChooser.SAVE_DIALOG ){
//            File path=fileChooser.getCurrentDirectory();
//            
//        }   
        ((IShell)container).createDocument();
    }   

    private static void saveCurrentOpenFile(Component container) {
        ((IShell)container).saveDocument();
    }
    
    private static void saveAllOpenFiles(Component container) {
        ((IShell)container).saveAllDocuments();
    }

    private static void undoChanges(Container container) {
        ((IShell)container).undoChnages();
    }
    
    private static void redoChanges(Container container) {
        ((IShell)container).redoChnages();
    }
    
    public static void runAllOpenProcesses(Container container){
        ((IShell)container).runAllOpenProcesses();
    }
}
