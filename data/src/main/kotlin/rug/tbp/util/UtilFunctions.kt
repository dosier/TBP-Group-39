package rug.tbp.util

import kotlin.math.ln

/**
 * https://github.com/ccampo133/NBodyJS/blob/master/src/main/kotlin/me/ccampo/nbody/util/UtilFunctions.kt
 */

/**
 * Silly function that determines a visually pleasing radius as a function of mass.
 * The formula is pretty arbitrary. I got it by picking some radii I liked and
 * plotting them, and then fitting a log function to the data. Strictly for
 * visualization purposes.
 */
fun massToRadius(mass: Double): Double {
    return if (mass > 1) 1.5.coerceAtLeast((10 * ln(mass) / ln(10.0) - 14) / 3) else 1.02
}
