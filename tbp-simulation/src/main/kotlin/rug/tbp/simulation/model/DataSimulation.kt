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

        var t = 0.0

        for(line in lines){

            if(line.contains("x"))
                continue

            val split = line.split(',')

            val offset: Int
            if(split.size == 13) {
                t = split[0].toDouble()
                offset = 1
            }  else
                offset = 0

            val ys = arrayOf(
                split.readVector(offset),
                split.readVector(offset+2),
                split.readVector(offset+4),
                split.readVector(offset+6),
                split.readVector(offset+8),
                split.readVector(offset+10)
            )

            timeMap.addLast(Pair(t, ys))
            t += 0.01
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