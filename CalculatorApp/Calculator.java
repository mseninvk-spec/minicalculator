import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.text.DecimalFormat;

public class Calculator extends JFrame implements ActionListener, KeyListener {

    // UI Components
    private JLabel historyLabel;
    private JTextField displayField;

    // Calculation State
    private double num1 = 0;
    private double num2 = 0;
    private String operator = "";
    private boolean startNewNumber = true;

    private static final DecimalFormat FORMATTER = new DecimalFormat("#.########");

    public Calculator() {
        // Window setup
        setTitle("Mini Calculator");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(340, 480);
        setResizable(false);
        setLocationRelativeTo(null);
        getContentPane().setBackground(new Color(28, 33, 40));
        setLayout(new BorderLayout(10, 10));

        // Display Panel
        JPanel displayPanel = new JPanel();
        displayPanel.setLayout(new BoxLayout(displayPanel, BoxLayout.Y_AXIS));
        displayPanel.setBackground(new Color(28, 33, 40));
        displayPanel.setBorder(new EmptyBorder(15, 15, 10, 15));

        historyLabel = new JLabel(" ");
        historyLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        historyLabel.setForeground(new Color(139, 148, 158));
        historyLabel.setAlignmentX(Component.RIGHT_ALIGNMENT);

        displayField = new JTextField("0");
        displayField.setFont(new Font("Segoe UI", Font.BOLD, 36));
        displayField.setForeground(Color.WHITE);
        displayField.setBackground(new Color(28, 33, 40));
        displayField.setHorizontalAlignment(JTextField.RIGHT);
        displayField.setBorder(null);
        displayField.setEditable(false);
        displayField.setFocusable(false);
        displayField.setAlignmentX(Component.RIGHT_ALIGNMENT);

        displayPanel.add(historyLabel);
        displayPanel.add(Box.createVerticalStrut(5));
        displayPanel.add(displayField);

        add(displayPanel, BorderLayout.NORTH);

        // Buttons Panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(5, 4, 8, 8));
        buttonPanel.setBackground(new Color(28, 33, 40));
        buttonPanel.setBorder(new EmptyBorder(5, 15, 15, 15));

        // ⌫ is the single-digit erase / backspace button
        String[] buttonLabels = {
            "C", "⌫", "%", "/",
            "7", "8", "9", "*",
            "4", "5", "6", "-",
            "1", "2", "3", "+",
            "±", "0", ".", "="
        };

        for (String label : buttonLabels) {
            JButton btn = createButton(label);
            btn.addActionListener(this);
            buttonPanel.add(btn);
        }

        add(buttonPanel, BorderLayout.CENTER);

        // Keyboard Support
        addKeyListener(this);
        setFocusable(true);
        requestFocusInWindow();
    }

