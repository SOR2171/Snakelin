import java.awt.Graphics
import java.awt.event.ActionEvent
import java.awt.event.ActionListener
import java.awt.event.KeyEvent
import java.awt.event.KeyListener
import java.util.*
import javax.swing.*
import kotlin.system.exitProcess

class GameFrame : JFrame(), KeyListener, ActionListener {

    private lateinit var map: MapType
    private lateinit var snakeHead: Snake
    private lateinit var snakeTail: Snake
    private lateinit var foodPosition: Tile
    private var settingWidget = SettingFrame(this)
    var roundTime: Long = 800

    private val replayItem = JMenuItem("Reply")
    private val settingsItem = JMenuItem("Settings")
    private val exitItem = JMenuItem("Exit")
    private val tutorialItem = JMenuItem("Tutorial")
    private val aboutItem = JMenuItem("About us")

    private val nextTurnItem = JMenuItem("Next turn")
    private val refreshFoodItem = JMenuItem("Refresh food")

    init {
        this.title = GAME_NAME
        initFrame()
        refreshFrame()
        this.isResizable = false
//        this.isVisible = true
        settingWidget.isVisible = true
    }

    private fun initFrame() {
        this.setLocationRelativeTo(null)
        this.defaultCloseOperation = EXIT_ON_CLOSE
        this.layout = null
        this.addKeyListener(this)

        val jMenuBar = JMenuBar()
        val functionJMenu = JMenu("Menu")
        val helpJMenu = JMenu("Help")
        val debugJMenu = JMenu("Debug")

        functionJMenu.add(replayItem)
        functionJMenu.add(settingsItem)
        functionJMenu.add(exitItem)
        helpJMenu.add(tutorialItem)
        helpJMenu.add(aboutItem)
        debugJMenu.add(nextTurnItem)
        debugJMenu.add(refreshFoodItem)

        jMenuBar.add(functionJMenu)
        jMenuBar.add(helpJMenu)
        if (Settings.debug) jMenuBar.add(debugJMenu)

        this.jMenuBar = jMenuBar

        replayItem.addActionListener(this)
        settingsItem.addActionListener(this)
        exitItem.addActionListener(this)
        tutorialItem.addActionListener(this)
        aboutItem.addActionListener(this)
        nextTurnItem.addActionListener(this)
        refreshFoodItem.addActionListener(this)
    }

    private fun initSnake() = Snake(
        map[Settings.x / 2][Settings.y / 2],
        Snake(map[Settings.x / 2][Settings.y / 2 - 1])
    )

    private fun newMap() = Array(Settings.x) { x ->
        Array(Settings.y) {
            Tile(x, it)
        }
    }

    private fun refreshFood(): Tile {
        val randomMax = Settings.x * Settings.y - Settings.score - 2
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

    fun refreshFrame() {
        this.setSize(Settings.widgetWidth(), Settings.widgetHeight())
        map = newMap()
        snakeTail = initSnake()
        snakeHead = snakeTail.toward!!
        foodPosition = refreshFood()
        Settings.score = 0
        this.repaint()
        pauseGame()
    }

    private fun createDialog(s: String) = createDialog(arrayOf(s))
    private fun createDialog(array: Array<String>) {
        val jDialog = JDialog()
        val sj = StringJoiner("<br>", "<html><body>", "</html></body>")
        var longest = 1

        for (s in array) {
            if (s.length > longest) longest = s.length
            if (s.contains("https")) {
                sj.add("<a href=\"$s\">$s</a>")
            } else {
                sj.add(s)
            }
        }

        val width = longest * 6
        val height = array.size * 20 + UPPER_BOARD

        val jLabel = JLabel(sj.toString())
        jLabel.setBounds(BOARD, 0, width, height)
        jDialog.setSize(width + BOARD * 2, height)

        jDialog.contentPane.add(jLabel)
        jDialog.isAlwaysOnTop = true
        jDialog.setLocationRelativeTo(null)
        jDialog.isModal = true
        jDialog.isVisible = true
    }

    fun goNextTurn() {
        var snakeBody = snakeTail
        var needRefresh = false
        if (snakeHead.position == foodPosition) {
            snakeTail = snakeTail.addBody()
            needRefresh = true
        }
        snakeBody.position.snake = false
        while (snakeBody.toward != null) {
            snakeBody.goAhead(map, this)
            snakeBody = snakeBody.toward ?: snakeBody.also {
                if (it.position.snake) {
                    gameLose()
                } else {
                    it.position.snake = true
                }
            }
        }
        if (needRefresh) {
            if (Settings.score >= Settings.x * Settings.y - 2) gameWin()
            foodPosition = refreshFood()
            Settings.score += 1
        }
        snakeBody.goAhead(map, this)
        snakeBody.position.snake = true
        repaint()
    }

    fun gameLose() {
        pauseGame()
        createDialog(arrayOf("You lose!", "Score: ${Settings.score}"))
    }

    private fun gameWin() {
        pauseGame()
        createDialog(arrayOf("You win!", "Score: ${Settings.score}"))
    }

    private fun pauseGame() {
        Settings.isPause = true
        this.repaint()
    }

    private fun runGame() {
        roundTime = when (Settings.difficulty) {
            Difficulty.E -> 1000
            Difficulty.N -> 800
            Difficulty.H -> 600
            Difficulty.I -> 400
        }
        Settings.isPause = false
        this.repaint()
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

        g!!.color = Colors.Food
        g.fillArc(
            foodPosition.getPaintX() + 3, foodPosition.getPaintY() + 3,
            TILE_WIDTH - 10, TILE_WIDTH - 10,
            0, 360
        )

        g.color = Colors.Snake
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
    }

    override fun keyTyped(e: KeyEvent?) {}

    override fun keyPressed(e: KeyEvent?) {
        /*
        a w d s 65 87 68 83
        up    38
        down  40
        left  37
        right 39
        g 71
        BAR 32
        */
        when (e!!.keyCode) {
            65, 37 -> snakeHead.dir = Direction.L
            87, 38 -> snakeHead.dir = Direction.U
            68, 39 -> snakeHead.dir = Direction.R
            83, 40 -> snakeHead.dir = Direction.D
            71 -> if (Settings.debug) goNextTurn()
            32 -> if (Settings.isPause) runGame() else pauseGame()
        }
    }

    override fun keyReleased(e: KeyEvent?) {}

    override fun actionPerformed(e: ActionEvent?) {
        pauseGame()
        val obj = e!!.source
        when (obj) {
            replayItem -> refreshFrame()

            tutorialItem -> createDialog(
                arrayOf(
                    "press W A S D or arrow keys to change direction.",
                    "press SPACE BAR or Pause to pause the game.",
                    "press G to go next round."
                )
            )

            aboutItem -> createDialog("https://github.com/SOR2171/Snakelin")

            settingsItem -> {
                pauseGame()
                settingWidget.isVisible = true
                this.isVisible = false
            }

            exitItem -> exitProcess(0)

            nextTurnItem -> goNextTurn()

            refreshFoodItem -> foodPosition = refreshFood()
        }
    }
}