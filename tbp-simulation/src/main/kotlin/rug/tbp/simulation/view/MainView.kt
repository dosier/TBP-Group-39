package rug.tbp.simulation.view

import rug.tbp.simulation.model.RealTimeSimulation
import rug.tbp.simulation.model.Simulation
import java.awt.BorderLayout
import java.awt.Color
import java.awt.Dimension
import java.awt.event.ActionListener
import javax.swing.JFrame
import javax.swing.Timer
import javax.swing.WindowConstants

/**
 * TODO: add documentation
 *
 * @author  Stan van der Bend
 * @since   28/05/2020
 * @version 1.0
 *
 * @param context               the [RealTimeSimulation]
 * @param preferredRefreshDelay the preferred delay between each consecutive draw (not the same as [RealTimeSimulation.dt])
 */
class MainView(
    context: Simulation,
    preferredRefreshDelay: Int = 10
) : JFrame("TBP") {

    private val timer = Timer(preferredRefreshDelay, ActionListener {
        context.run()
        repaint()
    }).also {
        it.isRepeats = true
        it.isCoalesce = true
    }

    init {
        size = Dimension(600, 600)
        layout = BorderLayout()
        background = Color.BLACK
        defaultCloseOperation = WindowConstants.EXIT_ON_CLOSE
        contentPane.add(SimulationPanel(context), BorderLayout.CENTER)
        isVisible = true
    }

    fun start(){
        timer.start()
    }

    fun stop(){
        timer.stop()
    }
}