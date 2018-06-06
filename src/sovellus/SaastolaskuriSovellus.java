package sovellus;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;


public class SaastolaskuriSovellus extends Application {

    @Override
    public void start(Stage ikkuna) {

        // Luodaan asettelu, slider sekä Label -oliot
        BorderPane asettelu = new BorderPane();
        BorderPane ylaOsanAsettelu = new BorderPane();
        BorderPane alaOsanAsettelu = new BorderPane();
        VBox ylaOsa = new VBox();
        Slider ylaOsanSlider = new Slider(25, 250, 0);
        ylaOsanSlider.setShowTickLabels(true);
        ylaOsanSlider.setShowTickMarks(true);
        ylaOsanSlider.setMajorTickUnit(25);
        ylaOsanSlider.setMinorTickCount(5);
        ylaOsanSlider.setBlockIncrement(10);

        Label ylaVasenTeksti = new Label("Kuukausittainen tallennus");
        Label ylaOikeaTeksti = new Label("0.0");
        Slider alaOsanSlider = new Slider(0, 10, 0);
        alaOsanSlider.setShowTickLabels(true);
        alaOsanSlider.setShowTickMarks(true);
        alaOsanSlider.setMajorTickUnit(10);
        alaOsanSlider.setMinorTickCount(9);
        alaOsanSlider.setBlockIncrement(10);
        alaOsanSlider.setSnapToTicks(true);
        Label alaVasenTeksti = new Label("Vuosittainen korko");
        Label alaOikeaTeksti = new Label("0.0");

        // Asetetaan Slider ja label -oliot asetteluihin.
        ylaOsanAsettelu.setLeft(ylaVasenTeksti);
        ylaOsanAsettelu.setCenter(ylaOsanSlider);
        ylaOsanAsettelu.setRight(ylaOikeaTeksti);
        alaOsanAsettelu.setLeft(alaVasenTeksti);
        alaOsanAsettelu.setCenter(alaOsanSlider);
        alaOsanAsettelu.setRight(alaOikeaTeksti);

        ylaOsa.getChildren().addAll(ylaOsanAsettelu, alaOsanAsettelu);
        asettelu.setTop(ylaOsa);

        // Luodaan viivakaavion akseli- ja viivakaavio oliot
        NumberAxis xAkseli = new NumberAxis(0, 30, 1);
        NumberAxis yAkseli = new NumberAxis();
        LineChart<Number, Number> viivakaavio = new LineChart<>(xAkseli, yAkseli);
        viivakaavio.setTitle("Säästölaskuri");

        // Luodaan XYChart olio, johon tallennetaan piirrettävän viivan data
        XYChart.Series tallennus = new XYChart.Series();

        // Yläosan sliderin klikkauksesta tapahtuvat tapahtumat
        ylaOsanSlider.setOnMouseReleased((event) -> {

            ylaOikeaTeksti.setText(String.valueOf((int) ylaOsanSlider.getValue()));
            tallennus.getData().clear();
            for (int i = 0; i < 31; i++) {
                if (i == 0) {
                    tallennus.getData().add(new XYChart.Data(0, 0));
                } else {
                    tallennus.getData().add(new XYChart.Data(i, ((int) ylaOsanSlider.getValue() * 12) * i));
                }
            }
        });

        XYChart.Series tallennusKorkoineen = new XYChart.Series();

        // Alaosan Sliderin klikkauksesta tapahtuvat tapahtumat
        alaOsanSlider.setOnMouseReleased((event) -> {
            alaOikeaTeksti.setText(String.valueOf((int) alaOsanSlider.getValue()));
            tallennusKorkoineen.getData().clear();
            int ilmanKorkoa = 0;
            double koronKasvattaja = 0;
            double tulosKoronKanssa = 0;

            double saastotPerVuosi = 0;

            for (int i = 0; i < 31; i++) {
                if (i == 0) {
                    tallennusKorkoineen.getData().add(new XYChart.Data(0, 0));

                } else {
                    ilmanKorkoa = ((int) ylaOsanSlider.getValue() * 12 * i);
                    koronKasvattaja  = alaOsanSlider.getValue()/100 + 1;
                    
                    saastotPerVuosi += ilmanKorkoa/i;
                    saastotPerVuosi *= koronKasvattaja;
    
                    
                    tallennusKorkoineen.getData().add(new XYChart.Data(i, saastotPerVuosi));

                }
            }

        });

        viivakaavio.getData().addAll(tallennus, tallennusKorkoineen);
        asettelu.setCenter(viivakaavio);
        Scene nakyma = new Scene(asettelu);
        ikkuna.setScene(nakyma);
        ikkuna.show();
    }

    public static void main(String[] args) {
        launch(SaastolaskuriSovellus.class);
    }
}
