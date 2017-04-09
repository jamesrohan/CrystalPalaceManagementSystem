/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package crystal_palace_management_system.Controller;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import javafx.util.Callback;

/**
 * FXML Controller class
 *
 * @author Jason
 */
public class ViewReservationsController implements Initializable {
  @FXML
  private TableView ReservationView;
  @FXML
  private Button showReservations, returnToMain;
  @FXML
  private Stage stage = null;
  @FXML
  private Parent root = null;
    private ObservableList<ObservableList> data;
    
    private Connection connection; 
    private Statement statement; 
     @FXML
    private void handleShowReservations(ActionEvent event) throws IOException{
         if(event.getSource()==showReservations){
            buildData();
      }
    }
    public void buildData(){
          data = FXCollections.observableArrayList();
          try{
              Class.forName("org.sqlite.JDBC");
             connection = DriverManager.getConnection("jdbc:sqlite:CPMS.db");
              System.out.println("Opened database successfully");
              statement = connection.createStatement();
         
            //SQL FOR SELECTING ALL OF CUSTOMER
            String SQL = "SELECT * from VISIT";
            //ResultSet
            ResultSet rs = statement.executeQuery(SQL);

            /**********************************
             * TABLE COLUMN ADDED DYNAMICALLY *
             **********************************/
            for(int i=0 ; i<rs.getMetaData().getColumnCount(); i++){
                //We are using non property style for making dynamic table
                final int j = i;                
                TableColumn col = new TableColumn(rs.getMetaData().getColumnName(i+1));
                col.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ObservableList,String>,ObservableValue<String>>(){                    
                    @Override
                    public ObservableValue<String> call(TableColumn.CellDataFeatures<ObservableList, String> param){                                                                                              
                        return new SimpleStringProperty(param.getValue().get(j).toString());                        
                    }                    
                });

                ReservationView.getColumns().addAll(col); 
                System.out.println("Column ["+i+"] ");
            }

            /********************************
             * Data added to ObservableList *
             ********************************/
            while(rs.next()){
                //Iterate Row
                ObservableList<String> row = FXCollections.observableArrayList();
                for(int i=1 ; i<=rs.getMetaData().getColumnCount(); i++){
                    //Iterate Column
                    if(rs.getString(i) != null ){
                         row.add(rs.getString(i));
                    }else{
                        row.add("null");
                    }
                   
                  
                }
                System.out.println("Row [1] added "+row );
                data.add(row);

            }

            //FINALLY ADDED TO TableView
            ReservationView.setItems(data);
          }catch(Exception e){
              System.out.println("Error on Building Data");             
          }
//        return inventoryView;
      }
    @FXML
    private void handleReturnToMain(ActionEvent event) throws IOException{
         if(event.getSource()==returnToMain){
             //get reference to the button's stage         
        stage=(Stage) returnToMain.getScene().getWindow();
        //load up OTHER FXML document
        //root = FXMLLoader.load(getClass().getResource("/crystal_palace_management_system/View/Inventory.fxml"));
        FXMLLoader tester = new FXMLLoader(getClass().getResource("/crystal_palace_management_system/View/EmployeeMainScreen.fxml"));
        
         root= tester.load();
         
         }
         //create a new scene with root and set the stage
        
      Scene scene = new Scene(root);  
      stage.setScene(scene);
      stage.show();
      }
    
    
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}