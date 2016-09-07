/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */



/**
 *
 * @author standard
 */
public interface IShell {
    void openProject(String projectRootPath, Boolean closeDocuments);
    void createProject(String path);
    
    void openDocument(final String fileToOpen);
    void createDocument();
    void saveDocument();
    void saveAllDocuments();

    void undoChnages();

    void redoChnages();

    void runAllOpenProcesses();
    /**
	 *  Indicates if get any active document .
	 * 
	 *  @return true or false.
	 */
    public boolean getActiveDocumentList();
}
