import java.awt.event.ActionEvent
import java.awt.event.ActionListener
import javax.swing.*

class SettingFrame(private val gf: GameFrame) : JFrame(), ActionListener {

    private val topB = Box.createVerticalBox()
    private val mapSizeB = Box.createVerticalBox()
    private val difficultyB = Box.createVerticalBox()
    private val settingsB = Box.createHorizontalBox()
    private val controllerB = Box.createHorizontalBox()

    private val cancelBtn = JButton("Cancel")
    private val confirmBtn = JButton("Confirm")
    private val mwText = JLabel("map width:              ")
    private val mhText = JLabel("map height:             ")
    private val mapWidthTF = JTextField(Settings.x.toString())
    private val mapHeightTF = JTextField(Settings.y.toString())
    private val difficultyRB1 = JRadioButton("Easy")
    private val difficultyRB2 = JRadioButton("Normal")
    private val difficultyRB3 = JRadioButton("Hard")
    private val difficultyRB4 = JRadioButton("Impossible")
    private val difficultyBG = ButtonGroup()

    init {
        this.setSize(SW_WIDTH, SW_HEIGHT)
        this.title = "Settings"
        this.isResizable = false
        this.isVisible = false
        this.setLocationRelativeTo(gf)
        this.defaultCloseOperation = HIDE_ON_CLOSE
//        this.addKeyListener(this)

        cancelBtn.addActionListener(this)
        confirmBtn.addActionListener(this)

        when (Settings.difficulty) {
            Difficulty.E -> difficultyRB1.isSelected = true
            Difficulty.N -> difficultyRB2.isSelected = true
            Difficulty.H -> difficultyRB3.isSelected = true
            Difficulty.I -> difficultyRB4.isSelected = true
        }

        cancelBtn.setSize(50, 20)
        confirmBtn.setSize(50, 20)

        mapSizeB.add(Box.createVerticalStrut(25))
        mapSizeB.add(mwText)
        mapSizeB.add(mapWidthTF)
        mapSizeB.add(Box.createVerticalStrut(30))
        mapSizeB.add(mhText)
        mapSizeB.add(mapHeightTF)
        mapSizeB.add(Box.createVerticalStrut(35))

        difficultyBG.add(difficultyRB1)
        difficultyBG.add(difficultyRB2)
        difficultyBG.add(difficultyRB3)
        difficultyBG.add(difficultyRB4)

        difficultyB.add(Box.createVerticalGlue())
        difficultyB.add(difficultyRB1)
        difficultyB.add(difficultyRB2)
        difficultyB.add(difficultyRB3)
        difficultyB.add(difficultyRB4)
        difficultyB.add(Box.createVerticalGlue())

        settingsB.add(Box.createHorizontalStrut(50))
        settingsB.add(mapSizeB)
        settingsB.add(Box.createHorizontalStrut(30))
        settingsB.add(difficultyB)
        settingsB.add(Box.createHorizontalStrut(50))

        controllerB.add(Box.createHorizontalGlue())
        controllerB.add(cancelBtn)
        controllerB.add(Box.createHorizontalGlue())
        controllerB.add(confirmBtn)
        controllerB.add(Box.createHorizontalGlue())

        topB.add(Box.createVerticalStrut(20))
        topB.add(settingsB)
        topB.add(Box.createVerticalStrut(20))
        topB.add(controllerB)
        topB.add(Box.createVerticalStrut(20))
        this.add(topB)
    }

    override fun actionPerformed(e: ActionEvent?) {
        when (e!!.source) {
            cancelBtn -> {
                when (Settings.difficulty) {
                    Difficulty.E -> difficultyRB1.isSelected = true
                    Difficulty.N -> difficultyRB2.isSelected = true
                    Difficulty.H -> difficultyRB3.isSelected = true
                    Difficulty.I -> difficultyRB4.isSelected = true
                }
                mapWidthTF.text = Settings.x.toString()
                mapHeightTF.text = Settings.y.toString()
            }

            confirmBtn -> {
                if (difficultyRB1.isSelected) Settings.difficulty = Difficulty.E
                if (difficultyRB2.isSelected) Settings.difficulty = Difficulty.N
                if (difficultyRB3.isSelected) Settings.difficulty = Difficulty.H
                if (difficultyRB4.isSelected) Settings.difficulty = Difficulty.I

                Settings.x = mapWidthTF.text.toIntOrNull() ?: Settings.x
                Settings.y = mapHeightTF.text.toIntOrNull() ?: Settings.y
                if (Settings.x !in MAP_X_MIN..MAP_X_MAX) Settings.x = MAP_X_DEFAULT
                if (Settings.y !in MAP_Y_MIN..MAP_Y_MAX) Settings.y = MAP_Y_DEFAULT
                mapWidthTF.text = Settings.x.toString()
                mapHeightTF.text = Settings.y.toString()
                gf.refreshFrame()
            }
        }
        this.isVisible = false
    }
}