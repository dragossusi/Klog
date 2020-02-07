package ro.dragossusi.klog.writer

import java.io.Closeable
import java.io.File
import java.io.OutputStreamWriter

abstract class FileLogWriter() : LogWriter, Closeable {

    abstract val file: File

    var writer: OutputStreamWriter? = null

    override fun startLog() {
        writer = file.writer()
    }

    override fun write(pair: Pair<String, Any>) {
        writer!!.appendln("${pair.first} : ${pair.second}")
    }

    override fun endLog() {
        writer!!.let {
            it.flush()
            it.close()
        }
        writer = null
    }

    override fun close() {
        writer?.close()
    }
}