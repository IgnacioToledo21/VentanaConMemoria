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

    // Modelo
    private DoubleProperty x = new SimpleDoubleProperty();
    private DoubleProperty y = new SimpleDoubleProperty();
    private DoubleProperty width = new SimpleDoubleProperty();
    private DoubleProperty height = new SimpleDoubleProperty();

    private IntegerProperty red = new SimpleIntegerProperty();
    private IntegerProperty blue = new SimpleIntegerProperty();
    private IntegerProperty green = new SimpleIntegerProperty();

    @Override
    public void init() throws Exception {
        super.init();
        System.out.println("Iniciando.");

        File profileFolder = new File(System.getProperty("user.home"));
        File configFolder = new File(profileFolder, ".ventanaConMemoria");
        File configFile = new File(configFolder, "config.properties");

        if (configFile.exists()) {
            // Cargar las propiedades
            FileInputStream fis = new FileInputStream(configFile);
            Properties props = new Properties();
            props.load(fis);

            width.set(Double.parseDouble(props.getProperty("size.width")));
            height.set(Double.parseDouble(props.getProperty("size.height")));
            x.set(Double.parseDouble(props.getProperty("location.x")));
            y.set(Double.parseDouble(props.getProperty("location.y")));

            // Cargar los valores del color
            red.set(Integer.parseInt(props.getProperty("color.red", "0")));   // Valor por defecto 0
            green.set(Integer.parseInt(props.getProperty("color.green", "0"))); // Valor por defecto 0
            blue.set(Integer.parseInt(props.getProperty("color.blue", "0")));  // Valor por defecto 0

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

        // Barra de deslizar ROJO
        Slider redSlider = new Slider();
        redSlider.setMin(0);
        redSlider.setMax(255);
        redSlider.setShowTickLabels(true);
        redSlider.setShowTickMarks(true);
        redSlider.setMajorTickUnit(255);
        redSlider.setMinorTickCount(5);

        // Barra de deslizar AZUL
        Slider blueslider = new Slider();
        blueslider.setMin(0);
        blueslider.setMax(255);
        blueslider.setShowTickLabels(true);
        blueslider.setShowTickMarks(true);
        blueslider.setMajorTickUnit(255);
        blueslider.setMinorTickCount(5);

        // Barra de deslizar VERDE
        Slider greenSlider = new Slider();
        greenSlider.setMin(0);
        greenSlider.setMax(255);
        greenSlider.setShowTickLabels(true);
        greenSlider.setShowTickMarks(true);
        greenSlider.setMajorTickUnit(255);
        greenSlider.setMinorTickCount(5);

        VBox root = new VBox();
        root.setFillWidth(false);
        root.setAlignment(Pos.CENTER);
        root.getChildren().addAll(redSlider, blueslider, greenSlider);

        Scene scene = new Scene(root, width.get(), height.get());

        primaryStage.setX(x.get());
        primaryStage.setY(y.get());
        primaryStage.setTitle("Ventana con memoria");
        primaryStage.setScene(scene);
        primaryStage.show();

        // Vincular las propiedades de la ventana con las propiedades del modelo
        x.bind(primaryStage.xProperty());
        y.bind(primaryStage.yProperty());
        width.bind(primaryStage.widthProperty());
        height.bind(primaryStage.heightProperty());

        // Vincular sliders con los valores de color
        redSlider.valueProperty().bindBidirectional(red);
        blueslider.valueProperty().bindBidirectional(blue);
        greenSlider.valueProperty().bindBidirectional(green);

        // Listeners para actualizar el color de fondo cuando cambien los valores de los sliders
        red.addListener((o, ov, nv) -> {
            Color r = Color.rgb(nv.intValue(), green.get(), blue.get());
            root.setBackground(Background.fill(r));
        });

        blue.addListener((o, ov, nv) -> {
            Color b = Color.rgb(red.get(), green.get(), nv.intValue());
            root.setBackground(Background.fill(b));
        });

        green.addListener((o, ov, nv) -> {
            Color g = Color.rgb(red.get(), nv.intValue(), blue.get());
            root.setBackground(Background.fill(g));
        });

        // Aplicar el color inicial basado en los valores cargados
        Color initialColor = Color.rgb(red.get(), green.get(), blue.get());
        root.setBackground(Background.fill(initialColor));
    }

    @Override
    public void stop() throws Exception {
        System.out.println("Cerrando.");

        File profileFolder = new File(System.getProperty("user.home"));
        File configFolder = new File(profileFolder, ".ventanaConMemoria");
        File configFile = new File(configFolder, "config.properties");

        if (!configFolder.exists()) {
            configFolder.mkdir();
        }

        System.out.println("Saving config: " + configFile);

        FileOutputStream fos = new FileOutputStream(configFile);

        Properties props = new Properties();
        props.setProperty("size.width", "" + width.getValue());
        props.setProperty("size.height", "" + height.getValue());
        props.setProperty("location.x", "" + x.getValue());
        props.setProperty("location.y", "" + y.getValue());

        // Guardar los valores de los colores
        props.setProperty("color.red", "" + red.getValue());
        props.setProperty("color.green", "" + green.getValue());
        props.setProperty("color.blue", "" + blue.getValue());

        props.store(fos, "Estado de la ventana y color");
    }
}
