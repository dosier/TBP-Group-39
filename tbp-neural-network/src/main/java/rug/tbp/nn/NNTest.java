package rug.tbp.nn;

import smile.base.mlp.Layer;
import smile.base.mlp.OutputFunction;
import smile.classification.MLP;
import smile.math.MathEx;

public class NNTest {

    public static void main(String[] args) {

        final int neuronCount = 10;

        final MLP mlp = new MLP(2,
                Layer.sigmoid(neuronCount),
                Layer.mle(1, OutputFunction.SIGMOID));

        mlp.setLearningRate(0.1);
        mlp.setMomentum(0.1);

        double[][] x = new double[10][10];
        int[] y = new int[10];

        for(int epoch = 0; epoch < 10; epoch++){
            for(int i: MathEx.permutate(x.length)){
                mlp.update(x[i], y[i]);
            }
        }
    }
}
