import com.jidesoft.docking.DockContext;
import com.jidesoft.docking.DockableFrame;
import com.jidesoft.editor.CodeEditor;
import com.jidesoft.editor.ListDataCodeEditorIntelliHints;
import com.jidesoft.editor.language.LanguageSpec;
import com.jidesoft.editor.language.LanguageSpecManager;
import com.jidesoft.editor.margin.CodeFoldingMargin;
import com.jidesoft.editor.status.CodeEditorStatusBar;
import com.jidesoft.editor.tokenmarker.JavaTokenMarker;
import com.jidesoft.swing.JideSplitPane;
import com.jidesoft.swing.JideTabbedPane;
import com.jidesoft.swing.ResizablePanel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.ScrollPane;
import java.awt.event.ActionEvent;
import java.lang.reflect.Field;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import javax.swing.AbstractAction;
import javax.swing.AbstractButton;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.SwingWorker;
import javax.swing.table.DefaultTableModel;
import xqt.api.AppInfo;
import xqt.api.LanguageServicePoint;
import xqt.model.data.Resultset;
import xqt.model.data.Variable;

/**
 *
 * @author jfd
 */
public class EditingPanel extends ResizablePanel{
    private CodeEditor codeEditor;
    private DockableFrame dataFrame = null;
    private JideTabbedPane dataPane;
    private final JScrollPane editorPane;
    private final JTextArea outputArea = new JTextArea(); // the output area of the current editor
//    private JTextArea masterOutputArea; // the output area of the shell
    JideSplitPane splitPane;
    String filePath;
    
    public EditingPanel(String filePath, String title){
        this.filePath = filePath;
        this.setLayout(new BorderLayout());
        this.setName(title);
        
        splitPane = new JideSplitPane();
        splitPane.setOrientation(JideSplitPane.VERTICAL_SPLIT);
        splitPane.setDividerSize(2);
        this.add(splitPane, BorderLayout.CENTER);

        initCodeEditor(filePath);
        editorPane = Utilities.createScrollPane(codeEditor);
        editorPane.setPreferredSize(new Dimension(200, 200));
        splitPane.add(editorPane); // above the spliter
        //splitPane.add(new JButton("Test"));

        CodeEditorStatusBar codeStatusBar = prepareCodeSatusBar();
        this.add(codeStatusBar, BorderLayout.NORTH); // better go to the top of the code editor inside the spliter
    }
    
    private synchronized void initDataFrame(){
        if(dataFrame == null){
            dataFrame = new DockableFrame("Results");
            dataFrame.setDockable(true);
            dataFrame.getContext().setInitMode(DockContext.STATE_AUTOHIDE);
            dataFrame.getContext().setInitSide(DockContext.DOCK_SIDE_SOUTH);
            dataFrame.getContext().setInitIndex(0);            
            dataFrame.setPreferredSize(new Dimension(200, 200));
            splitPane.add(dataFrame);        

            dataPane = new JideTabbedPane();
            dataPane.setPreferredSize(new Dimension(200, 200));// think of making it automatic or relative to the workspace size
            dataFrame.add(dataPane, BorderLayout.CENTER);
        }       
        while (dataPane.getTabCount() > 0)
            dataPane.remove(0);
        dataPane.addTab("Activity Log", Utilities.createScrollPane(outputArea));
        this.revalidate();
    }
    
    public synchronized void addTabularTab(String title, Resultset resultSet){
        dataPane.addTab(title, createTable(resultSet)); // JTable+data
//        dataPane.revalidate();
        this.revalidate();
    }
        
    public static EditingPanel openEditorComponent(/*JideTabbedPane container,*/ String fileName, String title/*, JTextArea masterOutputArea*/) {
        // check if the file is already open, activate its container tab
        // if not create another tab, put the editor, etc in it and activate it
        EditingPanel editingPanel = new EditingPanel(fileName, title);
//        editingPanel.masterOutputArea = masterOutputArea;
        return editingPanel;
    }
        
    public static JideTabbedPane createEditorContainer() {
        JideTabbedPane workspacePane = new JideTabbedPane();        
        return workspacePane;
    }

