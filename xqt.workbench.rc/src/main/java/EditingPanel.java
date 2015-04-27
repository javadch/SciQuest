import com.jidesoft.chart.Chart;
import com.jidesoft.docking.DockContext;
import com.jidesoft.docking.DockableFrame;
import com.jidesoft.editor.CodeEditor;
import com.jidesoft.editor.ListDataCodeEditorIntelliHints;
import com.jidesoft.editor.language.LanguageSpec;
import com.jidesoft.editor.language.LanguageSpecManager;
import com.jidesoft.editor.margin.CodeFoldingMargin;
import com.jidesoft.editor.status.CodeEditorStatusBar;
import com.jidesoft.editor.tokenmarker.JavaTokenMarker;
import com.jidesoft.grid.SortableTable;
import com.jidesoft.grid.TableModelWrapperUtils;
import com.jidesoft.paging.PageNavigationBar;
import com.jidesoft.paging.PageNavigationSupport;
import com.jidesoft.swing.JideSplitPane;
import com.jidesoft.swing.JideTabbedPane;
import com.jidesoft.swing.ResizablePanel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.ScrollPane;
import java.awt.event.ActionEvent;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.IOException;
import java.lang.reflect.Field;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.swing.AbstractAction;
import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.SwingWorker;
import javax.swing.table.DefaultTableModel;
import xqt.api.AppInfo;
import xqt.api.LanguageServicePoint;
import xqt.model.data.Resultset;
import xqt.model.data.Variable;
import xqt.model.exceptions.LanguageException;

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
    private AbstractButton runButton;
    
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
        //dataPane.addTab(title, createTable(resultSet)); // JTable+data
        dataPane.addTab(title, createTablePanel(resultSet));
