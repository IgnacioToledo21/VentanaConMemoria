package org.example;

import javafx.application.Application;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Slider;
import javafx.scene.layout.Background;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.paint.Color;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Properties;


public class App extends Application {

    //Modelo
    private DoubleProperty x = new SimpleDoubleProperty();
    private DoubleProperty y = new SimpleDoubleProperty();
    private DoubleProperty width = new SimpleDoubleProperty();
    private DoubleProperty height = new SimpleDoubleProperty();

    private IntegerProperty red = new SimpleIntegerProperty();

    @Override
    public void init() throws Exception {
        super.init();
        System.out.println("Iniciando.");

        File profileFolder = new File(System.getProperty("user.home"));
        File configFolder = new File(profileFolder,".ventanaConMemoria");
        File configFile = new File(configFolder,"config.properties");

        if (configFile.exists()) {
            //lo cargamos
            FileInputStream fis = new FileInputStream(configFile);

            Properties props = new Properties();
            props.load(fis);

            width.set(Double.parseDouble(props.getProperty("size.width")));
            height.set(Double.parseDouble(props.getProperty("size.height")));
            x.set(Double.parseDouble(props.getProperty("location.x")));
            y.set(Double.parseDouble(props.getProperty("location.y")));

        } else {

            width.set(320);
            height.set(200);
            x.set(0);
            y.set(0);
        }

    }

    VBox root = new VBox();

    Scene scene = new Scene(root, 320, 200);

    @Override
    public void start(Stage primaryStage) throws Exception {

        //Barra de deslizar
        Slider redSlider = new Slider();
        redSlider.setMin(0);
        redSlider.setMax(255);
        redSlider.setShowTickLabels(true);
        redSlider.setShowTickMarks(true);
        redSlider.setMajorTickUnit(255);
        redSlider.setMinorTickCount(5);

        VBox root = new VBox();
        root.setFillWidth(false);
        root.setAlignment(Pos.CENTER);
        root.getChildren().add(redSlider);

        Scene scene = new Scene(root, width.get(), height.get());

        primaryStage.setX(x.get());
        primaryStage.setY(y.get());
        primaryStage.setTitle("Ventana con memoria");
        primaryStage.setScene(scene);
        primaryStage.show();

        //Vinculos Bindings

        x.bind(primaryStage.xProperty());
        y.bind(primaryStage.yProperty());
        width.bind(primaryStage.widthProperty());
        height.bind(primaryStage.heightProperty());

        redSlider.valueProperty().bindBidirectional(red);

        red.addListener((o, ov, nv) -> {
          Color c = Color.rgb(nv.intValue(), 0, 0);
          root.setBackground(Background.fill(c));
        });

    }

    @Override
    public void stop() throws Exception {

        System.out.println("Cerrando.");

        File profileFolder = new File(System.getProperty("user.home"));
        File configFolder = new File(profileFolder, ".ventanaConMemoria");
        File configFile = new File(configFolder, "config.properties");

        if (!configFolder.exists()){
            configFolder.mkdir();
        }

        System.out.println("Saving config: " + configFile);

//        System.out.println("Profile :" + profileFolder);
//        System.out.println("Config Folder :" + configFolder);
//        System.out.println("Config File :" + configFile);

        FileOutputStream fos = new FileOutputStream(configFile);


        Properties props = new Properties();
        props.setProperty("size.width", "" + width.getValue());
        props.setProperty("size.height", "" + height.getValue());
        props.setProperty("location.x", "" + x.getValue());
        props.setProperty("location.y", "" + y.getValue());
        props.store(fos, "Estado de la ventana");

    }
}




