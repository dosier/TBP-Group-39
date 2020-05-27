repositories {
    maven(url = "https://jetbrains.bintray.com/lets-plot-maven")
}
dependencies {
    implementation("org.jetbrains.lets-plot:lets-plot-common:1.4.1")
    implementation("org.jetbrains.lets-plot:lets-plot-jfx:1.4.1")
    api("org.jetbrains.lets-plot:lets-plot-kotlin-api:0.0.10-SNAPSHOT")
}