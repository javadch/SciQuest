package xqt.workbench;


import java.io.FileNotFoundException;
import java.io.IOException;
import xqt.api.LanguageServicePoint;
import xqt.model.data.Variable;

/**
 *
 * @author Javad Chamanara
 */
public class Driver{

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
    	ProcessExecutionHelper px = new ProcessExecutionHelper();
    	px.execute(args);
    }


    @SuppressWarnings("unused")
	private static void processInput() throws FileNotFoundException, IOException {
        String inputFile = "D:\\javad\\Projects\\XQtProjects\\XQt\\xqt.test\\src\\main\\java\\xqt\\test\\scripts\\testcase1.txt";
        //if ( args.length>0 ) inputFile = args[0];
        try{
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
                                default:
                                	break;
                            }
                        } else {
                            System.out.println("Statement " + s.getExecutionInfo().getStatement().getId() + " is executed but returned no result.\n");
                        }                      
                    }
                });
            }            
        } catch (Exception ex){
        	System.err.println("Program execution was interrupted. " + ex.getMessage() + "\n");
        }
        // The way to access the variables from the query engine's memory
//        lsp.getEngine().getVariables().stream().forEach((s) -> {
//            if(s.getResult().getResultsetType() == ResultsetType.Tabular){
//                List<Object> list = ((List<Object>)s.getResult().getData());
//                System.out.println("var: (" + s.getName() + ") contains " + list.size() + " records.\n");
//            }
//        });
    }
   
}
