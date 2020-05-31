package rug.tbp.nn

import smile.base.mlp.*

/**
 * @author  Stan van der Bend
 * @since   31/05/2020
 * @version 1.0
 */
class MLP(n: Int, p: Int, vararg builders: LayerBuilder)
    : MultilayerPerceptron(*net(n, p, *builders)) {

    fun predict(x: Double): DoubleArray {
        propagate(doubleArrayOf(x))
        return output.output()
    }

    fun update(x: Double, y: DoubleArray) {
        propagate(doubleArrayOf(x))
        target = y
        backpropagate(doubleArrayOf(x))
        update()
    }

    companion object {


        private fun net(neuronCount: Int, layerCount: Int, vararg builders: LayerBuilder): Array<Layer> {
            var p = layerCount
            val l = builders.size
            return Array(l + 1) {
                if(it == l)
                    OutputLayer(neuronCount, p, OutputFunction.LINEAR, Cost.MEAN_SQUARED_ERROR)
                else
                   builders[it].build(p).also { _ -> p = builders[it].neurons() }
            }
        }
    }
}