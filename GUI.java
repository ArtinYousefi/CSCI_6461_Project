package tryingtowork;
import java.awt.EventQueue;
import javax.swing.*;
import java.awt.Color;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.awt.event.ActionEvent;
import java.awt.Font;
import javax.swing.table.DefaultTableModel;

public class GUI {

    public JFrame frmFrontPanel;
    public static JTextField textField, textField_1, textField_2, textField_3, textField_5, textField_6, textField_7;
    public static JTextField textField_4, textField_8, textField_9, textField_10, textField_11, textField_12, textField_13;
    private JTable memoryTable;
    private DefaultTableModel memoryModel;
    public static Control control;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                GUI window = new GUI();
                window.frmFrontPanel.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * Create the application.
     */
    public GUI() {
        initialize();
        System.out.println("[GUI DEBUG] Initializing GUI...");
        control = new Control(this);
        System.out.println("[GUI DEBUG] Control instance created.");
    }

    /**
     * Initialize the contents of the frame.
     */
    private void initialize() {
        frmFrontPanel = new JFrame();
        frmFrontPanel.getContentPane().setBackground(new Color(204, 153, 255));
        frmFrontPanel.setTitle("CSCI 6461 Machine Simulator");
        frmFrontPanel.setBounds(100, 100, 900, 550);
        frmFrontPanel.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frmFrontPanel.getContentPane().setLayout(null);

        JLabel lblTitle = new JLabel("CSCI 6461 Machine Simulator");
        lblTitle.setFont(new Font("Tahoma", Font.BOLD, 16));
        lblTitle.setBounds(300, 10, 300, 30);
        frmFrontPanel.getContentPane().add(lblTitle);

        JButton btnLoadFile = new JButton("Load");
        btnLoadFile.setBounds(50, 450, 100, 30);
        frmFrontPanel.getContentPane().add(btnLoadFile);
        btnLoadFile.addActionListener(e -> openFileChooser());

        JButton btnStore = new JButton("Store");
        btnStore.setBounds(160, 450, 100, 30);
        frmFrontPanel.getContentPane().add(btnStore);
        btnStore.addActionListener(e -> storeData());

        JButton btnRun = new JButton("Run");
        btnRun.setBounds(270, 450, 100, 30);
        frmFrontPanel.getContentPane().add(btnRun);
        btnRun.addActionListener(e -> runSimulator());

        JButton btnStep = new JButton("Step");
        btnStep.setBounds(380, 450, 100, 30);
        frmFrontPanel.getContentPane().add(btnStep);
        btnStep.addActionListener(e -> stepExecution());

        JButton btnHalt = new JButton("Halt");
        btnHalt.setBounds(490, 450, 100, 30);
        frmFrontPanel.getContentPane().add(btnHalt);
        btnHalt.addActionListener(e -> haltExecution());


        // Memory Table
        memoryModel = new DefaultTableModel(new String[]{"Address", "Value"}, 0);
        memoryTable = new JTable(memoryModel);
        JScrollPane scrollPane = new JScrollPane(memoryTable);
        scrollPane.setBounds(500, 50, 350, 300);
        frmFrontPanel.getContentPane().add(scrollPane);

        // Register Labels and TextFields
        JLabel lblPC = new JLabel("PC:");
        lblPC.setBounds(50, 50, 40, 25);
        frmFrontPanel.getContentPane().add(lblPC);
        textField_8 = new JTextField();
        textField_8.setBounds(100, 50, 100, 25);
        frmFrontPanel.getContentPane().add(textField_8);

        JLabel lblMAR = new JLabel("MAR:");
        lblMAR.setBounds(50, 90, 40, 25);
        frmFrontPanel.getContentPane().add(lblMAR);
        textField_9 = new JTextField();
        textField_9.setBounds(100, 90, 100, 25);
        frmFrontPanel.getContentPane().add(textField_9);

        JLabel lblMBR = new JLabel("MBR:");
        lblMBR.setBounds(50, 130, 40, 25);
        frmFrontPanel.getContentPane().add(lblMBR);
        textField_10 = new JTextField();
        textField_10.setBounds(100, 130, 100, 25);
        frmFrontPanel.getContentPane().add(textField_10);

        JLabel lblIR = new JLabel("IR:");
        lblIR.setBounds(50, 170, 40, 25);
        frmFrontPanel.getContentPane().add(lblIR);
        textField_11 = new JTextField();
        textField_11.setBounds(100, 170, 100, 25);
        frmFrontPanel.getContentPane().add(textField_11);

        JLabel lblCC = new JLabel("CC:");
        lblCC.setBounds(50, 210, 40, 25);
        frmFrontPanel.getContentPane().add(lblCC);
        textField_12 = new JTextField();
        textField_12.setBounds(100, 210, 100, 25);
        frmFrontPanel.getContentPane().add(textField_12);

        JLabel lblMFR = new JLabel("MFR:");
        lblMFR.setBounds(50, 250, 40, 25);
        frmFrontPanel.getContentPane().add(lblMFR);
        textField_13 = new JTextField();
        textField_13.setBounds(100, 250, 100, 25);
        frmFrontPanel.getContentPane().add(textField_13);

        // GPRs (General Purpose Registers)
        JLabel lblGPR = new JLabel("GPR");
        lblGPR.setBounds(250, 50, 40, 25);
        frmFrontPanel.getContentPane().add(lblGPR);
        textField = new JTextField();
        textField.setBounds(300, 50, 100, 25);
        frmFrontPanel.getContentPane().add(textField);
        textField_1 = new JTextField();
        textField_1.setBounds(300, 90, 100, 25);
        frmFrontPanel.getContentPane().add(textField_1);
        textField_2 = new JTextField();
        textField_2.setBounds(300, 130, 100, 25);
        frmFrontPanel.getContentPane().add(textField_2);
        textField_3 = new JTextField();
        textField_3.setBounds(300, 170, 100, 25);
        frmFrontPanel.getContentPane().add(textField_3);

        // IXRs (Index Registers)
        JLabel lblIXR = new JLabel("IXR");
        lblIXR.setBounds(250, 210, 40, 25);
        frmFrontPanel.getContentPane().add(lblIXR);
        textField_5 = new JTextField();
        textField_5.setBounds(300, 210, 100, 25);
        frmFrontPanel.getContentPane().add(textField_5);
        textField_6 = new JTextField();
        textField_6.setBounds(300, 250, 100, 25);
        frmFrontPanel.getContentPane().add(textField_6);
        textField_7 = new JTextField();
        textField_7.setBounds(300, 290, 100, 25);
        frmFrontPanel.getContentPane().add(textField_7);
    }

