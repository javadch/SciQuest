package xqt.workbench;


import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import xqt.api.AppInfo;
import xqt.api.LanguageServicePoint;
import xqt.model.data.ResultsetType;

/**
 *
 * @author Javad Chamanara
 */
public class Driver {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        // the client must use the API only!
        // get the input process
        // tokenize and parse the input and return a parse tree
        // annotate the parse tree to a DST
        // validate the DST for dependencies, ...
        // go through the process's statements, choose and initiate proper drivers, process
        // handle the result sets per statement
        System.out.println("Client Started");        
        System.out.println(AppInfo.getFullName());
        long start = System.nanoTime();
        // ----------------------- process -------------------------//
        processInput();
        // ----------------------- process -------------------------//
        long end = System.nanoTime();
        double seconds = (double)(end - start) / 1000000000;
        System.out.println("Client finished. status: normal");        
        System.out.println("Total processing time: " + seconds + " seconds. (including printouts)");        
        //use generate.bat file in the D:\SciQL\Src\xqt.lang\lang\src\main\java\xqt\lang\grammar folder to regenerate the
        // grammar files
    }

    private static void processInput() throws FileNotFoundException, IOException {
        String inputFile = "D:\\javad\\Projects\\XQtProjects\\XQt\\xqt.test\\src\\main\\java\\xqt\\test\\scripts\\testcase1.txt";
        //if ( args.length>0 ) inputFile = args[0];
        InputStream is = new FileInputStream(inputFile);
        LanguageServicePoint lsp = new LanguageServicePoint(is);
        lsp.process();
        // The way to access the variables from the query engine's memory
        lsp.getEngine().getVariables().stream().forEach((s) -> {
            if(s.getResult().getResultsetType() == ResultsetType.Tabular){
                List<Object> list = ((List<Object>)s.getResult().getData());
                System.out.println("var: (" + s.getName() + ") contains " + list.size() + " records.\n");
            }
        });
        // The way to access the variables from the process model
        lsp.getEngine().getProcessModel().getStatements().values().stream().forEach( (s) -> {
            if(s.isExecuted()){
                if(s.hasResult() && s.getExecutionInfo().getVariable().getResult().getResultsetType() == ResultsetType.Tabular){
                    List<Object> list = ((List<Object>)s.getExecutionInfo().getVariable().getResult().getData());
                    System.out.println("var: (" + s.getExecutionInfo().getVariable().getName() + ") contains " + list.size() + " records.\n");
                }                    
            }
        });
    }
    
}
