package xqt.workbench;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

import xqt.api.LanguageServiceTask;
import xqt.api.ProcessExecutionListener;
import xqt.model.data.Variable;

public class ProcessExecutionHelper implements ProcessExecutionListener {
	
	private LanguageServiceTask task = null;
	
    public void execute(String[] processFiles){
    	//processFiles contains a list of process files to be executed in a sequence
    	// for each process file a separate engine is instantiated. The engine instanced are instantiated and invoked sequentially.
    	for(String processFile: processFiles){
            // ----------------------- process -------------------------//
        	// preparing for the execution is happening in the ProcessExecutionListener.executing method bellow
    		//File f = new File(processFile);
            task = new LanguageServiceTask(new File(processFile), this);
            task.executeSync();
            // ----------------------- process -------------------------//    		
    	}        
        // there is no guarantee that the codes after the task.execute are run after it, because the task is executed in a separate thread.
        // if concerned, put the code in the executed() function
        
        //use generate.bat file in the D:\SciQL\Src\xqt.lang\lang\src\main\java\xqt\lang\grammar folder to regenerate the
        // grammar files
    }

    @Override
    public String getProcessPath() {
    	return "";
    }
    
	@Override
	public void executing() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void executed(StringBuilder report) {
		// write the report to a log file with a similar name to the original process
		// create a file name with the process name + date having .log extension
		// create a file in the log folder
		// write the report in the log file
		String processFileName = task.getProcessFilePath();
		File processFile = new File(processFileName);
		String logFileName = processFile.getName();
		logFileName = logFileName + "_" + new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss'.log'").format(new Date());// + ".log";
		try(  PrintWriter out = new PrintWriter( "logs/" + logFileName )  ){
		    out.println(report);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void present(Variable v) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void draw(Variable v) {
		// TODO Auto-generated method stub
		
	}
	
}
