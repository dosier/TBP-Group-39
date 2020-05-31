package rug.tbp.simulation

import rug.tbp.simulation.model.Body
import rug.tbp.simulation.model.Simulation
import rug.tbp.simulation.model.Vector
import rug.tbp.simulation.util.randomSemiUnitCirclePoint
import rug.tbp.simulation.view.MainView
import javax.swing.SwingUtilities


/**
 * TODO: add documentation
 *
 * @author  Stan van der Bend
 * @since   27/05/2020
 * @version 1.0
 */

fun main() {

    val initBodies = createSet(1.0, 0.2, Vector(1.0, 0.0))

    val numTrailPts = 200
    val dt = 0.01
    val simulation = Simulation(dt, initBodies, write = false, maxTrail = numTrailPts)

    SwingUtilities.invokeLater {
        val view = MainView(simulation)
        view.start()
    }
}


fun createSet(mass: Double, radius: Double, x1: Vector): Set<Body> {
    val x2 = randomSemiUnitCirclePoint()
    val x3 = -x1-x2
    return setOf(
        Body(mass, radius, x1),
        Body(mass, radius, x2),
        Body(mass, radius, x3))
}

val exampleSet = setOf(
        Body(
            1.0, 0.5,
            Vector(1.0, -1.0),
            Vector(0.0, 0.0)
        ),
        Body(
            3.0, 0.3,
            Vector(1.0, 3.0),
            Vector(0.0, 0.0)
        ),
        Body(
            4.0, 0.4,
            Vector(-2.0, -1.0),
            Vector(0.0, 0.0)
        )
    )
