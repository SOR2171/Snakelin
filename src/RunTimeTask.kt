import java.util.TimerTask

class RunTimeTask(private val func: () -> Unit): TimerTask() {
    override fun run(): Unit = func()
}