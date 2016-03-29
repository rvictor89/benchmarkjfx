package de.victorfx.benchmarkjfx.benchmarks;

import javafx.animation.AnimationTimer;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;

import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author Ramon Victor March 2016.
 */
public class AreaChartBenchmark extends Stage {

    private static final int SERIES_MAX = 10;
    private static final int VALUES_MAX_X = 10;
    private static final int VALUES_MAX_Y = 100;
    private static final int WARMUP_COUNT = 3;
    private static final boolean WARMUP_ENABLED = true;
    private static final int BENCHMARK_COUNT = 20;
    private static final boolean BENCHMARK_REPORT_ENABLED = true;
    private static final String BENCHMARK_REPORT_NAME = "AreaChart.txt";
    private static final String BENCHMARK_REPORT_NAME_FULLSCREEN = "AreaChartFullscreen.txt";

    private boolean fullscreen;
    private int frames = 0;
    private long lastFrame = 0;
    private int warmupCount = 0;
    private int benchmarkCount = 0;

    private List<Integer> benchmarkResults = new ArrayList<>();
    private List<String> benchmarkEndResults = new ArrayList<>();
    private List<XYChart.Series<Number, Number>> series = new ArrayList<>();
    private Collection<XYChart.Data<Number, Number>> dataDefault = new ArrayList<>();

    private GraphicsDevice device = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();

    public AreaChartBenchmark(Boolean fullscreen) {
        this.setTitle("AreaChart Benchmark");
        this.fullscreen = fullscreen;
        System.out.println("<- Prepare AreaChart Benchmark with Fullscreen = " + fullscreen + " ->");
        for (int i = 0; i < VALUES_MAX_X; i++) {
            dataDefault.add(new XYChart.Data<Number, Number>((i + 1), Math.random()));
        }
    }

    @Override
    public void showAndWait() {

        int width = 640;
        int height = 480;

        final Group root = new Group();
        if (fullscreen) {
            width = device.getDisplayMode().getWidth();
            height = device.getDisplayMode().getHeight();
            this.setFullScreen(true);
        }
        final AreaChart<Number, Number> areaChart = new AreaChart<>(new NumberAxis(1, VALUES_MAX_X, 1), new
                NumberAxis(0, VALUES_MAX_Y, 10));
        areaChart.setAnimated(true);
        XYChart.Series<Number, Number> series1 = new XYChart.Series<>();
        areaChart.getData().add(series1);
        series.add(series1);
        series1.getData().addAll(dataDefault);
        root.getChildren().add(areaChart);
        areaChart.setPrefHeight(height);
        areaChart.setPrefWidth(width);
        areaChart.setLegendVisible(false);
        Scene scene = new Scene(root, 640, 480);

        System.out.println("<- Start AreaChart Benchmark with Fullscreen = " + fullscreen + " ->");
        this.setScene(scene);

        new AnimationTimer() {
            @Override
            public void handle(long now) {
                ++frames;
                long currentNanoTime = System.nanoTime();
                if (currentNanoTime > lastFrame + 1_000_000_000) {
                    //changes the data every second of all series
                    for (int i = 0; i < series.size(); i++) {
                        for (XYChart.Data data : series.get(i).getData()) {
                            data.setYValue(Math.random() * 100);
                        }
                    }
                    // Warmup
                    if (WARMUP_ENABLED && warmupCount < WARMUP_COUNT) {
                        System.out.println("<- Warm-Up ->");
                        ++warmupCount;
                    } else {
                        warmupCount = WARMUP_COUNT;
                    }
                    if (warmupCount == WARMUP_COUNT) {
                        // Benchmark
                        if (benchmarkCount < BENCHMARK_COUNT) {
                            System.out.println("FPS: " + frames + "; SERIES_COUNT: " + series.size() + ";");
                            benchmarkResults.add(frames);
                            ++benchmarkCount;
                        } else {
                            benchmarkEndResults.add(getMiddleValue(benchmarkResults) + "; " + series.size() + ";\n");
                            benchmarkResults.clear();
                            if (series.size() >= SERIES_MAX) {
                                String name = fullscreen ? BENCHMARK_REPORT_NAME_FULLSCREEN : BENCHMARK_REPORT_NAME;
                                if (BENCHMARK_REPORT_ENABLED) {
                                    try {
                                        FileWriter writer = new FileWriter(new File(name));
                                        writer.append("Benchmark; AreaChart with Fullscreen = " + fullscreen + ";\n");
                                        writer.append("FPS; SERIES_COUNT;\n");
                                        for (String s : benchmarkEndResults) {
                                            writer.append(s);
                                        }
                                        writer.flush();
                                        writer.close();
                                        series.clear();
                                        this.stop();
                                        close();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            } else {
                                XYChart.Series<Number, Number> seriestmp = new XYChart.Series<>();
                                areaChart.getData().add(seriestmp);
                                series.add(seriestmp);
                                for (int i = 0; i < VALUES_MAX_X; i++) {
                                    seriestmp.getData().add(new XYChart.Data<Number, Number>((i + 1), Math.random()));
                                }
                            }
                            benchmarkCount = 0;
                            warmupCount = 0;
                        }
                    }
                    frames = 0;
                    lastFrame = currentNanoTime;
                }
            }
        }.start();

        super.showAndWait();
    }

    private double getMiddleValue(List<Integer> listOfValues) {
        int summary = 0;
        for (int i = 0; i < listOfValues.size(); i++) {
            summary += listOfValues.get(i);
        }
        return summary / listOfValues.size();
    }
}
