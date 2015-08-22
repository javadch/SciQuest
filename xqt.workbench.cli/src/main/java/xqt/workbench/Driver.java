package xqt.workbench;


import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import xqt.api.AppInfo;
import xqt.api.LanguageServicePoint;
import xqt.model.data.ResultsetType;
import xqt.model.data.Variable;

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
        LanguageServicePoint lsp = new LanguageServicePoint(".");
        lsp.registerScript(inputFile);
        lsp.process();
            if(lsp.getEngine().getProcessModel().hasError()){
                System.out.println("The script submitted contains errors.\n");
                lsp.getEngine().getProcessModel().getEffectiveErrors().forEach(p->
                    {System.out.println("Error: " + p.getMessage()+ "\n");  }  
                );
            } else {            
                lsp.getEngine().getProcessModel().getStatements().values().stream().forEachOrdered((s) -> {
                    if(s.hasExecutionInfo()){
                        if(s.hasResult()){
                            Variable v = s.getExecutionInfo().getVariable();
                            switch (v.getResult().getResultsetType()){
                                case Tabular:{
                                    System.out.println("var: (" + v.getName() + ") contains " + v.getResult().getTabularData().size() + " records.\n");                                    
                                    break;
                                }
                            }
                        } else {
                            System.out.println("Statement " + s.getExecutionInfo().getStatement().getId() + " is executed but returned no result.\n");
                        }                      
                    }
                });
            }              
        // The way to access the variables from the query engine's memory
//        lsp.getEngine().getVariables().stream().forEach((s) -> {
//            if(s.getResult().getResultsetType() == ResultsetType.Tabular){
//                List<Object> list = ((List<Object>)s.getResult().getData());
//                System.out.println("var: (" + s.getName() + ") contains " + list.size() + " records.\n");
//            }
//        });
    }
   
    public String getInfo(){
        return "XQt Workbench version 0.2.0";
    }
}
