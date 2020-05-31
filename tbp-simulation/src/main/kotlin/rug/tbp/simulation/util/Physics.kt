package rug.tbp.simulation.util

import rug.tbp.simulation.model.Body
import rug.tbp.simulation.model.Vector
import kotlin.math.pow

/**
 * https://github.com/ccampo133/NBodyJS/blob/master/src/main/kotlin/me/ccampo/nbody/util/Physics.kt
 */

const val G = 1.0 // Gravitational constant
const val SOFTENING_LENGTH = 2.0

/**
 * Numerically integrate Newton's equations of motion using the "Velocity Verlet" algorithm.
 *
 * See [Wikipedia](https://en.wikipedia.org/wiki/Verlet_integration#Velocity_Verlet)
 *
 * @param x The position vector
 * @param v The velocity vector
 * @param dt The timestep
 * @param a The acceleration function
 */
fun verlet(x: Vector, v: Vector, dt: Double, a: (Vector) -> Vector): Pair<Vector, Vector> {
    val x1 = (x + (v * dt)) + (a(x) * (dt.pow(2) / 2.0))
    val v1 = v + ((a(x) + a(x1)) * (dt / 2))
    return Pair(x1, v1)
}

/**
 * Newton's Law of Gravity. Here we're only returning the acceleration vector, not the force vector, according to
 * Newton's second law (F = ma)
 *
 * Also, a "softening length" is applied to modify gravitational interactions at small scales, avoiding a singularity
 * and hence crazy accelerations.
 *
 * See: http://www.scholarpedia.org/article/N-body_simulations_(gravitational)
 */
fun gravity(pos: Vector, body: Body): Vector {

    val d = body.position - pos

    return d * (body.mass * G)/ (d.length.pow(2) + SOFTENING_LENGTH.pow(2)).pow(3 / 2.0)
}

/**
 * Using Newton's law of universal gravitation, calculate the total acceleration vector on a two-dimension position
 * vector caused by a set of massive bodies.
 *
 * See [Wikipedia](https://en.wikipedia.org/wiki/Newton%27s_law_of_universal_gravitation)
 *
 * @param x The position vector to find the acceleration at.
 * @param bodies The set of bodies to used to contribute to the overall gravitational acceleration.
 */
fun gravityAcceleration(x: Vector, bodies: Set<Body>): Vector {

    return bodies
        .map { body -> gravity(x, body) }
        .fold(Vector.ZERO) { a1, a2 -> a1 + a2 }
}