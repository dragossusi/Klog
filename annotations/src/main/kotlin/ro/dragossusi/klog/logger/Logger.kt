package ro.dragossusi.klog.logger

import ro.dragossusi.klog.writer.LogWriter

interface Logger<T> {

    fun log(logWriter: LogWriter,item:T)

}