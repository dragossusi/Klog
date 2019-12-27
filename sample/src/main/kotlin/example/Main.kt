package example

import ro.dragossusi.klog.writer.LogWriter

fun main(){
    val writer = object : LogWriter{
        override fun write(pair: Pair<String, Any>) {
            println("${pair.first} : ${pair.second}")
        }
    }
    val logger = ExampleLogger()

    val example = Example(1)
    logger.log(logWriter = writer, item = example)
    example.versionNumber = 2
    logger.log(logWriter = writer, item = example)
}