package ro.dragossusi.klog.writer

interface LogWriter {
    fun write(pair: Pair<String,Any>)
}