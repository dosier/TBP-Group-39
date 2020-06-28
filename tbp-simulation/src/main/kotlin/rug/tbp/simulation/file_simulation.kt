package rug.tbp.simulation

import rug.tbp.simulation.model.Body
import rug.tbp.simulation.model.ParsedSimulation
import rug.tbp.simulation.model.Vector
import rug.tbp.simulation.view.MainView
import java.io.File
import javax.swing.SwingUtilities

fun main() {

    val input = File("/Users/stanvanderbend/Documents/MATLAB/Three body problem/data/output_9.txt")

    val b0 = Body(5.0, 0.5, Vector.ZERO)
    val b1 = Body(3.0, 0.3, Vector.ZERO)
    val b2 = Body(4.0,0.4, Vector.ZERO)

    val simulation = ParsedSimulation(input, setOf(b0, b1, b2))

    SwingUtilities.invokeLater {
        val view = MainView(simulation, preferredRefreshDelay = 5)
        view.start()
    }

}

fun List<String>.readVector(i: Int) = Vector(this[i].toDouble(), this[i+1].toDouble())