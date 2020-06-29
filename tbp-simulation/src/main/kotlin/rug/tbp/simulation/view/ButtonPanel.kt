package rug.tbp.simulation.view

import javax.swing.JButton
import javax.swing.JPanel
import javax.swing.JSlider

class ButtonPanel : JPanel() {

    val playButton = JButton("Play")
    val pauseButton = JButton("Pause")
    val restartButton = JButton("Restart")
    val refreshSlider = JSlider()

    init {
        add(restartButton)
        add(playButton)
        add(pauseButton)
        refreshSlider.minimum = 1
        refreshSlider.maximum = 100
        refreshSlider.paintTicks = true
        refreshSlider.paintLabels = true
        add(refreshSlider)
    }

}