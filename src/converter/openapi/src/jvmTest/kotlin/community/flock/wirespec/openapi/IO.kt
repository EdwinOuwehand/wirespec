package community.flock.wirespec.openapi

object IO {
    fun readFile(file: String): String {
        val resource = IO::class.java.classLoader.getResource(file)!!
        return resource.readText()
    }
}
