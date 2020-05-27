package rug.tbp

import rug.tbp.model.Body
import rug.tbp.model.Simulation
import rug.tbp.model.Vector
import rug.tbp.util.massToRadius
import java.awt.BorderLayout
import java.awt.Color
import java.awt.Dimension
import java.awt.event.ActionListener
import javax.swing.JFrame
import javax.swing.SwingUtilities
import javax.swing.Timer
import javax.swing.WindowConstants


/**
 * TODO: add documentation
 *
 * @author  Stan van der Bend
 * @since   27/05/2020
 * @version 1.0
 */

fun main() {
    val initBodies: Set<Body> = setOf(
        Body(100000.0, massToRadius(100000.0),
            Vector(0.0, 0.0),
            Vector(0.0, 0.0)
        ),
        Body(75.0, massToRadius(75.0),
            Vector(50.0, 0.0),
            Vector(0.0, 45.0)
        ),
        Body(10000.0, massToRadius(10000.0),
            Vector(220.0, 0.0),
            Vector(0.0, 21.0)
        )
    )
    val numTrailPts: Int = 1000
    val dt = 2E-2
    val context = Simulation(dt, initBodies, nPos = numTrailPts)


    SwingUtilities.invokeLater {
        val frame = JFrame("TBP")
        frame.size = Dimension(600, 600)
        frame.layout = BorderLayout()
        frame.background = Color.BLACK
        frame.contentPane.add(context, BorderLayout.CENTER)
        frame.defaultCloseOperation = WindowConstants.EXIT_ON_CLOSE
        frame.isVisible = true
        val timer = Timer(2, ActionListener {
            context.run()
            frame.repaint()
        })
        timer.isRepeats = true;
        timer.isCoalesce = true;
        timer.start();
    }
}
