package Code.Database;

import Code.Main.Main;
import com.mysql.cj.protocol.Resultset;

import javax.xml.transform.Result;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class Connection {
    /*



    Table documentproperty.type:
        FO
    -requirements
    -useCaseBeschrijving
    -requirements
    -designKeuzes

        TO


        Testontwerp
    -testCase


    Table documentfile.type:
        FO
    activityDiagram
    wireframe
    useCaseDiagram
    domeinModel

        TO
    architectuurOverzicht
    klassenDiagram

    */

    public static ArrayList<String> projectNames = new ArrayList<>();
    public static ArrayList<Integer> projectIDs = new ArrayList<>();
    public static ArrayList<String> projectDocumentNames = new ArrayList<>();
    public static ArrayList<Integer> projectDocumentIDs = new ArrayList<>();
    public static ArrayList<String> projectDocumentTypes = new ArrayList<>();
    private String url = "jdbc:mysql://localhost/designs";
    String username = "app", password = "admin";

    public static void main(String[] args) {
        Connection con = new Connection();
        con.addProject("Game");
    }

    public Connection() {
        getProjects();
    }

    public void getProjects() {
        ArrayList<String> names = new ArrayList<>();
        ArrayList<Integer> projectIDs = new ArrayList<>();


        try {

            ResultSet rs = select("SELECT name, projectID FROM projects ORDER BY name ASC");

            while (rs.next()) {
                names.add(rs.getString(1));
                projectIDs.add(rs.getInt(2));
            }

            projectNames = names;
            this.projectIDs = projectIDs;
            rs.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void removeProject(int id) {


        update("DELETE FROM projects WHERE projects.projectID = \'" + id + "\'");
        update("DELETE FROM documents WHERE projects.projectID = '" + id + "'");

        getProjects();
    }

    public void addProject(String name1) {

        ArrayList<String> projectNameList = new ArrayList<>();
        ArrayList<Integer> projectIDList = new ArrayList<>();


        try {
            int size = 0;

            ResultSet rs = select("SELECT projectID FROM projects ORDER BY projectID DESC");
            if (rs.next()) {
                size = rs.getInt(1);
            }

            rs = select("SELECT name, projectID FROM projects");
            while (rs.next()) {
                projectNameList.add(rs.getString(1));
                projectIDList.add(rs.getInt(2));
                if (rs.getString(1).equals(name1)) {
                    return;
                }
            }

            update("INSERT INTO projects (name, projectID) VALUES(\"" + name1 + "\", " + (size + 1) + ")");
            rs.close();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public void changeProjectName(String desiredName, String prevName) {
        int ID = -5;


        try {

            ResultSet rs = select("SELECT projectID, name FROM projects");

            while (rs.next()) {
                if (prevName.equals(rs.getString(2))) {
                    ID = rs.getInt(1);
                    System.out.println(rs.getString(1));
                    break;
                }
                System.out.println(rs.getString(2));
                System.out.println(prevName);
            }

            if (ID == -5) {
                return;
            }
            update("UPDATE projects SET name = \'" + desiredName + "\' WHERE projectID = " + ID);
            rs.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String[] getDocuments(String projectName) {

        String[] list = new String[0];
        int size = 0;

        projectDocumentNames = new ArrayList<>();
        projectDocumentIDs = new ArrayList<>();
        projectDocumentTypes = new ArrayList<>();


        try {

            int projectNameIndex = projectNames.indexOf(projectName);

            int projectID1 = projectIDs.get(projectNameIndex);
            ResultSet rs = select("SELECT count(documentID) FROM documents WHERE projectID = \'" + projectID1 + "\'");
            while (rs.next()) {
                size = rs.getInt(1);
            }
            list = new String[size];
            rs = select("SELECT name, documentID, type FROM documents WHERE projectID = \'" + projectID1 + "\'");
            int index = 0;
            while (rs.next()) {
                String type = (rs.getInt(3) == 1) ? "FO" : (rs.getInt(3) == 2) ? "TO" : "Testontwerp";
                projectDocumentNames.add(rs.getString(1));
                projectDocumentIDs.add(rs.getInt(2));
                projectDocumentTypes.add(type);
                list[index] = rs.getString(1) + " - " + type;
                index++;
            }
            rs.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public void addDocument(String name1, int type1) {
        projectDocumentNames = new ArrayList<>();
        projectDocumentIDs = new ArrayList<>();


        try {
            int size = 0;
            ResultSet rs = select("SELECT documentID FROM documents ORDER BY documentID DESC");
            if (rs.next()) {
                 size = rs.getInt(1);
            }
            rs = select("SELECT name, documentID FROM documents");

            while (rs.next()) {
                projectDocumentNames.add(rs.getString(1));
                projectDocumentIDs.add(rs.getInt(2));
                if (rs.getString(1).equals(name1)) {
                    return;
                }
            }

            update("INSERT INTO documents (name, projectID, documentID, type) VALUES(\'" + name1 + "\', " + Main.currentProjectID + "," + (size + 1) + ", " + type1 + ")");
            rs.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ResultSet select(String sql) {
        ResultSet rs;
        java.sql.Connection connection;
        Statement statement;
        try {
            connection = DriverManager.getConnection(url, username, password);
            statement = connection.createStatement();
            statement.execute("USE designs");
            rs = statement.executeQuery(sql);
            return rs;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean update(String query) {
        java.sql.Connection connection;
        Statement statement;
        try {
            connection = DriverManager.getConnection(url, username, password);
            statement = connection.createStatement();
            statement.execute("USE designs");
            statement.executeUpdate(query);
            connection.close();
            return true;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public void removeDocument(int documentID) {
        update("DELETE FROM documents WHERE documentID = " + documentID);
    }

    public double getDocVersion(int documentID) {
        double version = 1.0;
        try{
            ResultSet rs = select("SELECT version FROM documents WHERE documentID = " + documentID);
            rs.next();
            version = rs.getDouble(1);
            rs.close();
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return version;
    }

    public String getProjectGroup(){
        int projectID = Main.currentProjectID;
        String projectGroup = "";
        try{
            ResultSet rs = select("SELECT projectGroup FROM projects WHERE projectID = " + projectID);
            rs.next();
            projectGroup = rs.getString(1);
            rs.close();
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return projectGroup;
    }

    public String getProjectGroupMembers(int documentID){
        String projectGroup = "";
        try{
            ResultSet rs = select("SELECT authors FROM documents WHERE documentID = " + documentID);
            rs.next();
            projectGroup = rs.getString(1);
            rs.close();
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return projectGroup;
    }

    public boolean addFile(String path, int documentID, String type){
        int size = 1;
        java.sql.Connection connection;
        PreparedStatement statement;
        try{
            ResultSet rs = select("SELECT fileID FROM documentfile ORDER BY fileID DESC");
            if (rs.next()){
                size = rs.getInt(1);
            }
            System.out.println(path);

            rs = select("SELECT type FROM documentfile WHERE documentID = " + documentID);
            while (rs.next()){
                if (rs.getString(1).equals(type)){
                    return false;
                }
            }

            connection = DriverManager.getConnection(url, username, password);
            String query = "INSERT INTO documentfile VALUES(" + documentID + ",\"" + type + "\", ?," + (size+1) + ")";

            statement = connection.prepareStatement(query);
            statement.setString(1, path);
            statement.executeUpdate();
            statement.close();
            connection.close();
            return true;
        }
        catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public String getFile(int documentID, String type){
        try{
            ResultSet rs = select("SELECT type, filePath FROM documentfile WHERE documentID =" + documentID);
            while (rs.next()){
                if (rs.getString(1).equals(type)){
                    return rs.getString(2);
                }
            }
            rs.close();
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return "No file selected";
    }

    public ArrayList<String> getUseCasesNames(){
        ArrayList<String> nameList = new ArrayList<>();
        try{
            ResultSet rs = select("SELECT usecaseName FROM usecases WHERE projectID = " + Main.currentProjectID + " ORDER BY orderID ASC");
            while(rs.next()){
                nameList.add(rs.getString(1));
            }
            rs.close();
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return nameList;
    }

    public void addUseCase(String name){
        int usecaseID = 1;
        try{
            ResultSet rs = select("SELECT usecaseID FROM usecases ORDER BY usecaseID DESC");
            if (rs.next()){
                usecaseID += rs.getInt(1);
            }

            update("INSERT INTO usecases VALUES(" + usecaseID + "," + Main.currentProjectID + ", '" + name + "', " + usecaseID + ")");
            rs.close();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public void removeUseCase(int row){
        int counter = 0;

        try{
            int IDToRemove = -5;
            ResultSet rs = select("SELECT usecaseID FROM usecases WHERE projectID = " + Main.currentProjectID + " ORDER BY usecaseID ASC");
            while(rs.next()){
                if (counter == row){
                    IDToRemove = rs.getInt(1);
                    break;
                }
                counter++;
            }

            if (IDToRemove != -5){
                update("DELETE FROM usecases WHERE projectID = " + Main.currentProjectID + " AND usecaseID = " + IDToRemove);
            }
            rs.close();

        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public void moveUseCase(int row, int direction){
        ArrayList<String> names = getUseCasesNames();
        int goalID = -1;
        String currentName;
        int currentID = -1;
        String goal;

        try {
            currentName = names.get(row);
            goal = names.get(row + direction);
        }
        catch (IndexOutOfBoundsException e){
            return;
        }

        try{
            ResultSet rs = select("SELECT orderID FROM usecases WHERE usecaseName = '" + currentName + "' AND projectID = " + Main.currentProjectID);
            if (rs.next()){
                currentID = rs.getInt(1);
            }
            rs = select("SELECT orderID FROM usecases WHERE usecaseName = '" + goal + "' AND projectID = " + Main.currentProjectID);
            if (rs.next()){
                goalID = rs.getInt(1);
            }
            if (goalID != -1 && currentID != -1) {
                update("UPDATE usecases SET orderID = " + goalID + " WHERE usecaseName = '" + currentName + "'");
                update("UPDATE usecases SET orderID = " + currentID + " WHERE usecaseName = '" + goal + "'");
            }
            rs.close();
        }
        catch (Exception e){

        }
    }

    public int getUseCaseIDByName(String name){
        int usecaseID = -5;
        try{
            ResultSet rs = select("SELECT usecaseID FROM usecases WHERE usecaseName = '" + name + "'");
            if (rs.next()){
                usecaseID = rs.getInt(1);
            }
            rs.close();
            return usecaseID;
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return usecaseID;
    }

    public String getUseCaseRequirement(String name){
        int usecaseID = -5;
        String beschrijving = "";
        try{
            usecaseID = getUseCaseIDByName(name);

            ResultSet rs = select("SELECT requirement FROM usecaserequirement WHERE usecaseID = " + usecaseID);
            if (rs.next()){
                beschrijving = rs.getString(1);
            }
            rs.close();
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return beschrijving;
    }

    public ArrayList<String> getAllRequirements(){
        ArrayList<String> beschrijving = new ArrayList<>();
        try{

            ResultSet rs = select("SELECT requirement FROM usecaserequirement WHERE usecaseID IN (" +
                    "SELECT usecaseID " +
                    "FROM usecases " +
                    "WHERE projectID = " + Main.currentProjectID + ")");
            if (rs.next()){
                beschrijving.add(rs.getString(1));
            }
            rs.close();
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return beschrijving;
    }

    public void addUseCaseRequirement(String requirement, String usecaseName){
        int useCaseID = getUseCaseIDByName(usecaseName);
        int size = 0;

        try{
            ResultSet rs = select("SELECT requirementID FROM usecaserequirement ORDER BY requirementID DESC");
            if (rs.next()) {
                size = rs.getInt(1);
            }

            update("INSERT INTO usecaserequirement VALUES(" + useCaseID + ",'" + requirement + "'," + (size+1) + ")");
            rs.close();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public void updateUseCaseRequirement(String requirement, String usecaseName){
        int useCaseID = getUseCaseIDByName(usecaseName);


        try{
            ResultSet rs = select("SELECT requirementID FROM usecaserequirement WHERE usecaseID = " + useCaseID);
            if (rs.next()) {
                update("UPDATE usecaserequirement SET requirement = '" + requirement + "' WHERE usecaseID = " + useCaseID);
            }
            else {
                addUseCaseRequirement(requirement, usecaseName);
            }
            rs.close();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public void updateUseCaseCriteria(ArrayList<String> criteria, String usecaseName){
        int useCaseID = getUseCaseIDByName(usecaseName);
        int size = 0;
        try{
            update("DELETE FROM usecasecriteria WHERE useCaseID = " + useCaseID);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        for (String crit : criteria){
            try{
                ResultSet rs = select("SELECT criteriaID FROM usecasecriteria ORDER BY criteriaID DESC");
                if (rs.next()) {
                    size = rs.getInt(1);
                }
                update("INSERT INTO usecasecriteria VALUES(" + (size+1) + ", '" + crit + "', " + useCaseID + ")");
                rs.close();
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    public ArrayList<String> getUseCaseCriteria(String name){
        int useCaseID = getUseCaseIDByName(name);
        ArrayList<String> criteriaList = new ArrayList<>();

        try{
            ResultSet rs = select("SELECT criteria FROM usecasecriteria WHERE usecaseID = " + useCaseID);
            while (rs.next()){
                criteriaList.add(rs.getString(1));
            }
            rs.close();
        }
        catch (Exception e){

        }
        return criteriaList;
    }

    public void addAbout(int nameIndex, String contents){
        int size = 0;
        int documentID = Connection.projectDocumentIDs.get(nameIndex);
        try{
            ResultSet rs = select("SELECT aboutID FROM aboutdocument ORDER BY aboutID DESC");
            if (rs.next()) {
                size = rs.getInt(1);
            }
            update("INSERT INTO aboutdocument VALUES(" + size + ", '" + contents + "', " + documentID + ")");
            rs.close();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public void updateAbout(int nameIndex, String contents){
        int size = 0;
        int documentID = Connection.projectDocumentIDs.get(nameIndex);
        ResultSet rs;
        try{
            rs = select("SELECT aboutID FROM aboutdocument WHERE documentID = " + documentID);
            if (!rs.next()){
                String[] str = new String[1];
                str[66] = "";
            }

            update("UPDATE aboutdocument SET content = '" + contents + "' WHERE documentID = " + documentID);
            rs.close();
        }
        catch (Exception e){
            addAbout(nameIndex, contents);
        }
    }

    public String getAboutDocumentByIndex(int nameIndex){
        String about = "";
        int documentID = Connection.projectDocumentIDs.get(nameIndex);
        try{
            ResultSet rs = select("SELECT content FROM aboutdocument WHERE documentID = " + documentID);
            if (rs.next()){
                about = rs.getString(1);
            }
            rs.close();
        }
        catch (Exception e){

        }
        return about;
    }
    public String getAboutDocumentByID(int documentID){
        String about = "";
        try{
            ResultSet rs = select("SELECT content FROM aboutdocument WHERE documentID = " + documentID);
            if (rs.next()){
                about = rs.getString(1);
            }
            rs.close();
        }
        catch (Exception e){

        }
        return about;
    }

    public String getBy(int nameIndex){
        int documentID = Connection.projectDocumentIDs.get(nameIndex);
        String content = "";
        try{
            ResultSet rs = select("SELECT authors FROM documents WHERE documentID = " + documentID);
            if (rs.next()){
                content = rs.getString(1);
            }
            rs.close();
        }
        catch (Exception e){
            e.printStackTrace();
        }

        return content;
    }

    public void updateBy(int nameIndex, String content){
        int documentID = Connection.projectDocumentIDs.get(nameIndex);
        try{
            update("UPDATE documents SET authors = '" + content + "' WHERE documentID = " + documentID);
        }
        catch (Exception e){

        }
    }

    public String getCasus(){
        ResultSet rs;
        String casus = "";
        try{
            rs = select("SELECT casus FROM projects WHERE projectID = " + Main.currentProjectID);
            if (rs.next()){
                casus = rs.getString(1);
            }
        }
        catch (Exception e){

        }
        return casus;
    }

    public String getMissionAndVision(){
        ResultSet rs;
        String casus = "";
        try{
            rs = select("SELECT missie FROM projects WHERE projectID = " + Main.currentProjectID);
            if (rs.next()){
                casus = rs.getString(1);
            }
        }
        catch (Exception e){

        }
        return casus;
    }

    public String getAboutProject(){
        ResultSet rs;
        String casus = "";
        try{
            rs = select("SELECT about FROM projects WHERE projectID = " + Main.currentProjectID);
            if (rs.next()){
                casus = rs.getString(1);
            }
        }
        catch (Exception e){

        }
        return casus;
    }
}
