package rug.tbp.simulation.util

import rug.tbp.simulation.model.Vector
import rug.tbp.simulation.readVector
import java.io.File
import java.math.BigDecimal
import java.nio.file.Paths
import java.util.concurrent.atomic.AtomicInteger
import kotlin.math.abs
import kotlin.math.round

var total = 0
val atomicCounter = AtomicInteger(0)
val totalLinesReduced = AtomicInteger(0)

val collidedFiles = HashSet<String>()
val stuckFiles = HashSet<String>()

/**
 * Points to the directory that contains the files that are 'cleaned' up,
 * that is to say, formatted in a way that is faster to process.
 * This is supposed to be an intermediary step and data in this format
 * should not be used directly to train the model.
 **/
val cleanedDataDir: File = Paths.get("cleaned_data").toFile()
/**
 * Contains files that are 'cleaned' up, that is to say, formatted in a way that is
 * faster to process. This is supposed to be an intermediary step and data in this format
 * should not be used directly to train the model.
 **/
val readyDataDir: File  = Paths.get("ready_data").toFile()

val rawPath = Paths.get("/Users/stanvanderbend/Documents/MATLAB/Three body problem/data/")
val trainFile = Paths.get("/Users/stanvanderbend/PycharmProjects/TBP-Group-39/train_data.csv").toFile()
val testFile = Paths.get("/Users/stanvanderbend/PycharmProjects/TBP-Group-39/test_data.csv").toFile()

const val tFinal = 3.9
const val maxAmountStuck = 4
const val maxFiles = 10_000
const val trainTestRatio = 0.8

fun main() {
//    syncDataToConstantTimeStep()
//    detectAndCacheCollisions()
//    prepareDataForModel()
    saveDataToModelPath()
}

private fun saveDataToModelPath(){

    val files = readyDataDir.readAllCSVFiles()

    atomicCounter.set(0)
    total = files.size

    val totalTrain = (total * 0.8).toInt()
    val totalTest = total-totalTrain

    trainFile.writeText("x1,y1,vx1,vy1,x2,y2,vx2,vy2,x3,y3,vx3,vy3\n")
    testFile.writeText("x1,y1,vx1,vy1,x2,y2,vx2,vy2,x3,y3,vx3,vy3\n")

    for((i, file) in files.withIndex()){

        val text =  file.readText().substringAfter('\n')

        val outFile = if(i < totalTrain)
            trainFile
        else
            testFile

        outFile.appendText(text)

        if(i < total-1)
            outFile.appendText("\n")

        val count = atomicCounter.incrementAndGet()

        if(count % 1000 == 0)
            println("Finished copying over $count/$total data sets!")
    }

    println("Total train data = $totalTrain, total test data = $totalTest")
}

private fun syncDataToConstantTimeStep() {
    val files = rawPath.toFile()
        .listFiles()!!
        .filter { it.extension == "txt" }

    atomicCounter.set(0)
    total = files.size

    files.stream()
        .parallel()
        .forEach { syncDataInFileToConstantTimeStep(it) }

    println("Finished cleaning all the data sets, reduced a total of ${totalLinesReduced.get()} lines.")
}
private fun syncDataInFileToConstantTimeStep(input: File){

    val lines = input.readLines()

    val preCleanLineCount = lines.size

    val timeMap = HashMap<Double, String>()

    for(line in lines){
        val bloop = line.split(',')[0]

        val t = bloop.toDouble()

        timeMap[t] = line.substringAfter(bloop)
    }

    val output = Paths.get("cleaned_data", input.nameWithoutExtension+"_cleaned.csv").toFile()
    val writer = output.printWriter()
    output.createNewFile()

    val dt = 0.01
    val tEnd = tFinal
    var nextT = 0.0

    // iterates 1001 times
    while (nextT < tEnd){

        val closestT = timeMap.keys.minBy { originalT -> abs(originalT-nextT) }!!
        val line = "${nextT.round(3)}"+timeMap[closestT]

        nextT += dt

        if(nextT >= tEnd)
            writer.print(line)
        else
            writer.println(line)
    }

    writer.flush()
    writer.close()

    totalLinesReduced.addAndGet(preCleanLineCount-1001)

    val count = atomicCounter.incrementAndGet()

    if(count % 10 == 0)
        println("Finished cleaning $count/$total data sets!")
}
private fun detectAndCacheCollisions(){
    val files = cleanedDataDir.readAllCSVFiles()

    atomicCounter.set(0)
    total = files.size

    files.stream().parallel().forEach {
        val lines = it.readLines()

        var prev1 : Vector? = null
        var prev2 : Vector? = null
        var prev3 : Vector? = null
        var stuckCount = 0

        for(line in lines) {
            val split = line.split(",")
            val body1Pos = split.readVector(1)
            val body2Pos = split.readVector(3)
            val body3Pos = split.readVector(5)
            if(body1Pos == body2Pos || body1Pos == body3Pos || body2Pos == body3Pos){
                synchronized(collidedFiles) {
                    collidedFiles.add(it.name)
                }
            }
            val checkStuck = prev1 != null
            if(checkStuck){
                if(prev1 == body1Pos && prev2 == body2Pos && prev3 == body3Pos)
                    stuckCount++
                else
                    stuckCount = 0
                if(stuckCount >= maxAmountStuck){
                    synchronized(stuckFiles){
                        stuckFiles.add(it.name)
                    }
                    break
                }
            }
            prev1 = body1Pos
            prev2 = body2Pos
            prev3 = body3Pos
        }

        val count = atomicCounter.incrementAndGet()
        if(count % 1000 == 0)
            println("Finished parsing $count/$total data sets!")
    }

    println("Counted ${collidedFiles.size} files with a collision")

    println("Counted ${collidedFiles.size} files that got stuck for >= $maxAmountStuck time steps")

    for(file in stuckFiles)
        println(file)
}
private fun prepareDataForModel() {
    val files = cleanedDataDir
        .readAllCSVFiles()
        .filter {
            !collidedFiles.contains(it.name) && !stuckFiles.contains(it.name)
        }

    atomicCounter.set(0)
    total = files.size.coerceAtMost(maxFiles)

    files.stream().limit(total.toLong()).parallel().forEach {

        val lines = it.readLines()

        val output = readyDataDir.toPath().resolve(it.nameWithoutExtension + ".csv").toFile()
        val writer = output.printWriter()
        output.createNewFile()

        writer.println("x1,y1,vx1,vy1,x2,y2,vx2,vy2,x3,y3,vx3,vy3")

        for ((i, line) in lines.withIndex()) {
            val omittedLine = line.substringAfter(',')
            if (i == lines.size - 1)
                writer.print(omittedLine)
            else
                writer.println(omittedLine)
        }
        writer.flush()
        writer.close()

        val count = atomicCounter.incrementAndGet()

        if (count % 1000 == 0)
            println("Finished cleaning $count/$total data sets!")
    }
}


