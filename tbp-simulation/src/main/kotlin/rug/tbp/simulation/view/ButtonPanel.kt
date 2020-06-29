package rug.tbp.simulation.view

import java.awt.Label
import javax.swing.JButton
import javax.swing.JCheckBox
import javax.swing.JPanel
import javax.swing.JSlider

class ButtonPanel : JPanel() {

    val playButton = JButton("Play")
    val pauseButton = JButton("Pause")
    val restartButton = JButton("Restart")
    val refreshSlider = JSlider(1, 100)
    val drawAxisBox = JCheckBox("Axis")

    init {
        add(restartButton)
        add(playButton)
        add(pauseButton)
        add(drawAxisBox)
        val sliderLabel = Label("Refresh Delay (ms)")
        add(sliderLabel)
        add(refreshSlider)
    }

}