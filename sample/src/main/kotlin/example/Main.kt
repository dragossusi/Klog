package example

import ro.dragossusi.klog.writer.ConsoleLogWriter
import ro.dragossusi.klog.writer.FileLogWriter
import java.io.File

fun main(){
    val consoleWriter = ConsoleLogWriter()
    val fileWriter = object : FileLogWriter() {
        override val file: File
            get() =  File("log.txt")
    }
    val logger = ExampleLogger()

    val example = Example(0,1)
    logger.log(logWriter = consoleWriter, item = example)
    example.majorVersionNumber = 1
    example.minorVersionNumber = 0
    logger.log(logWriter = consoleWriter, item = example)
    logger.log(logWriter = fileWriter, item = example)
}