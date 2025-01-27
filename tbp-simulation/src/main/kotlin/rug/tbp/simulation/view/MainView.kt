package rug.tbp.simulation.view

import rug.tbp.simulation.model.DataSimulation
import java.awt.BorderLayout
import java.awt.Color
import java.awt.Dimension
import java.awt.event.ActionListener
import java.nio.file.Path
import java.nio.file.Paths
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
 * @param path                  the [Path] to the data folder
 * @param preferredRefreshDelay the preferred delay between each consecutive draw (not the same as [RealTimeSimulation.dt])
 */
class MainView(path: Path, preferredRefreshDelay: Int = 10) : JFrame("TBP") {

    val simulater = SimulationPanel()

    private val timer = Timer(preferredRefreshDelay, ActionListener {
        simulater.simulation?.run()
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

        val buttonPanel = ButtonPanel()
        buttonPanel.restartButton.addActionListener {
            timer.restart()
            simulater.simulation?.restart()
        }
        buttonPanel.playButton.addActionListener {
            timer.start()
        }
        buttonPanel.pauseButton.addActionListener {
            timer.stop()
        }
        buttonPanel.drawAxisBox.addActionListener {
            simulater.drawAxis = buttonPanel.drawAxisBox.isSelected
        }
        buttonPanel.refreshSlider.value = preferredRefreshDelay
        buttonPanel.refreshSlider.addChangeListener {
            timer.delay = buttonPanel.refreshSlider.value
        }

        buttonPanel.scaleSlider.value = 1
        buttonPanel.scaleSlider.addChangeListener {
            simulater.zoom = (100*buttonPanel.scaleSlider.value).toDouble()
        }

        val explorer = FileExplorerPanel(path.toFile())
        explorer.tree.addTreeSelectionListener {

            try {

                val dirName = it.path.parentPath.lastPathComponent.toString()
                val fileName = it.path.lastPathComponent.toString()
                val file = Paths.get(dirName, fileName).toFile()
                val simulation = DataSimulation(file)

                simulater.simulation = simulation

                timer.restart()

            } catch (e: Exception){
                e.printStackTrace()
            }
        }

        contentPane.add(explorer, BorderLayout.WEST)
        contentPane.add(simulater, BorderLayout.CENTER)
        contentPane.add(buttonPanel, BorderLayout.SOUTH)

        isVisible = true
    }
}