# TetrisJFX

## GitHub
https://github.com/rianlijy/CW2025.git

## Compilation Instructions

This project is compiled using **IntelliJ IDEA** with **Java 23** and **Maven**.

### Prerequisites
- IntelliJ IDEA (or compatible IDE)
- Java 23 JDK
- Maven 3.6+ (or use Maven wrapper included in project)

### Step-by-Step Compilation Guide

1. **Open the Project**
   - Open IntelliJ IDEA
   - Select "Open" and navigate to the project directory
   - IntelliJ will automatically detect the Maven project structure

2. **Configure Java Version**
   - Go to File -> Project Structure -> Project
   - Set Project SDK to Java 23
   - Set Project language level to 23

3. **Maven Configuration**
   - IntelliJ should automatically import Maven dependencies
   - If not, right-click on pom.xml and select Maven -> Reload Project
   - Wait for dependencies to download (JavaFX controls, FXML, media)

4. **Build the Project**
   - Go to Build -> Build Project (or press Ctrl+F9 / Cmd+F9)
   - Alternatively, use Maven: `mvn clean compile` in the terminal

5. **Run the Application**
   - Locate Main.java in `src/main/java/com/comp2042/controller/`
   - Right-click on Main.java and select Run 'Main.main()'
   - Or use Maven: `mvn clean javafx:run`

### Dependencies
The project uses the following Maven dependencies (configured in pom.xml):
- JavaFX Controls (for UI components)
- JavaFX FXML (for layout files)
- JavaFX Media (for audio support)

### Special Settings
- Java source/target version: 23
- Main class: `com.comp2042.controller.Main`
- Resources directory: `src/main/resources/` (contains FXML, CSS, fonts, and audio files)

---

## Implemented and Working Properly

### Core Tetris Mechanics
- **Standard Tetris Gameplay**: Full implementation of classic Tetris with all 7 tetromino pieces (I, J, L, O, S, T, Z)
- **Piece Movement**: Arrow keys and WASD support for left/right/down movement
- **Piece Rotation**: UP/W key rotates pieces with wall kick system
- **Line Clearing**: Automatic detection and removal of completed rows with score calculation

### Enhanced Gameplay Features

#### Hold Piece System
- Press SHIFT or C to hold current piece
- Swap between current and held piece
- Can only hold once per piece (resets when piece locks)
- Visual hold panel displays stored piece
- Audio feedback when holding

#### Hard Drop
- Press SPACE to instantly drop piece to bottom
- Fast placement for experienced players
- Immediate piece lock on landing

#### Ghost Piece Preview
- Semi-transparent preview showing where piece will land
- White outline for visibility
- Updates in real-time as piece moves
- Helps plan optimal placement

#### Next Pieces Preview
- Shows next 5 upcoming pieces
- Better strategic planning capability
- Each piece rendered in its own preview panel
- Uses 7-bag generation system for fair distribution

### Level and Scoring System

#### Progressive Level System
- Level increases every 10 lines cleared
- Level display in UI updates in real-time
- Level affects block fall speed (gets faster as level increases)
- Speed formula: max(200, 600 - (level - 1) x 100) milliseconds
- Maximum level: 5

#### Enhanced Scoring
- **Line Clear Scoring**:
  - 1 line: 100 x level points
  - 2 lines: 300 x level points
  - 3 lines: 500 x level points
  - 4 lines (Tetris): 800 x level points
- **Soft Drop**: 1 point per row when pressing DOWN/S
- Score and level display updated in real-time

### Audio System

#### Background Music
- Continuous background music during gameplay
- Separate music tracks for different game states
- Smooth transitions between tracks

#### Sound Effects
- Piece placement sound (when piece locks)
- Garbage warning sound (countdown alert)
- Volume control via slider in UI
- All sounds can be muted

#### Volume Control
- Volume slider in game UI (0-100%)
- Real-time volume adjustment
- Mute toggle functionality (M key)
- Last volume remembered when unmuting

### Garbage Row System

#### Timed Garbage Mechanic
- Garbage row appears every 30 seconds
- 5-second warning with flashing animation
- Warning sound plays during countdown
- Row added at bottom with random gap
- Increases game difficulty over time
- Pauses when game is paused
- Resets on new game

#### Visual and Audio Feedback
- Bottom row flashes gray during warning period
- 8 flash cycles (300ms on, 600ms off)
- Warning sound (garbage_warning.mp3) plays with each flash
- Audio synchronized with visual flashing animation
- Sound volume adjustable via volume slider
- Warning sounds pause with game pause
- Board redraws after garbage row added

