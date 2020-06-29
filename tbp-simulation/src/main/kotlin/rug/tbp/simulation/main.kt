package rug.tbp.simulation

import rug.tbp.simulation.model.Vector
import rug.tbp.simulation.view.MainView
import java.nio.file.Paths
import javax.swing.SwingUtilities

fun main() {
    SwingUtilities.invokeLater {
        MainView(path = Paths.get("cleaned_data"), preferredRefreshDelay = 10)
    }
}

fun List<String>.readVector(i: Int) = Vector(this[i].toDouble(), this[i+1].toDouble())