package ro.dragossusi.klog.writer

interface LogWriter {

    fun startLog()

    fun write(pair: Pair<String,Any>)

    fun endLog()
}