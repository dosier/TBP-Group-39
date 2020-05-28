package rug.tbp.view

import rug.tbp.model.Simulation
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
 */
class MainView(context: Simulation)
    : JFrame("TBP") {

    private val timer = Timer(2, ActionListener {
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