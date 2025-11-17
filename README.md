# ColorGame
ColorGame is a console-based betting mini-game where players bet on colors and win based on randomly drawn results. It includes a simple menu system, login/signup, and account balance management with data saved to a file.
![Image Alt](https://github.com/euzop/ColorGame/blob/05fdb080598b9181b50ef7066b751f261e47ab21/ColorGame_Sample.png)

Key features
- Login, signup, and balance management
- Deposit and withdraw options
- Place bets on 1–6 colors
- Random color results with multiplier-based payouts
- Saves user balance to a file for future sessions
- Colored terminal output using ANSI escape codes

Technical highlights
- Uses ANSI codes to display colored text and squares
- File I/O for storing credentials and balances
- Input validation and checks for sufficient balance
- Randomized game logic using java.util.Random
- mOrganized code separating menu, I/O, and game logic