private fun evaluateData(input: File) {

    val lines = input.readLines()

    var lastT = BigDecimal(0.0)
    var minDiffT = BigDecimal(Double.MAX_VALUE)
    var maxDiffT = BigDecimal(Double.MIN_VALUE)
    var totalDiffT = BigDecimal(0.0)

    var lastPos1 = Vector.ZERO
    var lastPos2 = Vector.ZERO
    var lastPos3 = Vector.ZERO

    var minDiffX = Double.MAX_VALUE
    var minDiffY = Double.MAX_VALUE

    var maxDiffX = Double.MIN_VALUE
    var maxDiffY = Double.MIN_VALUE
    var maxDiffXT = BigDecimal(0.0)
    var maxDiffYT = BigDecimal(0.0)

    for ((i, line) in lines.withIndex()) {

        val split = line.split(',')
        val t = BigDecimal(split[0])

        if (i == 0) {
            lastT = t
            continue
        }

        val diffT = t - lastT

        if (diffT > maxDiffT)
            maxDiffT = diffT

        if (diffT < minDiffT)
            minDiffT = diffT

        totalDiffT += diffT

        lastT = t


        val body1Pos = split.readVector(1)
        val body2Pos = split.readVector(3)
        val body3Pos = split.readVector(5)

        val diff1X = abs(body1Pos.x - lastPos1.x)
        val diff1Y = abs(body1Pos.y - lastPos1.y)
        val diff2X = abs(body2Pos.x - lastPos2.x)
        val diff2Y = abs(body2Pos.y - lastPos2.y)
        val diff3X = abs(body3Pos.x - lastPos3.x)
        val diff3Y = abs(body3Pos.y - lastPos3.y)

        val lastMaxDiffX = maxDiffX
        val lastMaxDiffY = maxDiffY

        if (diff1X > maxDiffX) maxDiffX = diff1X
        if (diff2X > maxDiffX) maxDiffX = diff2X
        if (diff3X > maxDiffX) maxDiffX = diff3X

        if (diff1Y > maxDiffY) maxDiffY = diff1Y
        if (diff2Y > maxDiffY) maxDiffY = diff2Y
        if (diff3Y > maxDiffY) maxDiffY = diff3Y

        if (diff1X < minDiffX) minDiffX = diff1X
        if (diff2X < minDiffX) minDiffX = diff2X
        if (diff3X < minDiffX) minDiffX = diff3X

        if (diff1Y < minDiffY) minDiffY = diff1Y
        if (diff2Y < minDiffY) minDiffY = diff2Y
        if (diff3Y < minDiffY) minDiffY = diff3Y

        if (lastMaxDiffX != maxDiffX)
            maxDiffXT = t
        if (lastMaxDiffY != maxDiffY)
            maxDiffYT = t

        lastPos1 = body1Pos
        lastPos2 = body2Pos
        lastPos3 = body3Pos
    }

    println("minDiffT = $minDiffT")
    println("maxDiffT = $maxDiffT")

    val avgDiffT = totalDiffT / BigDecimal(lines.size)
    println("avgDiffT = $avgDiffT")

    println("t: $maxDiffXT\tmaxDiffX = $maxDiffX")
    println("t: $maxDiffYT\tmaxDiffY = $maxDiffY")

    println("minDiffX = $minDiffX")
    println("minDiffY = $minDiffY")
}

fun Double.round(decimals: Int): Double {
    var multiplier = 1.0
    repeat(decimals) { multiplier *= 10 }
    return round(this * multiplier) / multiplier
}

fun File.readAllCSVFiles(): List<File> {
    return this.listFiles()!!
        .filter { it.extension == "csv" }
}