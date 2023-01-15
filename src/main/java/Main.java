import javafx.application.Application;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;
import org.ejml.simple.SimpleMatrix;
import javafx.scene.Scene;

import static java.lang.Math.pow;

public class Main extends Application {
    private static int n;
    private static double h;
    private static int begin = 1;
    private static int end = 2;
    private static SimpleMatrix A;
    private static SimpleMatrix B;
    private static SimpleMatrix C;

    public static double uiPrev(double x, double h) {
        return (pow(x,2)/pow(h,2)) - x/(2*h);
    }
    public static double ui(double x, double h) {
        return 1 - 2*(pow(x,2)/pow(h,2));
    }
    public static double uiNext(double x, double h) {
        return (pow(x,2)/pow(h,2)) + x/(2*h);
    }

    public static void solve() {
        h = (double)(end-begin)/n;
        double x = begin;
        n+=1;
        A = new SimpleMatrix(n, n);
        B = new SimpleMatrix(n, 1);
        C = new SimpleMatrix(n,1);
        C.set(0,0,-1*begin + 4*(begin*begin/h));
        for (int i = 1; i<n; i++)
            C.set(i,0,begin + h*i);

        A.set(0,0, ui(x, h));
        A.set(0,1, 2*(pow(x,2)/pow(h,2)));

        x += h;
        for (int i = 1; i<n-1; i++) {
            A.set(i,i-1, uiPrev(x,h));
            A.set(i,    i,   ui(x,h));
            A.set(i,i+1, uiNext(x,h));
            x += h;
        }

        A.set(n-1, n-2,2*(pow(x,2)/pow(h,2)));
        A.set(n-1, n-1,ui(x,h));

        SimpleMatrix At = A.invert();
        B = At.mult(C);
    }

    @Override
    public void start(Stage stage) {
        final NumberAxis xAxis = new NumberAxis();
        final NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("x");
        yAxis.setLabel("u(x)");

        final LineChart<Number, Number> lineChart = new LineChart<>(xAxis, yAxis);
        XYChart.Series series = new XYChart.Series();
        series.setName("rozwiazanie przyblizone");

        double x = begin;
        for (int i = 0; i<n; i++) {
            series.getData().add(new XYChart.Data(x,B.get(i,0)));
            x += h;
        }
        lineChart.getData().addAll(series);
        stage.setTitle("metoda roznic skonczonych n = " + (n-1));
        Scene scene = new Scene(lineChart,1000,1000);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        n = 50;
        solve();
        launch(args);
        
    }
}