    private static Component createTable(Resultset resultset) {
        //List<String> columns = resultset.getSchema().stream().map(p-> p.getName()).collect(Collectors.toList());
        JTable table = new JTable();
        DefaultTableModel tableModel = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
            return false;
            }
        };
        List<String> columnNames = resultset.getSchema().stream().map(p->p.getName()).collect(Collectors.toList());
        tableModel.setColumnIdentifiers(columnNames.toArray(new String[columnNames.size()]));
        table.setModel(tableModel);
        if (resultset.getTabularData()!= null && resultset.getTabularData().size() > 0) {
            List<Object> pagedData = resultset.getTabularData().stream().limit(100).collect(Collectors.toList());
            Class<?> clazz = pagedData.get(0).getClass();
            Object[] tableRow = new Object[columnNames.size()];
                for(Object row: pagedData) {            
                    for(int i=0; i<columnNames.size(); i++){
                        try {
                            Field field = clazz.getField(columnNames.get(i));
                            try {
                                tableRow[i] = field.get(row);
                            } catch (IllegalArgumentException | IllegalAccessException ex) {
                                tableRow[i] = "ERROR";
                            }
                        } catch (NoSuchFieldException | SecurityException ex) {
                            tableRow[i] = "ERROR";
                        }
                    }
                    tableModel.addRow(tableRow);
                }
        }
        JScrollPane scroll = Utilities.createScrollPane(table);
        return scroll;
    }

    private void initCodeEditor(String fileName) {
        codeEditor = new CodeEditor();  
        codeEditor.setFileName(fileName);
        codeEditor.setTokenMarker(new XQtTokemMarker());
        LanguageSpec languageSpec = LanguageSpecManager.getInstance().getLanguageSpec("XQt");
        if (languageSpec != null) {
            languageSpec.configureCodeEditor(codeEditor);
        }
        codeEditor.setHorizontalScrollBarPolicy(ScrollPane.SCROLLBARS_AS_NEEDED);
        codeEditor.setVerticalScrollBarPolicy(ScrollPane.SCROLLBARS_AS_NEEDED);
        Set<String> stringSet = JavaTokenMarker.getKeywords().keyWordSet();
        String[] strings = stringSet.toArray(new String[stringSet.size()]);
        Arrays.sort(strings);
        ListDataCodeEditorIntelliHints<String> listDataCodeEditorIntelliHints = new ListDataCodeEditorIntelliHints<>(codeEditor, strings);
        
        CodeFoldingMargin margin = new CodeFoldingMargin(codeEditor);

        //codeEditor.getMarginArea().addMarginComponent(new CodeFoldingMargin());
        // View caret position
        // DefaultSelectionModel start and end offsets
        // setTokenMarker
        //codeEditor.setTokenMarker();
        // LanguageSpec
        // LanguageSpecManager
    }

    private CodeEditorStatusBar prepareCodeSatusBar() {
        CodeEditorStatusBar codeStatusBar = new CodeEditorStatusBar(codeEditor);
        JLabel codeMessage = new JLabel("Coding errors or messages related to the current code file go here");
        codeMessage.setForeground(Color.RED);
        codeStatusBar.add(codeMessage, 0);
        AbstractButton runButton = ShellCommandBarFactory.createRunButton();
        runButton.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
//                Thread t =  new Thread(() -> { // Swing based UI components are not thread safe! what to do then?
                    run();
//                });
//                t.start();
            }
        });
        codeStatusBar.add(runButton, 0); // attach the button to the actual script run method
        return codeStatusBar;
    }
    
    /*
    Provides a multi-threaded envoronment for the process execution.
    In the UI, it is possible for the user to have more than one processes (code editors) open.
    Runing one or more of the processes may take a long time. During the execution of one of running processes
    the user may want to go to other processes to edit, author or look at their result sets. For the UI to be responsive in this situations
    the LanguageServiceTask class is designed. the base class also contains some methods to show a sort of progress in therm of events. so that it is possible to show a progress bar if needed.
    */
    class LanguageServiceTask extends SwingWorker<LanguageServicePoint, String>{
        String processScript = "";
        double elapsedTime;
        LanguageServiceTask(String processScript){
            this.processScript = processScript;
        }
        
        /*
        Runs the process exeution task in the background using a separate thread.
        */
        @Override
        protected LanguageServicePoint doInBackground() throws Exception {
            LanguageServicePoint lsp = new LanguageServicePoint(processScript);
            long start = System.nanoTime();
            lsp.process();
            long end = System.nanoTime();
            elapsedTime = (double)(end - start) / 1000000000;
            return lsp;
        }
        
        /*
        Gets executed when the execution of the process is finished. the result is accessible by calling the get() method.
        When called, it created proper UI elements like Tables, graphs an so on and updated the UI.
        */
        @Override
        protected void done(){
            try { 
                LanguageServicePoint lsp = get();
                lsp.getEngine().getProcessModel().getStatements().values().stream().forEach( (s) -> {
                    if(s.isExecuted()){
                        if(s.hasResult()){
                            Variable v = s.getExecutionInfo().getVariable();
                            switch (v.getResult().getResultsetType()){
                                case Tabular:{
                                    outputArea.append("var: (" + v.getName() + ") contains " + v.getResult().getTabularData().size() + " records.\n");
                                    addTabularTab(v.getName(), v.getResult()); 
                                    break;
                                }
                            }
                        }                    
                    }
                });
                outputArea.append("The execution finished in " + elapsedTime + " seconds\n");                     
            } catch (Exception ignore) {
            }
        }    
    }
    
