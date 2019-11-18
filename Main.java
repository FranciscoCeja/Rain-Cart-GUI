package sample;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.Optional;


public class Main extends Application {

    private BorderPane borderPaneRoot;
    private TextField janTF, febTF, marTF, aprTF;
    private Button submitBtn;


    private Menu fileMenu;
    private Menu viewMenu;

    private MenuItem barChartMenuItem, lineChartMenuItem, areaChartMenuItem;

    @Override
    public void start(Stage primaryStage) throws Exception {

        // build menu
        borderPaneRoot = new BorderPane();

        MenuBar menuBar = new MenuBar();
        buildMenu(primaryStage, menuBar);

        buildMainContainer();

        borderPaneRoot.getStylesheets().add("myStyles.css");
        borderPaneRoot.setId("borderPaneRoot");

        Scene myScene = new Scene(borderPaneRoot, 300, 300);
        primaryStage.setScene(myScene);
        primaryStage.show();
    }

    void buildMainContainer() {

        GridPane gridPane = new GridPane();

        TextFieldListener listener = new TextFieldListener();

        // creates labels and adds listener
        Label janLabel = new Label("January:");
        janLabel.setId("labels");
        janTF = new TextField();
        janTF.textProperty().addListener(listener);

        Label febLabel = new Label("February:");
        febLabel.setId("labels");
        febTF = new TextField();
        febTF.textProperty().addListener(listener);

        Label marLabel = new Label("March:");
        marLabel.setId("labels");
        marTF = new TextField();
        marTF.textProperty().addListener(listener);

        Label aprLabel = new Label("April:");
        aprLabel.setId("labels");
        aprTF = new TextField();
        aprTF.textProperty().addListener(listener);

        // submit button
        submitBtn = new Button("Submit");
        submitBtn.setDisable(true);

        submitBtn.setOnAction(event -> {
            createBarChart();
            barChartMenuItem.setDisable(true);
            lineChartMenuItem.setDisable(false);
            areaChartMenuItem.setDisable(false);
        });

        // adds nodes to gridpane
        gridPane.add(janLabel, 0, 0);
        gridPane.add(febLabel, 0, 1);
        gridPane.add(marLabel, 0, 2);
        gridPane.add(aprLabel, 0, 3);
        gridPane.add(janTF, 1, 0);
        gridPane.add(febTF, 1, 1);
        gridPane.add(marTF, 1, 2);
        gridPane.add(aprTF, 1, 3);
        gridPane.add(submitBtn, 1, 4);

        gridPane.setHgap(20);
        gridPane.setVgap(20);
        gridPane.setAlignment(Pos.CENTER);

        // gridpane and submit button nodes vbox
        VBox vbox = new VBox(gridPane);
        vbox.setPadding(new Insets(20));
        vbox.setAlignment(Pos.CENTER);

        // set view to vbox
        borderPaneRoot.setCenter(vbox);
    }

