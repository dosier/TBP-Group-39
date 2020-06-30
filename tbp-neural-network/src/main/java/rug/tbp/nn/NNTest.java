//package rug.tbp.nn;
//
//import smile.base.mlp.Layer;
//import smile.base.mlp.OutputFunction;
//
//import java.io.File;
//import java.io.IOException;
//import java.nio.file.Files;
//import java.nio.file.Path;
//import java.nio.file.Paths;
//import java.util.Arrays;
//import java.util.List;
//import java.util.stream.Stream;
//
//public class NNTest {
//
//    private final static Path PATH = Paths.get("simulation.csv");
//
//    public static void main(String[] args) {
//
//        final int neuronCount = 10;
//
//        final MLP mlp = new MLP(6, 1,
//                Layer.sigmoid(neuronCount),
//                Layer.mle(1, OutputFunction.SIGMOID));
//
//        mlp.setLearningRate(0.1);
//        mlp.setMomentum(0.1);
//
//        try {
//            final List<String> lines = Files.readAllLines(PATH);
//
//            final int length = lines.size()-1;
//            final double[][] data = new double[length][6];
//
//            for (int i = 1; i < lines.size(); i++) {
//                final int dataIndex = i-1;
//                final double[] entries = Stream.of(lines.get(i).split(",")).mapToDouble(Double::parseDouble).toArray();
//                data[dataIndex] = new double[6];
//                data[dataIndex][0] = entries[1];
//                data[dataIndex][1] = entries[2];
//                data[dataIndex][2] = entries[5];
//                data[dataIndex][3] = entries[6];
//                data[dataIndex][4] = entries[9];
//                data[dataIndex][5] = entries[10];
//                mlp.update(entries[0], data[dataIndex]);
//            }
//
//            double[] y = mlp.predict(0.2);
//
//            System.out.println(Arrays.toString(y));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//}
