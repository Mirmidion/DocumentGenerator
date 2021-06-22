package Code.Database;

import Code.Main.Main;
import com.mysql.cj.protocol.Resultset;

import javax.xml.transform.Result;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.*;
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
            Connection.projectIDs = projectIDs;
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




            ResultSet rs = select("SELECT name, projectID FROM projects");
            while (rs.next()) {
                projectNameList.add(rs.getString(1));
                projectIDList.add(rs.getInt(2));
                if (rs.getString(1).equals(name1)) {
                    return;
                }
            }

            update("INSERT INTO projects (name) VALUES('" + name1 + "')");
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


            ResultSet rs = select("SELECT name, documentID FROM documents");

            while (rs.next()) {
                projectDocumentNames.add(rs.getString(1));
                projectDocumentIDs.add(rs.getInt(2));
                if (rs.getString(1).equals(name1)) {
                    return;
                }
            }

            update("INSERT INTO documents (name, projectID, type) VALUES(\'" + name1 + "\', " + Main.currentProjectID + ", " + type1 + ")");
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

        java.sql.Connection connection;
        PreparedStatement statement;
        try{

            System.out.println(path);

            ResultSet rs = select("SELECT type FROM documentfile WHERE documentID = " + documentID);
            while (rs.next()){
                if (rs.getString(1).equals(type)){
                    return updateFile(path, documentID, type);
                }
            }

            connection = DriverManager.getConnection(url, username, password);
            String query = "INSERT INTO documentfile (documentID, type, filePath) VALUES(" + documentID + ",\"" + type + "\", ?)";

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

    public boolean updateFile(String path, int documentID, String type){

        java.sql.Connection connection;
        PreparedStatement statement;
        try{

            connection = DriverManager.getConnection(url, username, password);
            String query = "UPDATE documentfile SET filePath = ? WHERE documentID = " + documentID + " AND type = '" + type + "'";

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

            update("INSERT INTO usecases (projectID, usecaseName, orderID) VALUES(" + Main.currentProjectID + ", '" + name + "', " + usecaseID + ")");
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

        try{
            update("INSERT INTO usecaserequirement (usecaseID, requirement) VALUES(" + useCaseID + ",'" + requirement + "')");
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
        ResultSet rs = select("");
        try{
            rs = select("SELECT * FROM usecasecriteria WHERE useCaseID = " + useCaseID);
            update("DELETE FROM usecasecriteria WHERE useCaseID = " + useCaseID);
        }
        catch (Exception e){
            try {
                while (rs.next()) {
                    update("INSERT INTO usecasecriteria VALUES(" + rs.getInt(1) + ",'" + rs.getString(2) + "'," + rs.getInt(3) + ")");
                }
            }
            catch(Exception ex){
                    ex.printStackTrace();
            }
            e.printStackTrace();
        }
        for (String crit : criteria){
            try{
                rs = select("SELECT criteriaID FROM usecasecriteria ORDER BY criteriaID DESC");
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
        int documentID = Connection.projectDocumentIDs.get(nameIndex);
        try{

            update("INSERT INTO aboutdocument (content, documentID) VALUES('" + contents + "', " + documentID + ")");

        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public void updateActors(int nameIndex, ArrayList<String> actors, ArrayList<Boolean> isPrimary){
        int documentID = Connection.projectDocumentIDs.get(nameIndex);
        update("DELETE FROM actors WHERE documentID = " + documentID);
        for (int i = 0; i < actors.size(); i++) {
            update("INSERT INTO actors (actor, documentID, isprimary) VALUES('" + actors.get(i) + "'," + documentID + "," + isPrimary.get(i) + ")");
        }
    }

    public ArrayList<String[]> getActors(int nameIndex){
        int documentID = Connection.projectDocumentIDs.get(nameIndex);
        ArrayList<String[]> actors = new ArrayList<>();
        try{
            ResultSet rs = select("SELECT actor, isprimary FROM actors WHERE documentID = " + documentID);
            while(rs.next()){
                String[] actor = new String[2];
                actor[0] = rs.getString(1);
                boolean primary = rs.getBoolean(2);
                if (primary){
                    actor[1] = "true";
                }
                else{
                    actor[1] = "false";
                }
                actors.add(actor);
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return actors;
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

    public void addActorsUsedInUseCase(ArrayList<String> actors, String usecaseName, int nameIndex){
        int usecaseID = getUseCaseIDByName(usecaseName);
        int documentID = Connection.projectDocumentIDs.get(nameIndex);
        try{
            ResultSet rs;
            update("DELETE FROM usecaseactors WHERE usecaseID = " + usecaseID);
            ArrayList<Integer> actorIDs = new ArrayList<>();
            for (String actor : actors) {
                rs = select("SELECT actorID FROM actors WHERE actor = '" + actor + "' AND documentID = " + documentID);
                if (rs.next()) {
                    actorIDs.add(rs.getInt(1));
                }
            }
            for (Integer id : actorIDs){
                update("INSERT INTO usecaseactors VALUES(" + id + "," + usecaseID + ")");
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public ArrayList<String[]> actorUsedInUseCase(String usecaseName, int nameIndex){
        int usecaseID = getUseCaseIDByName(usecaseName);
        int documentID = Connection.projectDocumentIDs.get(nameIndex);
        ArrayList<String[]> addedList = new ArrayList<>();
        try{
            ResultSet rs = select("SELECT actorID FROM usecaseactors WHERE usecaseID = " + usecaseID);
            ArrayList<Integer> actorIDsAdded = new ArrayList<>();
            while (rs.next()){
                actorIDsAdded.add(rs.getInt(1));
            }

            rs = select("SELECT actorID, actor FROM actors WHERE documentID = " + documentID);

            while (rs.next()){
                String[] actorAdded = new String[2];
                actorAdded[0] = rs.getString(2);
                if (actorIDsAdded.contains(rs.getInt(1))){
                    actorAdded[1] = "true";
                }
                else{
                    actorAdded[1] = "false";
                }
                addedList.add(actorAdded);
            }
            rs.close();
        }
        catch (Exception e){
            e.printStackTrace();
        }

        return addedList;
    }

    public ArrayList<String> getBeschrijvingScenario(String usecaseName){
        int usecaseID = getUseCaseIDByName(usecaseName);
        ArrayList<String> scenario = new ArrayList<>();
        try{
            ResultSet rs = select("SELECT row FROM beschrijvingscenario WHERE usecaseID = " + usecaseID + " ORDER BY orderID ASC");
            while (rs.next()){
                scenario.add(rs.getString(1));
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return scenario;
    }

    public void addScenarioRow(String usecaseName){
        int usecaseID = getUseCaseIDByName(usecaseName);
        update("INSERT INTO beschrijvingscenario(usecaseID, row) VALUES(" + usecaseID + ", 'Edit This' )");
        try{
            ResultSet rs = select("SELECT scenarioID FROM beschrijvingscenario ORDER BY scenarioID DESC");
            if (rs.next()){
                update("UPDATE beschrijvingscenario SET orderID = " + rs.getInt(1) + " WHERE scenarioID = " + rs.getInt(1));
                update("UPDATE beschrijvingscenario SET row = 'Edit This" + rs.getInt(1) + "' WHERE scenarioID = " + rs.getInt(1));
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public void moveScenarioRow(int row, int direction, String usecaseName){
        ArrayList<String> names = getBeschrijvingScenario(usecaseName);
        int usecaseID = getUseCaseIDByName(usecaseName);
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
            ResultSet rs = select("SELECT orderID FROM beschrijvingscenario WHERE row = '" + currentName + "' AND usecaseID = " + usecaseID);
            if (rs.next()){
                currentID = rs.getInt(1);
            }
            rs = select("SELECT orderID FROM beschrijvingscenario WHERE row = '" + goal + "' AND usecaseID = " + usecaseID);
            if (rs.next()){
                goalID = rs.getInt(1);
            }
            if (goalID != -1 && currentID != -1) {
                update("UPDATE beschrijvingscenario SET orderID = " + goalID + " WHERE row = '" + currentName + "'");
                update("UPDATE beschrijvingscenario SET orderID = " + currentID + " WHERE row = '" + goal + "'");
            }
            rs.close();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public void updateScenarioRows(ArrayList<String> rows, int usecaseID){
        for (String row : rows){
            for (String secondRow : rows){
                if (row.equals(secondRow) && rows.indexOf(secondRow) != rows.indexOf(row)){
                    return;
                }
            }
        }
        int counter = 0;
        try{
            ResultSet rowIDs = select("SELECT scenarioID FROM beschrijvingscenario WHERE usecaseID = " + usecaseID + " ORDER BY orderID ASC");
            while(rowIDs.next()){
                update("UPDATE beschrijvingscenario SET row = '" + rows.get(counter) + "' WHERE scenarioID = " + rowIDs.getInt(1));
                counter++;
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    public String[] getBeschrijving(String usecaseName){
        int usecaseID = getUseCaseIDByName(usecaseName);
        String[] beschrijving = new String[3];
        try{
            ResultSet rs = select("SELECT precondition, postcondition, uitzondering from usecasebeschrijving WHERE usecaseID = " + usecaseID);
            if(rs.next()){
                beschrijving[0] = rs.getString(1);
                beschrijving[1] = rs.getString(2);
                beschrijving[2] = rs.getString(3);
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }

        return beschrijving;
    }

    private void addBeschrijving(String usecaseName, String[] beschrijving){
        int usecaseID = getUseCaseIDByName(usecaseName);
        try{
            update("INSERT INTO usecasebeschrijving (usecaseID,precondition,postcondition,uitzondering) VALUES(" + usecaseID + ",'" + beschrijving[0] + "','" + beschrijving[1] + "','" + beschrijving[2] + "')");
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public void updateBeschrijving(String usecaseName, String[] beschrijving){
        int usecaseID = getUseCaseIDByName(usecaseName);
        try{
            ResultSet rs = select("SELECT precondition FROM usecasebeschrijving WHERE usecaseID = " + usecaseID);
            if (!rs.next()){
                addBeschrijving(usecaseName, beschrijving);
            }
            update("UPDATE usecasebeschrijving SET precondition = '" + beschrijving[0] + "', postcondition = '" + beschrijving[1] + "', uitzondering = '" + beschrijving[2] + "' WHERE usecaseID = " + usecaseID);
        }
        catch (Exception e){
            addBeschrijving(usecaseName, beschrijving);
        }
    }
}
