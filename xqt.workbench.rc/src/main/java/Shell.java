

import com.jidesoft.action.DefaultDockableBarDockableHolder;
import com.jidesoft.docking.DefaultDockingManager;
import com.jidesoft.document.DocumentComponent;
import com.jidesoft.document.DocumentComponentAdapter;
import com.jidesoft.document.DocumentComponentEvent;
import com.jidesoft.document.DocumentPane;
import com.jidesoft.document.IDocumentGroup;
import com.jidesoft.document.IDocumentPane;
import com.jidesoft.navigation.NavigationTree;
import com.jidesoft.status.LabelStatusBarItem;
import com.jidesoft.status.ProgressStatusBarItem;
import com.jidesoft.status.StatusBar;
import com.jidesoft.swing.ContentContainer;
import com.jidesoft.swing.FolderChooser;
import com.jidesoft.swing.JideBoxLayout;
import com.jidesoft.swing.JideTabbedPane;
import com.jidesoft.swing.ResizablePanel;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.LinkedHashMap;
import javax.swing.AbstractAction;
import javax.swing.AbstractButton;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.tree.TreePath;

public class Shell extends DefaultDockableBarDockableHolder implements IShell{

    private static Shell mainFrame;
    private static Component workspacePane;
    private HashMap<String, ResizablePanel> openEditors = new LinkedHashMap<String, ResizablePanel>();
//    private static JideTabbedPane editorPane;
    private static IDocumentPane documentManagerPane;

    private static WindowAdapter _windowListener;
    private static StatusBar _statusBar;
    private static Timer _timer;
    private static JTextArea outputTextArea;
    private final String PROFILE_NAME = "SciQuest-IDE";
    public final String SHELL_DOCKABLE_FRAME_KEY = "ShellFrame";

