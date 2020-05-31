package rug.tbp.simulation.util

import rug.tbp.simulation.model.Vector
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt
import kotlin.random.Random

fun randomSemiUnitCirclePoint(): Vector {

    val theta = Random.nextDouble(Math.PI/2, Math.PI)
    val r = sqrt(Random.nextDouble())

    return Vector(x = 0.5 * r * cos(theta), y = r * sin(theta))
}