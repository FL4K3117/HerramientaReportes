package com.savadr.HerramientaFX8;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * JavaFX App
 */
public class App extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/savadr/HerramientaFX8/Vista/MenuConexion.fxml"));
        Parent root = loader.load();
        com.savadr.HerramientaFX8.Controlador.MenuConexionController mnController = loader.getController();
        mnController.setStage(primaryStage);

        Scene sceneM = new Scene(root);
        primaryStage.setScene(sceneM);
        primaryStage.setTitle("Conexion");
        primaryStage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}