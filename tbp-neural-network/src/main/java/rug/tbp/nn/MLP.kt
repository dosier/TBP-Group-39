//package rug.tbp.nn
//
//import smile.base.mlp.*
//
///**
// * @author  Stan van der Bend
// * @since   31/05/2020
// * @version 1.0
// */
//class MLP(n: Int, p: Int, vararg builders: LayerBuilder)
//    : MultilayerPerceptron(*net(n, p, *builders)) {
//
//    fun predict(x: Double): DoubleArray {
//        propagate(doubleArrayOf(x))
//        return output.output()
//    }
//
//    fun update(x: Double, y: DoubleArray) {
//        propagate(doubleArrayOf(x))
//        target = y
//        backpropagate(doubleArrayOf(x))
//        update()
//    }
//
//    companion object {
//
//        /**
//         * Create an array of [Layer]s.
//         *
//         * @param n         the number of neurons in the [OutputLayer]
//         * @param p         the number of input variables
//         * @param builders  an array of [LayerBuilder]s
//         */
//        private fun net(n: Int, p: Int, vararg builders: LayerBuilder): Array<Layer> {
//
//            var inputVariablesCount = p
//            val l = builders.size
//
//            return Array(l + 1) {
//                if (it == l)
//                    OutputLayer(n, inputVariablesCount, OutputFunction.LINEAR, Cost.MEAN_SQUARED_ERROR)
//                else {
//                    val builder = builders[it]
//                    val layer = builder.build(inputVariablesCount)
//                    inputVariablesCount = builder.neurons()
//                    layer
//                }
//            }
//        }
//    }
//}