package rug.tbp.simulation.model

import rug.tbp.simulation.readVector
import rug.tbp.simulation.util.round
import java.io.File
import java.util.ArrayDeque

class DataSimulation(file: File, bodies: List<Body> = emptyList()) : Simulation(bodies){

    private val originalMap : ArrayDeque<Pair<Double, Array<Vector>>>
    private val timeMap = ArrayDeque<Pair<Double, Array<Vector>>>()

    init {

        if (bodies.isEmpty()){
            val b0 = Body(1.0, 0.05, Vector.ZERO)
            val b1 = Body(1.0, 0.05, Vector.ZERO)
            val b2 = Body(1.0,0.05, Vector.ZERO)
            this.bodies = listOf(b0, b1, b2)
        }

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

        originalMap = timeMap.clone()
    }

    override var totalTime = 0.0

    override var dt = 0.0

    override fun run() {

        if(timeMap.isEmpty())
            return

        val next = timeMap.pollFirst()

        dt = 0.01
        totalTime = next.first.round(3)

        var offset = 0

        bodies.forEach { body ->
            body.lastPositions.addLast(body.position)
            if(body.lastPositions.size >= 200)
                body.lastPositions.removeFirst()
            body.position = next.second[offset]
            body.velocity = next.second[offset + 1]
            offset+=2
        }
    }

    override fun restart(){
        bodies.forEach {
            it.lastPositions.clear()
        }
        timeMap.clear()
        timeMap.addAll(originalMap)
    }
}