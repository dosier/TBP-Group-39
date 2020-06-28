package rug.tbp.simulation.model

import rug.tbp.simulation.readVector
import java.io.File
import java.util.ArrayDeque

class ParsedSimulation(file: File,  bodies: Set<Body> = emptySet()) : Simulation(bodies){

    val timeMap = ArrayDeque<Pair<Double, Array<Vector>>>()

    init {
        val lines = file.readLines()

        for(line in lines){
            val split = line.split(',')
            val t = split[0].toDouble()
            val ys = arrayOf(
                split.readVector(1),
                split.readVector(3),
                split.readVector(5),
                split.readVector(7),
                split.readVector(9),
                split.readVector(11)
            )
            timeMap.addLast(Pair(t, ys))
        }
    }

    override var totalTime = 0.0

    override var dt = 0.0

    override fun run() {

        if(timeMap.isEmpty())
            return

        val next = timeMap.pollFirst()

        dt = next.first-totalTime
        totalTime += dt

        var offset = 0
        bodies.forEachIndexed { index, body ->
            body.position = next.second[offset]
            body.velocity = next.second[offset + 1]
            offset+=2
        }
    }
}