package ro.dragossusi.klog.writer

open class ConsoleLogWriter : LogWriter {

    override fun startLog() = Unit

    override fun write(pair: Pair<String, Any>) {
        println("${pair.first} : ${pair.second}")
    }

    override fun endLog() = Unit

}