    void buildMenu(Stage primaryStage, MenuBar menuBar) {

        // create menu
        fileMenu = new Menu("File");
        viewMenu = new Menu("View");

        // create File menu items
        MenuItem newFileItem = new MenuItem("New");
        MenuItem exitFileItem = new MenuItem("Close");

        // create View menu items
        barChartMenuItem = new MenuItem("Bar Chart");
        lineChartMenuItem = new MenuItem("Line Chart");
        areaChartMenuItem = new MenuItem("Area Chart");

        barChartMenuItem.setDisable(true);
        lineChartMenuItem.setDisable(true);
        areaChartMenuItem.setDisable(true);

        // add accelerators
        newFileItem.setAccelerator(KeyCombination.keyCombination("ctrl+N"));
        exitFileItem.setAccelerator(KeyCombination.keyCombination("ctrl+X"));
        barChartMenuItem.setAccelerator(KeyCombination.keyCombination("ctrl+B"));
        lineChartMenuItem.setAccelerator(KeyCombination.keyCombination("ctrl+L"));
        areaChartMenuItem.setAccelerator(KeyCombination.keyCombination("ctrl+A"));

        // event handler for new file
        newFileItem.setOnAction(event -> {

            // creates alert if selecting new file
            Alert confirmAlert =
                    new Alert(Alert.AlertType.WARNING, "You will lose all current data. Continue?");
            confirmAlert.getButtonTypes().add(ButtonType.CANCEL);

            Button cancelBtn = (Button) confirmAlert.getDialogPane().lookupButton(ButtonType.CANCEL);
            cancelBtn.setDefaultButton(true);

            Button okBtn = (Button) confirmAlert.getDialogPane().lookupButton(ButtonType.OK);
            okBtn.setDefaultButton(false);

            Optional<ButtonType> result = confirmAlert.showAndWait();

            if (result.isPresent() && result.get() == ButtonType.OK) {

                buildMainContainer();
                barChartMenuItem.setDisable(true);
                lineChartMenuItem.setDisable(true);
                areaChartMenuItem.setDisable(true);
            }
            // else do nothing
        });

        // event handler for exit file
        exitFileItem.setOnAction(event -> {
            primaryStage.close();
        });

        // event handler for bar chart menu item
        barChartMenuItem.setOnAction(event -> {
            createBarChart();
        });

        // event handler for line chart menu item
        lineChartMenuItem.setOnAction(event ->{
            createLineChart();
        });

        // event handler for area chart menu item
        areaChartMenuItem.setOnAction(event ->{
            createAreaChart();
        });

        // add menu items to menus'
        fileMenu.getItems().addAll(newFileItem, exitFileItem);
        viewMenu.getItems().addAll(barChartMenuItem, lineChartMenuItem, areaChartMenuItem);

        // add menus' to menubar
        menuBar.getMenus().addAll(fileMenu, viewMenu);

        // adds menubar to root
        borderPaneRoot.setTop(menuBar);

    }

    void createLineChart(){

        // grabs values from texfields
        int janInput, febInput, marInput, aprInput;

        janInput = Integer.parseInt(janTF.getText());
        febInput = Integer.parseInt(febTF.getText());
        marInput = Integer.parseInt(marTF.getText());
        aprInput = Integer.parseInt(aprTF.getText());

        // creates chart axis titles
        CategoryAxis hAxis = new CategoryAxis();
        hAxis.setLabel("Month");
        NumberAxis vAxis = new NumberAxis();
        vAxis.setLabel("Inches of Rain");

        // creates line chart
        LineChart<String, Number> chart = new LineChart<>(hAxis, vAxis);

        XYChart.Series<String, Number> points = new XYChart.Series<>();
        points.getData().add(new XYChart.Data<>("January", janInput));
        points.getData().add(new XYChart.Data<>("February", febInput));
        points.getData().add(new XYChart.Data<>("March", marInput));
        points.getData().add(new XYChart.Data<>("April", aprInput));

        chart.getData().add(points);
        chart.setLegendVisible(false);

        // hides line chart selection from menu
        lineChartMenuItem.setDisable(true);
        barChartMenuItem.setDisable(false);
        areaChartMenuItem.setDisable(false);

        // sets line chart as view
        borderPaneRoot.setCenter(chart);

    }