//        dataPane.revalidate();
        this.revalidate();
    }
        
    public synchronized void addChartTab(String title, Resultset resultSet){
        dataPane.addTab(title, createChart(resultSet)); // JTable+data
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

    private static Component createChart(Resultset resultSet) {
        Chart chart = (Chart)resultSet.getData();
        JScrollPane scroll = Utilities.createScrollPane(chart);
        return scroll;        
    }
    
    private static Component createTable(Resultset resultset) {
        //List<String> columns = resultset.getSchema().stream().map(p-> p.getName()).collect(Collectors.toList());
        DefaultTableModel tableModel = populateTableModel(resultset);
        JTable table = new JTable();
        table.setModel(tableModel);
        JScrollPane scroll = Utilities.createScrollPane(table);
        return scroll;
    }

    private static DefaultTableModel populateTableModel(Resultset resultset){
       DefaultTableModel tableModel = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        }; 
        List<String> columnNames = resultset.getSchema().stream().map(p->p.getName()).collect(Collectors.toList());
        tableModel.setColumnIdentifiers(columnNames.toArray(new String[columnNames.size()]));
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
       return tableModel;
    }
  
    private static Component createTablePanel(Resultset resultset) {
        DefaultTableModel tableModel = populateTableModel(resultset);
        
        final SortableTable table = new SortableTable(tableModel);
        //table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        final JScrollPane scroller = Utilities.createScrollPane(table);
        scroller.getVerticalScrollBar().addAdjustmentListener(new AdjustmentListener() {
            public void adjustmentValueChanged(AdjustmentEvent e) {
                table.setCellContentVisible(!e.getValueIsAdjusting());
            }
        });
        scroller.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                int rowCount = scroller.getViewport().getHeight() / table.getRowHeight();
                PageNavigationSupport pageNavigationSupport = (PageNavigationSupport) TableModelWrapperUtils.getActualTableModel(table.getModel(), PageNavigationSupport.class);
                if (pageNavigationSupport != null) {
                    pageNavigationSupport.setPageSize(rowCount);
                }
            }
        });

        JPanel panel = new JPanel(new BorderLayout(2, 2));
        panel.setBorder(BorderFactory.createCompoundBorder());
                //BorderFactory.createTitledBorder("The resulting data"),
                //BorderFactory.createEmptyBorder(5, 10, 5, 10)));
        panel.add(scroller);
        PageNavigationBar pageNavigationBar = new PageNavigationBar(table);       
        pageNavigationBar.setName("tablePageNavigationBar");
        //PageNavigationBar _tablePageNavigationBar = pageNavigationBar;
        panel.add(pageNavigationBar, BorderLayout.AFTER_LAST_LINE);
        return panel;
    }
    
    private void initCodeEditor(String fileName) {
        codeEditor = new CodeEditor();  
        codeEditor.setFileName(fileName);
        codeEditor.setTokenMarker(new XQtTokenMarker());
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
        JLabel codeMessage = new JLabel("" /*"Coding errors or messages related to the current code file go here"*/);
        codeMessage.setForeground(Color.RED);
        codeStatusBar.add(codeMessage, 0);
        runButton = ShellCommandBarFactory.createRunButton();
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
        //LanguageServicePoint lsp;
        
        LanguageServiceTask(String processScript){
            this.processScript = processScript;
        }
        
        /*
        Runs the process exeution task in the background using a separate thread.
        */
        int errorCount = 0;
        @Override
        protected LanguageServicePoint doInBackground()  {
            LanguageServicePoint lsp = new LanguageServicePoint(processScript);
            if(!lsp.hasError()){
                long start = System.nanoTime();
                lsp.process();
                long end = System.nanoTime();
                elapsedTime = (double)(end - start) / 1000000000;
                errorCount = 0;
            }
            return lsp;
        }
        
        /*
        Gets executed when the execution of the process is finished. the result is accessible by calling the get() method.
        When called, it created proper UI elements like Tables, graphs an so on and updated the UI.
        */
        @Override
        protected void done(){
            LanguageServicePoint lsp = null; 
            try {
                lsp = get(); // check what happens if doInBackground throws an exception. answer: the lsp does not throw any expetion, instea it collects and returns an exception list.
            } catch (InterruptedException | ExecutionException ex) { // execution errors
                outputArea.append("Program execution was interrupted. " + ex.getMessage() + "\n");
            } 
            // see whether the process model contains any exception, if so throw an InputMismatchException
            // to singal the callers to go through the process model and handle the actual exceptions.
            
            if(lsp != null && lsp.hasError()){ // lexical, syntax and ... error
                outputArea.append("**************************************************************************************\n");
                outputArea.append("************************************ Lexical Errors ***********************************\n");
                outputArea.append("**************************************************************************************\n");
                lsp.getExceptions().forEach(p-> {                    
                    outputArea.append("Error " + ++errorCount + " : " + p.getMessage()+ "\n");  
                }  
                );
            } 
            if(lsp!=null && lsp.getEngine() != null && lsp.getEngine().getProcessModel() != null) {
                if(lsp.getEngine().getProcessModel().hasError()){ // semantic errors
                    outputArea.append("**************************************************************************************\n");
                    outputArea.append("******************************* Synatx and Semantic Errors *******************************\n");
                    outputArea.append("**************************************************************************************\n");
                    lsp.getEngine().getProcessModel().getEffectiveErrors().forEach(p->
                        {outputArea.append("Error " + ++errorCount + " : " + p.getMessage()+ "\n");  }  
                    );
                }             
                outputArea.append("**************************************************************************************\n");
                outputArea.append("****************************** Statement Execution Results ********************************\n");
                outputArea.append("**************************************************************************************\n");
                lsp.getEngine().getProcessModel().getStatements().values().stream().forEachOrdered((s) -> {
                    if(s.hasExecutionInfo()){
                        if(!s.getExecutionInfo().isExecuted()){
                            outputArea.append("Statement " + s.getId() + " was NOT executed.\n");
                        } else if(s.hasResult()){
                            Variable v = s.getExecutionInfo().getVariable();
                            switch (v.getResult().getResultsetType()){
                                case Tabular:{
                                    outputArea.append("Statement " + s.getId() + " was executed. Its result is in the variable: '" + v.getName() + "' and contains " + v.getResult().getTabularData().size() + " records.\n");
                                    addTabularTab(v.getName(), v.getResult()); 
                                    break;
                                }
                                case Image: {
                                    outputArea.append("Statement " + s.getId() + " was executed.  Its result is in the variable: '" + v.getName() + "'.\n");
                                    addChartTab(v.getName(), v.getResult()); 
                                    break;
                                }
                            }
                        } else {
                            outputArea.append("Statement " + s.getId() + " was executed but returned no result.\n");
                        }                      
                    } else {
                        outputArea.append("Statement " + s.getId() + " was NOT executed.\n");
                    }
                });
            }    
        outputArea.append("**************************************************************************************\n");
        outputArea.append("******************************** Process Execution Time **********************************\n");
        outputArea.append("**************************************************************************************\n");
        outputArea.append("The execution finished in " + elapsedTime + " seconds\n");                     
        runButton.setEnabled(true);
    }
    }
    
    private void run(){
        runButton.setEnabled(false);
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