    private void openFileChooser() {
        JFileChooser fileChooser = new JFileChooser();
        int returnValue = fileChooser.showOpenDialog(frmFrontPanel);

        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            try {
                control.loadLF(selectedFile.getAbsolutePath());
                updateMemoryDisplay();
                updateRegisterDisplay();
            } catch (IOException e) {
                JOptionPane.showMessageDialog(frmFrontPanel, "Error loading file!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    

    private void runSimulator() {
        control.runSimulator();
        updateMemoryDisplay();
        updateRegisterDisplay();
    }

    private void storeData() {
        control.storeData();
        updateMemoryDisplay();
    }


    private void stepExecution() {
        control.stepSimulator();
        updateMemoryDisplay();
        updateRegisterDisplay();
    }

    private void haltExecution() {
        control.haltSimulator();
        JOptionPane.showMessageDialog(frmFrontPanel, "Simulation Halted!", "Info", JOptionPane.INFORMATION_MESSAGE);
    }


    
    public void updateMemoryDisplay() {
        memoryModel.setRowCount(0);
        Memory mem = control.getMemory();
    
        System.out.println("Memory Contents:");
        for (int i = 0; i < 20; i++) { // Display first 20 memory locations
            int value = mem.readWord(i);
            memoryModel.addRow(new Object[]{String.format("%04o", i), String.format("%04o", value)});
            System.out.println("Address: " + i + " -> Value: " + value);
        }
    
        updateRegisterDisplay();
    }
    public void updateGUI() {
        textField_8.setText(String.valueOf(Integer.toOctalString(control.cpu.getPC())));
        textField_9.setText(String.valueOf(Integer.toOctalString(control.cpu.getMAR())));
        textField_10.setText(String.valueOf(Integer.toOctalString(control.cpu.getMBR())));
        textField_11.setText(String.valueOf(Integer.toOctalString(control.cpu.getIR())));
        textField_12.setText(String.valueOf(Integer.toOctalString(control.cpu.getCC())));
        textField_13.setText(String.valueOf(Integer.toOctalString(control.cpu.getMFR())));
    
        textField.setText(String.valueOf(Integer.toOctalString(control.cpu.getGPR()[0])));
        textField_1.setText(String.valueOf(Integer.toOctalString(control.cpu.getGPR()[1])));
        textField_2.setText(String.valueOf(Integer.toOctalString(control.cpu.getGPR()[2])));
        textField_3.setText(String.valueOf(Integer.toOctalString(control.cpu.getGPR()[3])));
    
        textField_5.setText(String.valueOf(Integer.toOctalString(control.cpu.getIX()[0])));
        textField_6.setText(String.valueOf(Integer.toOctalString(control.cpu.getIX()[1])));
        textField_7.setText(String.valueOf(Integer.toOctalString(control.cpu.getIX()[2])));
    
        System.out.println("[DEBUG] GUI Updated: PC=" + control.cpu.getPC());
    }
    

    public void updateRegisterDisplay() {
        Memory mem = control.getMemory();
    
        // Debugging: Print register values
        System.out.println("Updating Registers:");
        System.out.println("PC: " + mem.PC);
        System.out.println("MAR: " + mem.MAR);
        System.out.println("MBR: " + mem.MBR);
        System.out.println("IR: " + mem.IR);
        System.out.println("GPRs: " + mem.GPR[0] + ", " + mem.GPR[1] + ", " + mem.GPR[2] + ", " + mem.GPR[3]);
        System.out.println("IXRs: " + mem.IX[0] + ", " + mem.IX[1] + ", " + mem.IX[2]);
    
        // Update GUI
        textField_8.setText(String.valueOf(Integer.toOctalString(mem.PC)));
        textField_9.setText(String.valueOf(Integer.toOctalString(mem.MAR)));
        textField_10.setText(String.valueOf(Integer.toOctalString(mem.MBR)));
        textField_11.setText(String.valueOf(Integer.toOctalString(mem.IR)));
        textField_12.setText(String.valueOf(Integer.toOctalString(mem.CC)));
        textField_13.setText(String.valueOf(Integer.toOctalString(mem.MFR)));
    
        textField.setText(String.valueOf(Integer.toOctalString(mem.GPR[0])));
        textField_1.setText(String.valueOf(Integer.toOctalString(mem.GPR[1])));
        textField_2.setText(String.valueOf(Integer.toOctalString(mem.GPR[2])));
        textField_3.setText(String.valueOf(Integer.toOctalString(mem.GPR[3])));
    
        textField_5.setText(String.valueOf(Integer.toOctalString(mem.IX[0])));
        textField_6.setText(String.valueOf(Integer.toOctalString(mem.IX[1])));
        textField_7.setText(String.valueOf(Integer.toOctalString(mem.IX[2])));
    }

    
}