    private JButton createButton(String label) {
        JButton button = new JButton(label);

        // Use Segoe UI Symbol or SansSerif for ⌫ backspace glyph compatibility
        if (label.equals("⌫")) {
            button.setFont(new Font("Segoe UI Symbol", Font.BOLD, 20));
            button.setToolTipText("Erase single digit (Backspace)");
        } else {
            button.setFont(new Font("Segoe UI", Font.BOLD, 18));
        }

        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Styling based on button type
        if (label.equals("=")) {
            button.setBackground(new Color(46, 160, 67));
            button.setForeground(Color.WHITE);
        } else if (label.equals("/") || label.equals("*") || label.equals("-") || label.equals("+")) {
            button.setBackground(new Color(56, 139, 253));
            button.setForeground(Color.WHITE);
        } else if (label.equals("C") || label.equals("⌫") || label.equals("%") || label.equals("±")) {
            button.setBackground(new Color(45, 51, 59));
            button.setForeground(new Color(240, 246, 252));
        } else {
            // Numbers and decimal
            button.setBackground(new Color(34, 39, 46));
            button.setForeground(new Color(240, 246, 252));
        }

        // Hover effect
        Color defaultBg = button.getBackground();
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(defaultBg.brighter());
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(defaultBg);
            }
        });

        return button;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        processCommand(command);
        requestFocusInWindow();
    }

    private void processCommand(String command) {
        try {
            if (command.matches("[0-9]")) {
                handleNumber(command);
            } else if (command.equals(".")) {
                handleDecimal();
            } else if (command.equals("C")) {
                handleClear();
            } else if (command.equals("⌫") || command.equals("DEL") || command.equals("←")) {
                handleBackspace();
            } else if (command.equals("±")) {
                handleToggleSign();
            } else if (command.equals("%")) {
                handlePercent();
            } else if (command.equals("+") || command.equals("-") || command.equals("*") || command.equals("/")) {
                handleOperator(command);
            } else if (command.equals("=")) {
                handleEquals();
            }
        } catch (Exception ex) {
            displayField.setText("Error");
            startNewNumber = true;
        }
    }

    private void handleNumber(String numStr) {
        if (startNewNumber || displayField.getText().equals("0") || displayField.getText().equals("Error")) {
            displayField.setText(numStr);
            startNewNumber = false;
        } else {
            displayField.setText(displayField.getText() + numStr);
        }
    }

    private void handleDecimal() {
        if (startNewNumber || displayField.getText().equals("Error")) {
            displayField.setText("0.");
            startNewNumber = false;
        } else if (!displayField.getText().contains(".")) {
            displayField.setText(displayField.getText() + ".");
        }
    }

    private void handleClear() {
        num1 = 0;
        num2 = 0;
        operator = "";
        startNewNumber = true;
        displayField.setText("0");
        historyLabel.setText(" ");
    }

    /**
     * Erases a single digit from right to left.
     */
    private void handleBackspace() {
        String currentText = displayField.getText();

        if (currentText.equals("Error") || currentText.startsWith("Error")) {
            displayField.setText("0");
            startNewNumber = true;
            return;
        }

        if (currentText.equals("0")) {
            return;
        }

        // Erase one digit from the current number
        if (currentText.length() > 1) {
            String newText = currentText.substring(0, currentText.length() - 1);
            if (newText.equals("-") || newText.equals("-0") || newText.isEmpty()) {
                displayField.setText("0");
                startNewNumber = true;
            } else {
                displayField.setText(newText);
                startNewNumber = false;
            }
        } else {
            // Last digit erased, reset to 0
            displayField.setText("0");
            startNewNumber = true;
        }
    }

    private void handleToggleSign() {
        String currentText = displayField.getText();
        if (currentText.equals("Error") || currentText.equals("0")) return;

        double val = Double.parseDouble(currentText);
        val = -val;
        displayField.setText(formatResult(val));
    }

    private void handlePercent() {
        String currentText = displayField.getText();
        if (currentText.equals("Error")) return;

        double val = Double.parseDouble(currentText);
        val = val / 100.0;
        displayField.setText(formatResult(val));
        startNewNumber = true;
    }

    private void handleOperator(String op) {
        if (!operator.isEmpty() && !startNewNumber) {
            handleEquals();
        }

        num1 = Double.parseDouble(displayField.getText());
        operator = op;
        historyLabel.setText(formatResult(num1) + " " + operator);
        startNewNumber = true;
    }

    private void handleEquals() {
        if (operator.isEmpty()) return;

        num2 = Double.parseDouble(displayField.getText());
        double result = 0;

        switch (operator) {
            case "+":
                result = num1 + num2;
                break;
            case "-":
                result = num1 - num2;
                break;
            case "*":
                result = num1 * num2;
                break;
            case "/":
                if (num2 == 0) {
                    displayField.setText("Error: Div by 0");
                    historyLabel.setText(" ");
                    operator = "";
                    startNewNumber = true;
                    return;
                }
                result = num1 / num2;
                break;
        }

        historyLabel.setText(formatResult(num1) + " " + operator + " " + formatResult(num2) + " =");
        displayField.setText(formatResult(result));
        num1 = result;
        operator = "";
        startNewNumber = true;
    }

    private String formatResult(double value) {
        if (Double.isInfinite(value) || Double.isNaN(value)) {
            return "Error";
        }
        return FORMATTER.format(value);
    }

    // Keyboard support
    @Override
    public void keyTyped(KeyEvent e) {
        char ch = e.getKeyChar();
        if (ch >= '0' && ch <= '9') {
            processCommand(String.valueOf(ch));
        } else if (ch == '.') {
            processCommand(".");
        } else if (ch == '+' || ch == '-' || ch == '*' || ch == '/') {
            processCommand(String.valueOf(ch));
        } else if (ch == '=' || ch == '\n') {
            processCommand("=");
        } else if (ch == '%') {
            processCommand("%");
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        if (keyCode == KeyEvent.VK_BACK_SPACE) {
            // Erase single digit via keyboard Backspace
            processCommand("⌫");
        } else if (keyCode == KeyEvent.VK_ESCAPE) {
            processCommand("C");
        } else if (keyCode == KeyEvent.VK_ENTER) {
            processCommand("=");
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {}

    public static void main(String[] args) {
        // Set cross-platform look and feel for a clean appearance
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception ignored) {}

        SwingUtilities.invokeLater(() -> {
            Calculator calc = new Calculator();
            calc.setVisible(true);
        });
    }
}