    void createBarChart() {

        // grabs values from textfields
        int janInput, febInput, marInput, aprInput;

        janInput = Integer.parseInt(janTF.getText());
        febInput = Integer.parseInt(febTF.getText());
        marInput = Integer.parseInt(marTF.getText());
        aprInput = Integer.parseInt(aprTF.getText());

        // creates chart axis titles
        CategoryAxis hAxis = new CategoryAxis();
        hAxis.setLabel("Month");
        NumberAxis vAxis = new NumberAxis();
        vAxis.setLabel("Inches of Rain");

        // creates bar chart
        BarChart<String, Number> chart = new BarChart<>(hAxis, vAxis);

        XYChart.Series<String, Number> points = new XYChart.Series<>();
        points.getData().add(new XYChart.Data<>("January", janInput));
        points.getData().add(new XYChart.Data<>("February", febInput));
        points.getData().add(new XYChart.Data<>("March", marInput));
        points.getData().add(new XYChart.Data<>("April", aprInput));

        chart.getData().add(points);
        chart.setLegendVisible(false);

        // hides bar chart selection from menu
        lineChartMenuItem.setDisable(false);
        barChartMenuItem.setDisable(true);
        areaChartMenuItem.setDisable(false);

        // sets bar chart as view
        borderPaneRoot.setCenter(chart);

    }

    void createAreaChart(){

        // grabs values from textfields
        int janInput, febInput, marInput, aprInput;

        janInput = Integer.parseInt(janTF.getText());
        febInput = Integer.parseInt(febTF.getText());
        marInput = Integer.parseInt(marTF.getText());
        aprInput = Integer.parseInt(aprTF.getText());

        // creates chart axis titles
        CategoryAxis hAxis = new CategoryAxis();
        hAxis.setLabel("Month");
        NumberAxis vAxis = new NumberAxis();
        vAxis.setLabel("Inches of Rain");

        // creats chart
        AreaChart<String, Number> chart = new AreaChart<>(hAxis, vAxis);

        XYChart.Series<String, Number> points = new XYChart.Series<>();
        points.getData().add(new XYChart.Data<>("January", janInput));
        points.getData().add(new XYChart.Data<>("February", febInput));
        points.getData().add(new XYChart.Data<>("March", marInput));
        points.getData().add(new XYChart.Data<>("April", aprInput));

        chart.getData().add(points);
        chart.setLegendVisible(false);


        // hides line chart selection from menu
        lineChartMenuItem.setDisable(false);
        barChartMenuItem.setDisable(false);
        areaChartMenuItem.setDisable(true);

        // sets area chart as view
        borderPaneRoot.setCenter(chart);


    }

    public class TextFieldListener implements ChangeListener<String> {

        public void changed(ObservableValue<? extends String> source, String oldVal, String newVal) {

            if (janTF.getText().trim().equals("") || febTF.getText().trim().equals("") ||
                    marTF.getText().trim().equals("") || aprTF.getText().trim().equals("")) {

                submitBtn.setDisable(true);
                barChartMenuItem.setDisable(true);
                lineChartMenuItem.setDisable(true);
                areaChartMenuItem.setDisable(true);

            } else {

                Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                errorAlert.setContentText("Please enter a valid non-negative number.");

                // checks if input is a number greater than zero.
                if (!isNumeric(janTF.getText()) || (Integer.parseInt(janTF.getText()) < 0)) {
                    errorAlert.show();
                    janTF.setText("");
                }
                if (!isNumeric(febTF.getText()) || (Integer.parseInt(febTF.getText()) < 0)) {
                    errorAlert.show();
                    febTF.setText("");
                }
                if (!isNumeric(marTF.getText()) || (Integer.parseInt(marTF.getText()) < 0)) {
                    errorAlert.show();
                    marTF.setText("");
                }
                if (!isNumeric(aprTF.getText()) || (Integer.parseInt(aprTF.getText()) < 0)) {
                    errorAlert.show();
                    aprTF.setText("");
                }

                if(!janTF.getText().trim().equals("") && !febTF.getText().trim().equals("") &&
                        !marTF.getText().trim().equals("") && !aprTF.getText().trim().equals("")){
                    submitBtn.setDisable(false);
                }
            }
        }
    }

    private static boolean isNumeric(String strNum) {

        // checks for a non-negative number
        return strNum.matches("\\d+(\\.\\d+)?");
    }

    public static void main(String[] args) {
        launch(args);
    }
}
