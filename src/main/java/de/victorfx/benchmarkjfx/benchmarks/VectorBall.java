package de.victorfx.benchmarkjfx.benchmarks;

import de.victorfx.benchmarkjfx.entity.FXBall;
import de.victorfx.benchmarkjfx.entity.FXBallVector;
import javafx.animation.AnimationTimer;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Ramon Victor.
 *         Based on the modified version by TBEERNOT 2011 (tbeernot.wordpress.com).
 */
public class VectorBall extends Stage {

    private static final int BALLS_MIN = 500;
    private static final int BALLS_MAX = 9000;
    private static final int BALLS_INCREMENT = 500;
    private static final int WARMUP_COUNT = 10;
    private static final boolean WARMUP_ENABLED = true;
    private static final int BENCHMARK_COUNT = 20;
    private static final boolean BENCHMARK_REPORT_ENABLED = true;
    private static final String BENCHMARK_REPORT_NAME = "VectorBall.txt";
    private static final String BENCHMARK_REPORT_NAME_FULLSCREEN = "VectorBallFullscreen.txt";

    private final boolean fullscreen;
    private int frames = 0;
    private long lastFrame = 0;
    private int warmupCount = 0;
    private int benchmarkCount = 0;

    private final List<FXBall> fxBalls = new ArrayList<>();
    private final List<Integer> benchmarkResults = new ArrayList<>();
    private final List<String> benchmarkEndResults = new ArrayList<>();

    public VectorBall(Boolean fullscreen) {
        this.setTitle("VectorBall Benchmark");
        this.fullscreen = fullscreen;
        System.out.println("<- Prepare VectorBall Benchmark with Fullscreen = " + fullscreen + " ->");
    }

    @Override
    public void showAndWait() {
        final FXBallVector fxBallVector = new FXBallVector();
        Group root = new Group();
        if (fullscreen) {
            fxBallVector.getRegion().setToDeviceDimensions();
            this.setFullScreen(true);
        }
        root.getChildren().add(fxBallVector.getCircle());
        fxBalls.add(fxBallVector);
        Scene scene = new Scene(root, 640, 480);
        for (int i = 0; i < BALLS_MIN - 1; i++) {
            fxBalls.add(fxBallVector.clone());
        }
        System.out.println("<- Start VectorBall Benchmark with Fullscreen = " + fullscreen + " ->");
        this.setScene(scene);

        new AnimationTimer() {
            @Override
            public void handle(long now) {
                for (FXBall fxBall : fxBalls) {
                    fxBall.move();
                }
                ++frames;
                long currentNanoTime = System.nanoTime();
                if (currentNanoTime > lastFrame + 1_000_000_000) {
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
                            System.out.println("Balls: " + fxBalls.size() + "; FPS: " + frames + ";");
                            benchmarkResults.add(frames);
                            ++benchmarkCount;
                        } else {
                            benchmarkEndResults.add(fxBalls.size() + "; " + getMiddleValue
                                    (benchmarkResults) + ";\n");
                            benchmarkResults.clear();
                            if (fxBalls.size() >= BALLS_MAX) {
                                String name = fullscreen ? BENCHMARK_REPORT_NAME_FULLSCREEN : BENCHMARK_REPORT_NAME;
                                if (BENCHMARK_REPORT_ENABLED) {
                                    try {
                                        FileWriter writer = new FileWriter(new File(name));
                                        writer.append("Benchmark; VectorBalls with Fullscreen = ").append(String
                                                .valueOf(fullscreen)).append(";\n");
                                        writer.append("Balls; FPS;\n");
                                        for (String s : benchmarkEndResults) {
                                            writer.append(s);
                                        }
                                        writer.flush();
                                        writer.close();
                                        fxBalls.clear();
                                        this.stop();
                                        close();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            } else {
                                for (int i = 0; i < BALLS_INCREMENT; i++) {
                                    fxBalls.add(fxBallVector.clone());
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
        for (Integer listOfValue : listOfValues) {
            summary += listOfValue;
        }
        return summary / listOfValues.size();
    }

}
