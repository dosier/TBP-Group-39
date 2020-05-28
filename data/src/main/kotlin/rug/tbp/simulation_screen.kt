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
