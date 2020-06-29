package rug.tbp.simulation

import rug.tbp.simulation.model.Vector
import rug.tbp.simulation.view.MainView
import javax.swing.SwingUtilities

fun main() {
    SwingUtilities.invokeLater {
        MainView(preferredRefreshDelay = 10)
    }
}

fun List<String>.readVector(i: Int) = Vector(this[i].toDouble(), this[i+1].toDouble())