//     public static void paintJaggedLine(Graphics g, Shape a) {
//        int y = (int) (a.getBounds().getY() + a.getBounds().getHeight());
//        int x1 = (int) a.getBounds().getX();
//        int x2 = (int) (a.getBounds().getX() + a.getBounds().getWidth());
// 
//        Color old = g.getColor();
//        g.setColor(Color.red);
//        for (int i = x1; i <= x2; i += 6) {
//            g.drawArc(i + 3, y - 3, 3, 3, 0, 180);
//            g.drawArc(i + 6, y - 3, 3, 3, 180, 181);
//        }
//        g.setColor(old);
//    }
    
//     public void paint(Graphics g, Shape allocation) {
//        super.paint(g, allocation);
//        if (getAttributes().getAttribute("JAGGED_UDERLINE_ATTRIBUTE_NAME")!=null &&
//            (Boolean)getAttributes().getAttribute("JAGGED_UDERLINE_ATTRIBUTE_NAME"")) {
//            paintJaggedLine(g, allocation);
//        }
//    }
//    private static HighlightPainter painter = new HighlightPainter(){ 
//        @Override
//        public void paint(Graphics grphcs, int i, int i1, Shape shape, CodeEditor ce) {
//            try { 
//                grphcs.setColor(new Color(255, 255, 200)); 
//                grphcs.fillRect(10, 20, 50, 20); 
//                paintJaggedLine(grphcs, shape);
////                if(ce.getSelectionStart()==ce.getSelectionEnd()){ // if no selection 
////                    Rectangle r = ce.modelToView(ce.getCaretPosition()); 
////                    grphcs.setColor(new Color(255, 255, 200)); 
////                    grphcs.fillRect(0, r.y, ce.getWidth(), r.height); 
////                } 
//            }catch (Exception ignore) { 
//            }
//        }
//    };
    private void run(){
        initDataFrame();
//        try {
//            //this.revalidate();
//            codeEditor.getHighlighter().addHighlight(0, 10, painter);
//        } catch (BadLocationException ex) {
//            Logger.getLogger(EditingPanel.class.getName()).log(Level.SEVERE, null, ex);
//        }
        outputArea.setText("");
        outputArea.append(AppInfo.getFullName() + "\n");
        outputArea.append(MessageFormat.format("executing the process script {0}", this.filePath) + "\n");
        // ----------------------- process -------------------------//
        LanguageServiceTask task = new LanguageServiceTask(codeEditor.getText());
        task.execute();
        // ----------------------- process -------------------------//
        
        // there is no guarantee that the codes after the task.execute are run after it, because the task is executed in a separate thread.
    }

    public void dispose() {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
