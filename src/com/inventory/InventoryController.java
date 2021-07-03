package com.inventory;


import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.net.URL;
import java.util.Random;
import java.util.ResourceBundle;


/**
 * @author Omar Imam
 */


public class InventoryController implements Initializable {

/**
 * Fields
 */

@FXML
private MenuBar         mainWindow;
@FXML
private Label           mainFormTitle;
@FXML
private Pane            partsPane;
@FXML
private ButtonBar       partsButtonBar;
@FXML
private Button          partsAddButton;
@FXML
private Button          partsModifyButton;
@FXML
private Button          partsDeleteButton;

@FXML
private TextField       partsSearchField;
@FXML
private TextField       productsSearchField;
@FXML
public  Random          randomNumbers  = new Random( );
@FXML
public  TableView<Part> partsTableView = new TableView<>( );

@FXML
public TableView<Product> productsTableView = new TableView<>( );


// Parts List
@FXML
ObservableList<Part> allParts;

// Products List
@FXML
ObservableList<Product> allProducts;


@FXML
private Pane productsPane;

@FXML
public Stage stage = new Stage( );

@FXML
private ButtonBar productsButtonBar;

@FXML
private Button productsAddButton;

@FXML
private Button productsModifyButton;

@FXML
private Button productsDeleteButton;

@FXML
private TextField filterFieldParts;

@FXML
private TextField filterFieldProducts;

@FXML
public Button mainFormExitButton;

@FXML
private Label partsErrorLabel;

@FXML
private Label productsErrorLabel;

@Override
public void initialize( URL url, ResourceBundle resourceBundle ) {
  // Initialize the columns
  TableColumn<Part, Integer> partsIdColumn    = new TableColumn<>( "id" );
  TableColumn<Part, String>  partsNameColumn  = new TableColumn<>( "Name" );
  TableColumn<Part, Double>  partsPriceColumn = new TableColumn<>( "Price" );
  TableColumn<Part, Integer> partsStockColumn = new TableColumn<>( "Stock" );
  partsTableView.getColumns( ).addAll( partsIdColumn, partsNameColumn, partsPriceColumn, partsStockColumn );
  
  TableColumn<Product, Integer> productsIdColumn    = new TableColumn<>( "id" );
  TableColumn<Product, String>  productsNameColumn  = new TableColumn<>( "Name" );
  TableColumn<Product, Double>  productsPriceColumn = new TableColumn<>( "Price" );
  TableColumn<Product, Integer> productsStockColumn = new TableColumn<>( "Stock" );
  productsTableView.getColumns( ).addAll( productsIdColumn, productsNameColumn, productsPriceColumn,
      productsStockColumn );
  
  // Define the data in an observable list
  Inventory.allParts = FXCollections.observableArrayList(
      new InHouse( 505, "Paint", 30.25, 15, 3, 2, 5024 ),
      new InHouse( 1025, "Screwdrivers", 15.00, 17, 3, 18, 6996 ),
      new Outsourced( 88, "Trash Can", 30.25, 15, 3, 2, "The Company" ),
      new Outsourced( 79, "Wrench", 15.00, 17, 3, 18, "Acme" )
  );
  
  Inventory.allProducts = FXCollections.observableArrayList(
      new Product( "Tool Set", 60.35, 10, 2, 15, 4000 ),
      new Product( "Ladder", 45.80, 8, 2, 10, 4000 ),
      new Product( "Drill Set", 60.35, 10, 2, 15, 2058 ),
      new Product( "Wood Panel Set", 40.50, 8, 2, 10, 2076 )
  );
  
  // Associate the data with the columns
  partsIdColumn.setCellValueFactory( new PropertyValueFactory<>( ( "id" ) ) );
  partsNameColumn.setCellValueFactory( new PropertyValueFactory<Part, String>( ( "Name" ) ) );
  partsStockColumn.setCellValueFactory( new PropertyValueFactory<Part, Integer>( ( "Stock" ) ) );
  partsPriceColumn.setCellValueFactory( new PropertyValueFactory<Part, Double>( ( "Price" ) ) );
  
  productsIdColumn.setCellValueFactory( new PropertyValueFactory<>( ( "id" ) ) );
  productsNameColumn.setCellValueFactory( new PropertyValueFactory<Product, String>( ( "Name" ) ) );
  productsStockColumn.setCellValueFactory( new PropertyValueFactory<Product, Integer>( ( "Stock" ) ) );
  productsPriceColumn.setCellValueFactory( new PropertyValueFactory<Product, Double>( ( "Price" ) ) );
  
  // Wrap the Observable list in a FilteredList
  FilteredList<Part> filteredData = new FilteredList<>( Inventory.allParts, t -> true );
  
  // Add the data to the table
  partsTableView.setItems( Inventory.allParts );
  productsTableView.setItems( Inventory.allProducts );
  
  // Set the filter to change whenever the filter changes
  filterFieldParts.textProperty( ).addListener( ( observable, oldValue, newValue ) -> {
    
    filteredData.setPredicate( part -> {
      
      if ( newValue == null || newValue.isEmpty( ) ) {
        return true;
      }
      
      // Compare first name and last name of every person with filter text.
      String lowerCaseFilter = newValue.toLowerCase( );
      
      if ( part.getName( ).toLowerCase( ).contains( lowerCaseFilter ) || Integer.toString( ( part.getId( ) ) ).contains( lowerCaseFilter ) ) {
        return true;
      }
      else if ( part.getName( ).toLowerCase( ).contains( lowerCaseFilter ) || Integer.toString( ( part.getId( ) ) ).contains( lowerCaseFilter ) ) {
        return true;
      }
      else if ( String.valueOf( part.getName( ) ).indexOf( lowerCaseFilter ) != -1 ) {
        return true;
      }
      else { return false; }
    } );
  } );
  
  SortedList<Part> sortedData = new SortedList<>( filteredData );
  
  sortedData.comparatorProperty( ).bind( partsTableView.comparatorProperty( ) );
  
  partsTableView.setItems( sortedData );
  
  // Wrap the Observable list in a FilteredList
  FilteredList<Product> filteredProductData = new FilteredList<>( Inventory.allProducts, t -> true );
  
  // Set the filter to change whenever the filter changes
  filterFieldProducts.textProperty( ).addListener( ( observable, oldValue, newValue ) -> {
    
    filteredProductData.setPredicate( product -> {
      
      if ( newValue == null || newValue.isEmpty( ) ) {
        return true;
      }
      
      // Compare first name and last name of every person with filter text.
      String lowerCaseFilter = newValue.toLowerCase( );
      
      if ( product.getName( ).toLowerCase( ).contains( lowerCaseFilter ) || Integer.toString( ( product.getId( ) ) ).contains( lowerCaseFilter ) ) {
        return true;
      }
      else if ( product.getName( ).toLowerCase( ).contains( lowerCaseFilter ) || Integer.toString( ( product.getId( ) ) ).contains( lowerCaseFilter ) ) {
        return true;
      }
      else if ( String.valueOf( product.getName( ) ).indexOf( lowerCaseFilter ) != -1 ) {
        return true;
      }
      else { return false; }
    } );
  } );
  
  SortedList<Product> sortedProductData = new SortedList<>( filteredProductData );
  
  sortedProductData.comparatorProperty( ).bind( productsTableView.comparatorProperty( ) );
  
  productsTableView.setItems( sortedProductData );
  
}

public int getRandomNumber( ) {
  Random randomNumbers = new Random( );
  return Math.abs( randomNumbers.nextInt( 1000 ) );
}

public void setProductData( ObservableList<Product> productsList, TableView<Product> productsTableView ) {
  productsTableView.setItems( productsList );
}

public void setData( ObservableList<Part> partsList, TableView<Part> partsTableView ) {
  partsTableView.setItems( partsList );
}

/**
 * This function opens the Add Part menu
 *
 * @param actionEvent the event that fires when the "Add" Button is clicked on the main form
 * @throws Exception if the file is not found
 */

public void partsAddButtonListener( ActionEvent actionEvent ) throws Exception {
  // Clear the error message
  partsErrorLabel.setText( "" );
  // Load the FXML file
  Parent parent = FXMLLoader.load( getClass( ).getResource( "addPart.fxml" ) );
  Stage  stage  = new Stage( );
  
  // Build the scene graph
  Scene scene1 = new Scene( parent );
  
  // Display our window, using the scene graph.
  stage.setTitle( "Add Part" );
  stage.setScene( scene1 );
  stage.show( );
}

/**
 * This function opens the Modify Part menu
 *
 * @param actionEvent the event that fires when the "Add" Button is clicked on the main form
 * @throws Exception if the file is not found
 */

public void partsModifyButtonListener( ActionEvent actionEvent ) throws Exception {
  // Clear the error message
  partsErrorLabel.setText( "" );
  
  // Get the currently selected part
  
  // Load the FXML file
  Parent parent = FXMLLoader.load( getClass( ).getResource( "modifyPart.fxml" ) );
  Stage  stage  = new Stage( );
  
  // Build the scene graph
  Scene scene = new Scene( parent );
  
  // Display our window, using the scene graph.
  stage.setTitle( "Modify Part" );
  stage.setScene( scene );
  stage.show( );
  
  // Fill in the text fields with the information about the selected part
  
}

/**
 * This function deletes the selected entry from the parts TableView
 *
 * @param actionEvent the event that fires when the "Delete" Button is clicked on the main form
 */


public void productsAddButtonListener( ActionEvent actionEvent ) throws Exception {
  // Clear the error message
  productsErrorLabel.setText( "" );
  // Load the FXML file
  Parent parent = FXMLLoader.load( getClass( ).getResource( "addProduct.fxml" ) );
  
  Stage stage = new Stage( );
  
  // Build the scene graph
  Scene scene = new Scene( parent );
  
  // Display our window, using the scene graph.
  stage.setTitle( "Add Product" );
  stage.setScene( scene );
  stage.show( );
}

public void productsModifyButtonListener( ActionEvent actionEvent ) throws Exception {
  // Clear the error message
  productsErrorLabel.setText( "" );
  
  // Load the FXML file
  Parent parent = FXMLLoader.load( getClass( ).getResource( "modifyProduct.fxml" ) );
  
  Stage stage = new Stage( );
  
  // Build the scene graph
  Scene scene = new Scene( parent );
  
  // Display our window, using the scene graph.
  stage.setTitle( "Modify Product" );
  stage.setScene( scene );
  stage.show( );
  
}


public void partsDeleteButtonListener( ActionEvent actionEvent ) throws Exception {
  // Present error message if Parts list is empty
  if ( Inventory.allParts.size( ) == 0 ) {
    partsErrorLabel.setText( "Error: Cannot Delete, the parts list is empty!" );
  }
  else {
    // Find out which part is selected in the table
    Part selectedPart = partsTableView.getSelectionModel( ).getSelectedItem( );
    // Delete the part
    Inventory.allParts.remove( selectedPart );
  }
}

public void productsDeleteButtonListener( ActionEvent actionEvent ) throws Exception {
  // Present error message if Products list is empty
  if ( Inventory.allProducts.size( ) == 0 ) {
    productsErrorLabel.setText( "Error: Cannot Delete, the products list is empty!" );
  }
  // Find out which product is selected in the table
  Product selectedProduct = productsTableView.getSelectionModel( ).getSelectedItem( );
  // Delete the product
  Inventory.allProducts.remove( selectedProduct );
}

public void exitButtonListener( ActionEvent actionEvent ) {
  // Exit the application
  Platform.exit( );
}
}