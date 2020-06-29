package rug.tbp.nn

import org.datavec.api.records.reader.impl.csv.CSVSequenceRecordReader
import org.datavec.api.split.NumberedFileInputSplit
import org.deeplearning4j.datasets.datavec.SequenceRecordReaderDataSetIterator
import org.deeplearning4j.nn.api.OptimizationAlgorithm
import org.deeplearning4j.nn.conf.GradientNormalization
import org.deeplearning4j.nn.conf.NeuralNetConfiguration
import org.deeplearning4j.nn.conf.layers.LSTM
import org.deeplearning4j.nn.conf.layers.RnnOutputLayer
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork
import org.deeplearning4j.nn.weights.WeightInit
import org.deeplearning4j.optimize.listeners.ScoreIterationListener
import org.nd4j.evaluation.classification.Evaluation
import org.nd4j.linalg.activations.Activation
import org.nd4j.linalg.learning.config.Nesterovs
import org.nd4j.linalg.lossfunctions.LossFunctions.LossFunction
import java.nio.file.Paths

fun main() {

    val dataPath = Paths.get("cleaned_data").toFile()

    val batchSize = 128
    val numLabelClasses = 6

    // training data
    val trainRR = CSVSequenceRecordReader(0, ", ")
    trainRR.initialize(NumberedFileInputSplit(dataPath.absolutePath + "/%d.csv", 0, 449))
    val trainIter = SequenceRecordReaderDataSetIterator(trainRR, batchSize, numLabelClasses, 1)

    // testing data
    val testRR = CSVSequenceRecordReader(0, ", ")
    testRR.initialize(NumberedFileInputSplit(dataPath.absolutePath + "/%d.csv", 450, 599))
    val testIter = SequenceRecordReaderDataSetIterator(testRR, batchSize, numLabelClasses, 1)

    val conf = NeuralNetConfiguration.Builder()
        //Random number generator seed for improved repeatability. Optional.
        .seed(123)
        .optimizationAlgo(OptimizationAlgorithm.STOCHASTIC_GRADIENT_DESCENT)
        .weightInit(WeightInit.XAVIER)
        .updater(Nesterovs(0.005))
        //Not always required, but helps with this data set
        .gradientNormalization(GradientNormalization.ClipElementWiseAbsoluteValue)
        .gradientNormalizationThreshold(0.5)
        .list()
        .layer(0, LSTM.Builder()
            .activation(Activation.TANH)
            .nIn(1)
            .nOut(10)
            .build())
        .layer(1, RnnOutputLayer.Builder(LossFunction.MCXENT)
            .activation(Activation.SOFTMAX)
            .nIn(10)
            .nOut(numLabelClasses)
            .build())
        .build()

    val model = MultiLayerNetwork(conf)
    model.setListeners(ScoreIterationListener(20))

    val numEpochs = 1
    model.fit(trainIter, numEpochs)

    val evaluation = model.evaluate<Evaluation>(testIter)

    // print the basic statistics about the trained classifier
    println("Accuracy: "+evaluation.accuracy())
    println("Precision: "+evaluation.precision())
    println("Recall: "+evaluation.recall())
}