     public void openProject(String projectRootPath) {     
            FileTreeModel treeModel = new FileTreeModel(new File(projectRootPath));
            NavigationTree tree = new NavigationTree(treeModel);
            tree.setCellRenderer(new FileTreeCellRenderer());
            tree.setRowHeight(20);
            tree.setVisibleRowCount(30);
            //ShellDockableFrameFactory.resetProjectViewFrame();
            documentManagerPane.closeAll();
            MouseListener ml = new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                int selRow = tree.getRowForLocation(e.getX(), e.getY());
                TreePath selPath = tree.getPathForLocation(e.getX(), e.getY());
                if(selRow != -1) {
                    if(e.getClickCount() == 1) {
                        //mySingleClick(selRow, selPath);
                    }
                    else if(e.getClickCount() == 2) {
                        openDocumentDoubleClick(selRow, selPath);
                    }
                }
            }
            private void openDocumentDoubleClick(int selRow, TreePath selPath) {
                String fileToOpen = selPath.getLastPathComponent().toString();                
                // on script file double click: add another editor component
                if(fileToOpen.endsWith(".xqt.txt")){
                    openDocument(fileToOpen);
                }
            }
        };
        tree.addMouseListener(ml);        
        ShellDockableFrameFactory.openProjectViewFrame(tree);
        this.revalidate();
    }
    
    public Shell(String title) throws HeadlessException {
        super(title);
        //this.setIconImage(JideIconsFactory.getImageIcon(JideIconsFactory.JIDE50).getImage());
    }

    public Shell() throws HeadlessException {
        this(""); 
    }

    public DefaultDockableBarDockableHolder showUp(final boolean exit) {
        mainFrame = new Shell(MessageFormat.format(ResourceManager.RB.getString("Shell.title"), 
                ResourceManager.RB.getString("Shell.version"), ResourceManager.RB.getString("Shell.xqt")));
        mainFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        mainFrame.setIconImage(ShellIconsFactory.getImageIcon(ShellIconsFactory.Logos.TITLE_BAR32).getImage());

        // add a window listener to do clear up when windows closing.
        _windowListener = new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                clearUp();
                if (exit) {
                    System.exit(0);
                }
            }
        };
        mainFrame.addWindowListener(_windowListener);

        // set the profile key
        mainFrame.getLayoutPersistence().setProfileKey(PROFILE_NAME);
        mainFrame.getLayoutPersistence().setXmlFormat(true);

        mainFrame.getDockableBarManager().setProfileKey(PROFILE_NAME);
        // add menu and tool bars
        // put then in one line if possible
        mainFrame.getDockableBarManager().addDockableBar(ShellCommandBarFactory.createMenuCommandBar(mainFrame));
        mainFrame.getDockableBarManager().addDockableBar(ShellCommandBarFactory.createStandardToolBar(mainFrame));

        // add status bar
        mainFrame.getContentPane().add(createStatusBar(), BorderLayout.AFTER_LAST_LINE);

        mainFrame.getDockingManager().getWorkspace().setAdjustOpacityOnFly(true);
        mainFrame.getDockingManager().setUndoLimit(10);
        mainFrame.getDockingManager().beginLoadLayoutData();

        mainFrame.getDockingManager().setInitSplitPriority(DefaultDockingManager.SPLIT_SOUTH_NORTH_EAST_WEST);

        // add all dockable frames
        mainFrame.getDockingManager().addFrame(ShellDockableFrameFactory.createInspectionFrame());
        mainFrame.getDockingManager().addFrame(ShellDockableFrameFactory.createProjectViewFrame());
        outputTextArea = new JTextArea();
        mainFrame.getDockingManager().addFrame(ShellDockableFrameFactory.createOutputFrame(outputTextArea));

        mainFrame.getDockingManager().setShowGripper(true);

        mainFrame.getDockingManager().getWorkspace().setLayout(new BorderLayout());
        //editorPane = EditingPanel.createEditorContainer();
        documentManagerPane = createDocumentManagerPane();
        mainFrame.getDockingManager().getWorkspace().add((Component) documentManagerPane, BorderLayout.CENTER);
            
       
        // load layout information from previous session
        mainFrame.getLayoutPersistence().loadLayoutData();

        mainFrame.toFront();
        return mainFrame;
    }

    public void openDocument(final String fileToOpen) {
        String title = new File(fileToOpen).getName(); // extract the file name
        if (!documentManagerPane.isDocumentOpened(title)) {
            EditingPanel editor = EditingPanel.openEditorComponent(fileToOpen, title/*, outputTextArea*/);
            final DocumentComponent documentComponent = new DocumentComponent(editor, title); // its possible to pass an icon too.
            openEditors.put(title, editor);
            documentComponent.addDocumentComponentListener(new DocumentComponentAdapter() {
                @Override
                public void documentComponentOpened(DocumentComponentEvent e) {
                }

                @Override
                public void documentComponentClosing(DocumentComponentEvent e) {
//                    if (documentManagerPane.getDocumentCount() <= 1) {
//                        getDockingManager().hideFrame(OPTIONS_DOCKABLE_FRAME_KEY);
//                    }
                    editor.dispose();
                }

                @Override
                public void documentComponentClosed(DocumentComponentEvent e) {
                }

                @Override
                public void documentComponentActivated(final DocumentComponentEvent e) {
                    // the code is here as a sample only
//                    DockableFrame frame = getDockingManager().getFrame(OPTIONS_DOCKABLE_FRAME_KEY);
//                    if (frame.isHidden()) {
//                        getDockingManager().showFrame(OPTIONS_DOCKABLE_FRAME_KEY, false);
//                    }
                }

                @Override
                public void documentComponentDeactivated(DocumentComponentEvent e) {
                }
            });
            documentManagerPane.openDocument(documentComponent);
        }
        documentManagerPane.setActiveDocument(title, true);
    }

    private static void clearUp() {
        mainFrame.removeWindowListener(_windowListener);
        _windowListener = null;
        if (mainFrame.getLayoutPersistence() != null) {
            mainFrame.getLayoutPersistence().saveLayoutData();
        }

        if (workspacePane != null) {
            //workspacePane.dispose();
            workspacePane = null;
        }
        if (_statusBar != null && _statusBar.getParent() != null)
            _statusBar.getParent().remove(_statusBar);
        _statusBar = null;
        mainFrame.dispose();
        mainFrame = null;
    }

    private static StatusBar createStatusBar() {
        // setup status bar
        // CodeEditorStatusBar
        StatusBar statusBar = new StatusBar();

        final LabelStatusBarItem statusMessage = new LabelStatusBarItem("Line");
        statusMessage.setText("Update this bar when something in the app is happening or has happend!");
        statusMessage.setPreferredWidth(500);
        statusMessage.setAlignment(JLabel.LEFT);
        statusBar.add(statusMessage, JideBoxLayout.FLEXIBLE);

        final ProgressStatusBarItem progress = new ProgressStatusBarItem();
        progress.setCancelCallback(new ProgressStatusBarItem.CancelCallback() {
            public void cancelPerformed() {
                _timer.stop();
                _timer = null;
                progress.setStatus(ResourceManager.RB.getString("Shell.canceled"));
                progress.showStatus();
            }
        });
        
        statusBar.add(progress, JideBoxLayout.VARY);

//        final OvrInsStatusBarItem ovr = new OvrInsStatusBarItem();
//        ovr.setPreferredWidth(100);
//        ovr.setAlignment(JLabel.CENTER);
//        statusBar.add(ovr, JideBoxLayout.FLEXIBLE);

//        final MemoryStatusBarItem gc = new MemoryStatusBarItem();
//        gc.setPreferredWidth(100);
//        statusBar.add(gc, JideBoxLayout.FLEXIBLE);

        return statusBar;
    }

    @Override
    protected ContentContainer createContentContainer() {
        return new LogoContentContainer();
    }

    private class LogoContentContainer extends ContentContainer {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            //ImageIcon imageIcon = JideIconsFactory.getImageIcon(JideIconsFactory.JIDELOGO);
            //imageIcon.paintIcon(this, g, getWidth() - imageIcon.getIconWidth() - 2, 2);
        }
    }

    private FolderChooser folderChooser;
    private File currentFolder = null;
    public JTextField textField;
    
    private AbstractButton createBrowseButton() {
        final JButton button = new JButton("Browse");
        button.setMnemonic('B');
        button.addActionListener(new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                if (textField.getText().length() > 0) {
                    currentFolder = folderChooser.getFileSystemView().createFileObject(textField.getText());
                }
                folderChooser.setCurrentDirectory(currentFolder);
                folderChooser.setFileHidingEnabled(true);
                int result = folderChooser.showOpenDialog(button.getTopLevelAncestor());
                if (result == FolderChooser.APPROVE_OPTION) {
                    currentFolder = folderChooser.getSelectedFile();
                    File selectedFile = folderChooser.getSelectedFile();
                    if (selectedFile != null) {
                        textField.setText(selectedFile.toString());
                    }
                    else {
                        textField.setText("");
                    }
                }
            }
        });
        button.setRequestFocusEnabled(false);
        button.setFocusable(false);
        return button;
    }    
    
    private static boolean _autohideAll = true;
    private static byte[] _fullScreenLayout;

    private static DocumentPane createDocumentManagerPane() {
        final DocumentPane pane = new DocumentPane() {
            // add function to maximize (autohideAll) the document pane when mouse double clicks on the tabs of DocumentPane.
            @Override
            protected IDocumentGroup createDocumentGroup() {
                IDocumentGroup group = super.createDocumentGroup();
                if (group instanceof JideTabbedPane) {
                    ((JideTabbedPane) group).addMouseListener(new MouseAdapter() {
                        @Override
                        public void mouseClicked(MouseEvent e) {
                            if (SwingUtilities.isLeftMouseButton(e) && e.getClickCount() == 2) {
                                if (!_autohideAll) {
                                    _fullScreenLayout = mainFrame.getDockingManager().getLayoutRawData();
                                    mainFrame.getDockingManager().autohideAll();
                                    _autohideAll = true;
                                }
                                else {
                                    if (_fullScreenLayout != null) {
                                        mainFrame.getDockingManager().setLayoutRawData(_fullScreenLayout);
                                    }
                                    _autohideAll = false;
                                }
                            }
                        }
                    });
                }
                return group;
            }
        };
        pane.registerKeyboardAction(new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                pane.closeDocument(pane.getActiveDocumentName());
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_F4, KeyEvent.CTRL_MASK), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        pane.registerKeyboardAction(new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                pane.nextDocument();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, KeyEvent.ALT_MASK), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        pane.registerKeyboardAction(new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                pane.prevDocument();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, KeyEvent.ALT_MASK | KeyEvent.SHIFT_MASK), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        pane.setTabPlacement(JTabbedPane.TOP);
        pane.setName("DocumentManagerPane");
        return pane;
    }

    
}
