package example

import ro.dragossusi.klog.annotations.Loggable

@Loggable
class Example(
    @Loggable
    var majorVersionNumber: Int,
    @Loggable
    var minorVersionNumber: Int
)