### Pause System
- Press P or ESC to pause/resume game
- "PAUSED" overlay displayed when paused
- All timers pause (block fall timer, garbage timer)
- Garbage warning animation pauses mid-flash
- Can't move pieces while paused
- Press P/ESC again to resume

### Game Over Handling
- Automatic detection when new piece can't spawn
- Game over panel displayed with restart option
- All timers stop automatically
- Press N to start new game
- Board and score reset on new game

### Visual Enhancements

#### Modern Color Scheme
- Distinct colors for each tetromino type:
  - I-piece: Cyan (#00AFFF)
  - J-piece: Purple (#B044FF)
  - L-piece: Pink (#FF2DAE)
  - O-piece: Yellow (#FFF65B)
  - S-piece: Green (#63FFA7)
  - Z-piece: Red (#FF4E3D)
  - T-piece: Magenta (#FF6BFA)
  - Garbage: Gray (#777777)
- All cells styled as squares with consistent sizing (20px)

#### UI Components
- Score display with digital font styling
- Level indicator
- Pause label overlay
- Preview box for next pieces (right side)
- Hold box for held piece (left side)
- Notification panel for score bonuses
- Volume slider for audio control

### Input Controls
- **Movement**: LEFT/A (left), RIGHT/D (right), DOWN/S (soft drop)
- **Rotation**: UP/W (rotate clockwise)
- **Hard Drop**: SPACE
- **Hold Piece**: SHIFT or C
- **Pause**: P or ESC
- **Mute**: M
- **New Game**: N

### Wall Kick Implementation
- When rotation would cause collision, attempts multiple positions:
  - Original position (0, 0)
  - One cell right (+1, 0)
  - One cell left (-1, 0)
  - One cell up (0, -1)
  - Two cells right (+2, 0)
  - Two cells left (-2, 0)
- Prevents frustrating rotation failures near walls and other pieces

---

## New Java Classes

### Controller Package (com.comp2042.controller)

1. **GarbageRow.java**
   - **Location**: `src/main/java/com/comp2042/controller/GarbageRow.java`
   - **Purpose**: Manages the timed garbage row mechanic. Controls a 30-second timer that triggers warning animations, plays warning sounds, and adds garbage rows to the board. Handles pause/resume functionality and integrates with the game board to add rows with random gaps. Uses JavaFX Timeline for timing and animation control.

2. **GameListener.java** (Interface)
   - **Location**: `src/main/java/com/comp2042/controller/GameListener.java`
   - **Purpose**: Defines callback interface for game events. Provides methods for game over notifications, board changes, preview updates, and hold piece changes. Implements observer pattern to decouple game logic from UI updates.

3. **InputHandler.java**
   - **Location**: `src/main/java/com/comp2042/controller/InputHandler.java`
   - **Purpose**: Centralized keyboard input processing. Handles all game controls including movement (WASD + arrows), rotation, hard drop, hold piece, pause, mute, and new game. Separates input handling from game logic and UI code. Prevents input when game is paused or over.

### UI Package (com.comp2042.ui)

4. **Sound.java**
   - **Location**: `src/main/java/com/comp2042/ui/Sound.java`
   - **Purpose**: Complete audio management system. Handles background music playback (with looping), sound effects (place, warning), volume control, and mute functionality. Uses JavaFX MediaPlayer for music and AudioClip for sound effects. Maintains last volume level for unmute functionality.

5. **BoardRenderer.java**
   - **Location**: `src/main/java/com/comp2042/ui/BoardRenderer.java`
   - **Purpose**: Handles all rendering of the game board and active pieces. Manages the creation and positioning of Rectangle objects for the board matrix, current brick, and ghost piece. Separates rendering logic from game controller. Updates visual representation based on game state.

6. **PreviewRenderer.java**
   - **Location**: `src/main/java/com/comp2042/ui/PreviewRenderer.java`
   - **Purpose**: Renders the next 5 upcoming pieces in preview panels. Creates and manages 5 GridPane panels with 4×4 Rectangle grids. Calculates piece bounds and centers pieces in preview area. Updates preview when new pieces enter queue.

7. **HoldRenderer.java**
   - **Location**: `src/main/java/com/comp2042/ui/HoldRenderer.java`
   - **Purpose**: Renders the currently held piece in the hold panel. Creates GridPane with 4×4 Rectangle grid for displaying held tetromino. Calculates piece bounds for centering. Updates display when hold piece changes.

8. **ColorUtil.java**
   - **Location**: `src/main/java/com/comp2042/ui/ColorUtil.java`
   - **Purpose**: Utility class providing color mapping for tetromino types and garbage rows. Returns appropriate JavaFX Color/Paint objects based on integer cell values. Centralizes color scheme for consistent visuals across all renderers.

---

## Modified Java Classes

### Core Game Logic

1. **GameController.java**
   - **Location**: `src/main/java/com/comp2042/controller/GameController.java`
   - **Changes**:
     - Implemented GameListener pattern with add/notify methods
     - Added hardDrop() method for instant piece placement with scoring
     - Modified onDownEvent() to include locked state detection and soft drop scoring
     - Enhanced scoring to use level-based multipliers (score x level)
     - Added hold() method to interface with board's hold functionality
     - Integrated GarbageRow system with callback and pause control
     - Added onPauseStateChanged() to pause/resume garbage timer
     - Implemented level progression system (every 10 lines increases level)
     - Added blockFallSpeed() method to dynamically adjust drop speed based on level
     - Created listener notification methods for game over, board changes, preview, and hold
   - **Reason**: To implement new gameplay mechanics (hold, hard drop, garbage), improve scoring system with level multipliers, create event-driven architecture for better separation of concerns, and enable pause functionality across game systems.

2. **GuiController.java**
   - **Location**: `src/main/java/com/comp2042/controller/GuiController.java`
   - **Changes**:
     - Implements GameListener interface for event-driven updates
     - Extracted rendering logic to BoardRenderer, PreviewRenderer, HoldRenderer
     - Removed inline key event handler, delegated to InputHandler class
     - Integrated Sound system for audio playback and volume control
     - Added volume slider with change listener and focus management
     - Added pause label visibility control (showPauseOverlay/hidePauseOverlay)
     - Modified initGameView() to initialize all renderers and ghost piece rectangles
     - Added callRefreshBrick(), callMoveDown(), callTogglePause() methods for InputHandler
     - Integrated bonus score notifications with level multiplier display
     - Added bindLevel() method for level property binding
     - Implemented togglePause() with timer control and pause state notification
     - Added GameController reference and notifyPause() method
     - Enhanced moveDown() to play placement sound when piece locks
   - **Reason**: To separate concerns (rendering, input, audio), improve code maintainability and testability, implement audio system, add pause functionality, integrate level system display, and create cleaner event-driven architecture.

3. **SimpleBoard.java**
   - **Location**: `src/main/java/com/comp2042/game/SimpleBoard.java`
   - **Changes**:
     - Changed from RandomBrickGenerator to SevenBagGenerator for fairer piece distribution
     - Added nextFive queue (Deque) to track upcoming 5 pieces
     - Implemented getNextFiveMatrices() to provide preview data
     - Added holdPiece() method with held piece swapping logic
     - Added heldBrick field and holdUsed flag for hold functionality
     - Implemented getHeldMatrix() to provide hold display data
     - Enhanced getViewData() to include next 5 pieces, ghost piece, and held piece
     - Added computeGhostPosition() method to calculate ghost piece landing spot
     - Modified createNewBrick() to reset holdUsed flag and draw from nextFive queue
     - Enhanced rotateLeftBrick() with comprehensive wall kick system (6 kick attempts)
     - Implemented addGarbageRow() method to shift board up and add garbage row at bottom
     - Changed initial position from (4,10) to (3,0) for proper spawn location
   - **Reason**: To implement hold piece feature, improve piece generation fairness, add ghost piece preview, support next 5 preview, improve rotation with wall kicks, implement garbage row mechanic, and fix spawn position for better gameplay.

4. **Score.java**
   - **Location**: `src/main/java/com/comp2042/game/Score.java`
   - **Changes**:
     - Added level IntegerProperty for level tracking and binding
     - Added linesClearedTotal field to track cumulative lines cleared
     - Implemented addLines(int lines) method for level progression
     - Added getLevel() and levelProperty() methods
     - Modified reset() to reset level to 1 and clear lines count
     - Level calculation: min(5, (linesClearedTotal / 10) + 1)
   - **Reason**: To implement level progression system, enable UI binding for level display, track total lines for level calculation, and support level-based scoring multipliers.

5. **ViewData.java**
   - **Location**: `src/main/java/com/comp2042/game/ViewData.java`
   - **Changes**:
     - Changed nextBrickData from single int[][] to List<int[][]> for next 5 pieces
     - Added ghostBrick field (int[][]) for ghost piece rendering
     - Added ghostX and ghostY fields for ghost piece position
     - Added heldBrick field (int[][]) for hold piece display
     - Updated constructor to accept nextFiveBricks, ghost data, and held brick
     - Added getNextFive(), getGhostBrick(), getGhostX(), getGhostY(), getHeldBrick() methods
     - Maintained deep copy pattern for all array data using MatrixOperations.copy()
   - **Reason**: To support next 5 piece preview, ghost piece display, and hold piece display. Provides complete data transfer object for all visual elements needed by renderers.

6. **Board.java** (Interface)
   - **Location**: `src/main/java/com/comp2042/game/Board.java`
   - **Changes**:
     - Added addGarbageRow() method declaration
   - **Reason**: To support garbage row mechanic in the board interface contract.

7. **BrickRotator.java**
   - **Location**: `src/main/java/com/comp2042/game/BrickRotator.java`
   - **Changes**:
     - Added getBrick() method to return current Brick object
   - **Reason**: To enable hold piece functionality by providing access to current brick for swapping.

8. **DownData.java**
   - **Location**: `src/main/java/com/comp2042/game/DownData.java`
   - **Changes**:
     - Added locked field (boolean) to indicate if piece locked on this move
     - Added isLocked() getter method
     - Updated constructor to accept locked parameter
   - **Reason**: To communicate piece lock state to UI for triggering placement sound and visual feedback.

9. **ClearRow.java, MoveEvent.java, NextShapeInfo.java** (No functional changes)
   - **Location**: `src/main/java/com/comp2042/game/`
   - **Changes**: No modifications to logic, only maintained for game mechanics
   - **Reason**: Core data structures remained suitable for enhanced gameplay

### Display Classes

10. **GameOverPanel.java**
    - **Location**: `src/main/java/com/comp2042/ui/GameOverPanel.java`
    - **Changes**: Moved to ui package (previously in root), no functional changes
    - **Reason**: Better package organization

11. **NotificationPanel.java**
    - **Location**: `src/main/java/com/comp2042/ui/NotificationPanel.java`
    - **Changes**: Moved to ui package (previously in root), no functional changes
    - **Reason**: Better package organization

---

## Unexpected Problems

### 1. Garbage Row Timer and Pause Interaction
- **Problem**: Initially, the garbage timer and warning animation would continue running even when the game was paused, leading to garbage rows being added while the player couldn't respond.
- **Impact**: Unfair gameplay experience as garbage could accumulate during pause state.
- **Solution**: Implemented pause/resume functionality in GarbageRow class using Timeline.pause() and Timeline.play(). Added onPauseStateChanged() callback from GameController to GarbageRow. Ensured both garbageTimer and flashTimeline respect pause state. Added isPause() checks before executing warning effects.

### 2. GameBoard and Preview/Hold Panel Positioning
- **Problem**: When implementing the new layout with preview panels on the right and hold panel on the left, the positioning was challenging. The original code used absolute positioning with LayoutX/LayoutY which didn't work well with the new GridPane-based board renderer. The preview and hold panels needed to be properly aligned and sized relative to the game board.
- **Impact**: Panels would overlap, appear in wrong positions, or not scale properly with the window. The hold and preview pieces were either too large, too small, or misaligned within their containers.
- **Solution**: Refactored the rendering system completely. Created separate renderer classes (BoardRenderer, PreviewRenderer, HoldRenderer) that use GridPane constraints instead of absolute positioning. Used VBox containers with proper spacing and alignment settings. Implemented dynamic sizing using ColumnConstraints and RowConstraints for the game board. Set proper cell sizes (20px for game board, 12px for preview/hold). Used GridPane.add() with proper row/column indices for all elements. This ensured consistent positioning across all panels.

### 3. Ghost Piece Not Displaying Initially
- **Problem**: The ghost piece (preview of where the active piece would land) was not visible when the game first started or when a new piece spawned. The ghost rectangles were being created but not properly initialized or positioned in the GridPane.
- **Impact**: Players couldn't see where their piece would land at the start, reducing the utility of the ghost piece feature. This only became visible after the first movement, which was confusing.
- **Solution**: Modified the initBoard() method in BoardRenderer to properly initialize ghost rectangles with correct properties (transparent fill, white stroke, visibility set to false initially). Added ghost piece positioning logic to the initial board setup in initGameView(). Ensured that refreshBrick() is called immediately after board initialization with the first piece's ViewData, which triggers the ghost position calculation and rendering. Added proper bounds checking in the ghost rendering loop to handle pieces that spawn partially above the visible area.

---

*This README documents the complete implementation and enhancement of the TetrisJFX game project with modern features and improved code architecture.*
