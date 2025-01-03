import java.awt.Graphics
import java.awt.event.ActionEvent
import java.awt.event.ActionListener
import java.awt.event.KeyEvent
import java.awt.event.KeyListener
import javax.swing.*
import kotlin.system.exitProcess

class GameFrame : JFrame(), KeyListener, ActionListener {
    private var map: MapType
    private var snakeHead: Snake
    private var snakeTail: Snake
    private var foodPosition: Tile

    private val replayItem = JMenuItem("Reply")
    private val settingsItem = JMenuItem("Settings")
    private val exitItem = JMenuItem("Exit")
    private val tutorialItem = JMenuItem("Tutorial")
    private val aboutItem = JMenuItem("About us")

    init {
        initFrame()
        map = newMap()
        snakeTail = initSnake()
        snakeHead = snakeTail.toward!!
        foodPosition = refreshFood()
        this.isVisible = true
        refreshGraph()
    }

    private fun initFrame() {
        this.setSize(Settings.widgetWidth(), Settings.widgetHeight())
        this.title = "Snake V0.1"

        this.setLocationRelativeTo(null)
        this.defaultCloseOperation = EXIT_ON_CLOSE
        this.layout = null
        this.addKeyListener(this)

        val jMenuBar = JMenuBar()
        val functionJMenu = JMenu("Menu")
        val helpJMenu = JMenu("Help")

        functionJMenu.add(replayItem)
        functionJMenu.add(settingsItem)
        functionJMenu.add(exitItem)
        helpJMenu.add(tutorialItem)
        helpJMenu.add(aboutItem)

        jMenuBar.add(functionJMenu)
        jMenuBar.add(helpJMenu)

        this.jMenuBar = jMenuBar

        replayItem.addActionListener(this)
        settingsItem.addActionListener(this)
        exitItem.addActionListener(this)
        tutorialItem.addActionListener(this)
        aboutItem.addActionListener(this)
    }

    private fun initSnake() = Snake(
        map[Settings.x / 2][Settings.y / 2],
        Snake(
            map[Settings.x / 2][Settings.y / 2 - 1]
        )
    )

    private fun newMap() = Array(Settings.x) { a ->
        Array(Settings.y) {
            Tile(a, it)
        }
    }

    private fun refreshFood(): Tile {
        val randomMax = Settings.x * Settings.y - Settings.score - 3
        var tileOrder = (0..randomMax).random()
        for (i in map) {
            for (j in i) {
                if (j.snake) continue
                if (tileOrder == 0) {
                    return j
                }
                tileOrder--
            }
        }
        return map[0][0]
    }

    private fun refreshGraph() {
        this.contentPane.removeAll()
        this.contentPane.repaint()
    }

    private fun createDialog(s: String) {
        val jDialog = JDialog()

        val jLabel = JLabel(s)
        jLabel.setBounds(0, 0, 256, 128)
        jDialog.setSize(256, 128)

        jDialog.contentPane.add(jLabel)
        jDialog.isAlwaysOnTop = true
        jDialog.setLocationRelativeTo(null)
        jDialog.isModal = true
        jDialog.isVisible = true
    }

    override fun paint(g: Graphics?) {
        super.paint(g)
        for (i in map) {
            for (j in i) {
                g!!.color = Colors.TileEdge
                g.fillRect(
                    j.getPaintX(), j.getPaintY(),
                    TILE_WIDTH, TILE_WIDTH
                )
                g.color = Colors.Tile
                g.fillRect(
                    j.getPaintX(), j.getPaintY(),
                    TILE_WIDTH - TILE_EDGE * 2,
                    TILE_WIDTH - TILE_EDGE * 2
                )
            }
        }

        g!!.color = Colors.Snake
        var snakeBody: Snake? = snakeTail
        while (snakeBody != null) {
            val tile = snakeBody.position
            if (snakeBody.toward == null) g.color = Colors.SnakeHead
            g.fillRect(
                tile.getPaintX(), tile.getPaintY(),
                TILE_WIDTH - 4, TILE_WIDTH - 4
            )
            snakeBody = snakeBody.toward
        }

        g.color = Colors.Food
        g.fillArc(
            foodPosition.getPaintX() + 3, foodPosition.getPaintY() + 3,
            TILE_WIDTH - 9, TILE_WIDTH - 9,
            0, 360
        )
    }

    override fun keyTyped(e: KeyEvent?) {
        /*
        a w d s 65 87 68 83
        up    38
        down  40
        left  37
        right 39
        f 70
        l 76
        */
    }

    override fun keyPressed(e: KeyEvent?) {
    }

    override fun keyReleased(e: KeyEvent?) {
    }

    override fun actionPerformed(e: ActionEvent?) {
        val obj = e!!.source
        when (obj) {
            replayItem -> {
                snakeTail = initSnake()
                snakeHead = snakeTail.toward!!
                foodPosition = refreshFood()
                refreshGraph()
            }

            tutorialItem -> createDialog("")

            aboutItem -> createDialog("https://github.com/SOR2171/")

            settingsItem -> {}

            exitItem -> exitProcess(0)
        }
    }
}