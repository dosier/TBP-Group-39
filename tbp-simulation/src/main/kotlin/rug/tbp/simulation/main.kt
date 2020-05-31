package rug.tbp.simulation

import rug.tbp.simulation.model.Body
import rug.tbp.simulation.model.Simulation
import rug.tbp.simulation.model.Vector
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
    val initBodies: Set<Body> = setOf(
        Body(5.0,0.5,
            Vector(1.0, -1.0),
            Vector(0.0, 0.0)
        ),
        Body(3.0, 0.3,
            Vector(1.0, 3.0),
            Vector(0.0, 0.0)
        ),
        Body(4.0, 0.4,
            Vector(-2.0, -1.0),
            Vector(0.0, 0.0)
        )
    )
    val numTrailPts = 300
    val dt = 0.05
    val simulation = Simulation(dt, initBodies, maxTrail = numTrailPts)

    SwingUtilities.invokeLater {
        val view = MainView(simulation)
        view.start()
    }
}
