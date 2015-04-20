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
    void openProject(String projectRootPath);
    void saveDocument();
    void saveAllDocuments();

    void undoChnages();

    void redoChnages();

    void runProcess();
}
