import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.border.TitledBorder;
import javax.swing.border.EtchedBorder;



/**
 * Graphic interface
 */
public class InterfazGrafica extends JFrame{
    
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
     * Window's width
     */
    final static int LENGTH_X = 500;
    
    /**
     * Window's heigth
     */
    final static int LENGTH_Y = 500;
    
    JTextArea text, textArea;
    JLabel label, lblTextModeOn;
    JSlider slider, sliderLight;
    public JTextField textField;
    JButton btnSendText, button, btnNewButton;
    private JTextField txtCom;
    
    public ConnectThread connectThread = new ConnectThread();
    private JTextField textField_1;
    private JLabel labelPower;
    private JLabel labelSpeed;
    
    public JLabel lblM1Pow, lblM2Pow, lblM3Pow, lblM4Pow;
    public JLabel labelM1v;
    public JLabel labelM2v;
    public JLabel labelM3v;
    public JLabel labelM4v;
    public JLabel labelPitch;
    public JLabel labelRoll;
    public JLabel lblPitch;
    public JLabel lblRoll;
    public JLabel labelX, labelY, labelZ; 
    
    public JLabel labelDiffM1, labelDiffM2,
    			  labelDiffM3, labelDiffM4;
    
    public JLabel lblP0v, lblI0v, lblD0v;
    public JSlider sliderP1, sliderI1, sliderD1;
    public JSlider sliderP0, sliderI0, sliderD0;
    public JSlider sldOffM1, sldOffM2, sldOffM3, sldOffM4;
    public JLabel lblOffM1, lblOffM2, lblOffM3, lblOffM4;
    public JLabel lblP1v, lblI1v, lblD1v;
    public JLabel lblPID;
    public JButton btnDefaultPID;
    public JButton btnTestM1, btnTestM2, btnTestM3, btnTestM4;
    public JButton btnSetGround;
    private JButton btnSwitchPid;
    private JLabel lblLpfAlpha;
    public JSlider sliderAlphaAccel, sliderAlphaGyro, sliderAlphaDeg;
    public JLabel lblAlphaAccel;
    public JLabel lblAlphaGyro;
    public JLabel lblAlphaDeg;
    public JLabel lblGyroXMed, lblGyroZMed;
    
    public JSlider sliderOffX, sliderOffY, sliderOffZ;
    public JLabel lblOffX, lblOffY, lblOffZ, lbl0Pitch, lbl0Roll;
    
    public JLabel lblDeltaPitch, lblDeltaRoll;
    public  JButton btnDefaultOffsets, btnSetLevel, btnSetStillLevel;
    public JButton btnSwitchM1, btnSwitchM2, btnSwitchM3, btnSwitchM4;
    private JPanel panel_4;
    private JPanel panel_5;
    private JPanel panel_6;
    private JPanel panel_8;
    private JPanel panel_9;
    private JPanel panel_10;
    private JPanel panel_11;
    private JPanel panel_13;
    /**
     * Constructor. Abre una nueva ventana en la que 
     * inserta todos los componentes y un tablero.
     * 
     * @param tablero Tablero que se dibuja
     */
    public InterfazGrafica(){
        super("Arduino");
        setResizable(false);
        getContentPane().addKeyListener(new ArduinoKeyListener());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(1070, 600));        
        pack();
        getContentPane().setLayout(null);
        
        slider = new JSlider();
        slider.setValue(3);
        slider.setMaximum(3);
        slider.setBounds(20, 86, 200, 23);
        slider.addKeyListener(new ArduinoKeyListener());
        slider.addChangeListener(new ChangeListener() {
        	public void stateChanged(ChangeEvent arg0) {
        		MainAction.arduino.setSpeed((short)slider.getValue());
        		labelSpeed.setText(Integer.toString(slider.getValue()));
        	}
        });
        getContentPane().add(slider);
        
        JLabel lblBercianityArduino = new JLabel("BXDRONE - ARDUINO GUI CONTROLLER");
        lblBercianityArduino.setFont(new Font("Tahoma", Font.PLAIN, 20));
        lblBercianityArduino.setBounds(59, 11, 363, 23);
        getContentPane().add(lblBercianityArduino);
        
        JLabel lblSpeed = new JLabel("ACCEL (0-3)");
        lblSpeed.setBounds(20, 71, 74, 14);
        getContentPane().add(lblSpeed);
        
        textField = new JTextField();
        textField.addFocusListener(new FocusListener() {
        	public void focusGained(FocusEvent e) {
        		MainAction.window1.lblTextModeOn.setVisible(true);                
            }

			@Override
			public void focusLost(FocusEvent arg0) {
				MainAction.window1.lblTextModeOn.setVisible(false); 
				
			}
        });
/*        textField.addKeyListener(new KeyAdapter() {
        	@Override
        	public void keyReleased(KeyEvent arg0) {
        		if(arg0.getKeyCode() == KeyEvent.VK_ENTER){
        			MainAction.arduino.sendText(MainAction.window1.textField.getText());
            		MainAction.window1.textField.setText("");
            		MainAction.window1.btnSendText.requestFocus();
        		}
        			
        	}
        });*/

        textField.setBounds(20, 232, 190, 20);
        getContentPane().add(textField);
        textField.setColumns(10);
        
        JLabel lblTextMode = new JLabel("Text Mode");
        lblTextMode.setBounds(20, 207, 74, 14);
        getContentPane().add(lblTextMode);
        
        btnSendText = new JButton("Send Text");
        btnSendText.addKeyListener(new ArduinoKeyListener());
        btnSendText.setBounds(20, 263, 100, 23);
/*        btnSendText.addActionListener(new ActionListener(){
        	public void actionPerformed(ActionEvent e){
        		MainAction.arduino.sendText(MainAction.window1.textField.getText());
        		MainAction.window1.textField.setText("");
        	}
        });*/
        getContentPane().add(btnSendText);
        
        JPanel panel = new JPanel();
        panel.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Command Window", TitledBorder.LEADING, TitledBorder.TOP, null, null));
        panel.setBounds(14, 297, 462, 254);
        getContentPane().add(panel);
        panel.setLayout(null);
        
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setAutoscrolls(true);
        scrollPane.setBounds(6, 16, 446, 227);
        panel.add(scrollPane);
        
        textArea = new JTextArea();
        textArea.setFont(new Font("Courier New", Font.PLAIN, 12));
        scrollPane.setViewportView(textArea);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setEditable(false);
        textArea.setMargin(new Insets(0,5,0,5));
        
        lblTextModeOn = new JLabel("TEXT MODE ON!");
        lblTextModeOn.setFont(new Font("Tahoma", Font.BOLD, 12));
        lblTextModeOn.setBounds(220, 235, 109, 15);
        getContentPane().add(lblTextModeOn);
        
        button = new JButton("Connect");
        button.setBounds(349, 132, 89, 23);
        button.addActionListener(new ActionListener(){
        	public void actionPerformed(ActionEvent e){
        		String a = MainAction.window1.txtCom.getText();
        		MainAction.arduino.setPortName(a);
        		connectThread.start();
        		for(ActionListener l : MainAction.window1.button.getActionListeners())
        			MainAction.window1.button.removeActionListener(l);
        		MainAction.window1.button.addActionListener(new ActionListener(){
        			public void actionPerformed(ActionEvent e){
        				connectThread.threadContinue();
        			}
        		});
        		
        	}
        });
        getContentPane().add(button);
        
        JLabel label_1 = new JLabel("Select COM port");
        label_1.setHorizontalAlignment(SwingConstants.LEFT);
        label_1.setBounds(255, 99, 106, 14);
        getContentPane().add(label_1);
        
        txtCom = new JTextField();
        txtCom.setText("COM5");
        txtCom.setColumns(10);
        txtCom.setBounds(352, 96, 86, 20);
        getContentPane().add(txtCom);
        
        textField_1 = new JTextField();
        textField_1.setFont(new Font("Courier New", Font.PLAIN, 12));
        textField_1.setBounds(255, 266, 199, 20);
        textField_1.addKeyListener(new KeyAdapter(){
        	public void keyReleased(KeyEvent arg0){
        		if(arg0.getKeyCode() == KeyEvent.VK_ENTER){
        			String text = textField_1.getText();
        			for(int i=0; i<text.length(); i++){
        				MainAction.arduino.sendData((short) text.charAt(i));
        			}
        			println("[NOC]: " + text);
        			MainAction.arduino.sendData((short) 0);
        			textField_1.setText("");
        			if( text.equals("exit") ){
        				MainAction.window1.setConnected();
        				textField_1.setVisible(false);
        			}
        		}
        	}
        });

        getContentPane().add(textField_1);
        textField_1.setVisible(false);
        textField_1.setColumns(10);
        
        btnNewButton = new JButton("Command Mode");
        btnNewButton.setBounds(327, 231, 127, 23);
/*        btnNewButton.addActionListener(new ActionListener(){
        	public void actionPerformed(ActionEvent e){
        		MainAction.arduino.sendData((short) Comm.COMMANDMODE);
        		MainAction.window1.setInitial();
        		txtCom.setEnabled(false);
        		button.setEnabled(false);
        		textField_1.setVisible(true);
        		textField_1.requestFocus();
        		MainAction.inputS.setStandBy(false);
        		println("--------------------");
        	}
        }); */
        getContentPane().add(btnNewButton);
        
        sliderLight = new JSlider();
        sliderLight.setValue(100);
        sliderLight.setBounds(20, 147, 200, 37);
//        sliderLight.addChangeListener(new ChangeListener() {
//        	public void stateChanged(ChangeEvent arg0) {
//        		MainAction.arduino.setLightPower(sliderLight.getValue());
//        		labelPower.setText(Integer.toString(sliderLight.getValue()));
//        	}
//        });
        sliderLight.addKeyListener(new ArduinoKeyListener());
        getContentPane().add(sliderLight);
        
        JLabel lblLightPower = new JLabel("LIGHT POWER");
        lblLightPower.setBounds(20, 135, 100, 14);
        lblLightPower.addKeyListener(new ArduinoKeyListener());
        getContentPane().add(lblLightPower);
        
        labelPower = new JLabel(Integer.toString(sliderLight.getValue()));
        labelPower.setBounds(225, 151, 40, 16);
        labelPower.addKeyListener(new ArduinoKeyListener());
        getContentPane().add(labelPower);
        
        labelSpeed = new JLabel(Integer.toString(slider.getValue()));
        labelSpeed.setBounds(225, 86, 40, 16);
        labelSpeed.addKeyListener(new ArduinoKeyListener());
        getContentPane().add(labelSpeed);
        
        JPanel panel_1 = new JPanel();
        panel_1.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "TM/TC", TitledBorder.LEADING, TitledBorder.TOP, null, null));
        panel_1.setBounds(482, 11, 570, 548);
        getContentPane().add(panel_1);
        panel_1.setLayout(null);
        
        panel_9 = new JPanel();
        panel_9.setBorder(new TitledBorder(null, "Motors Speeds", TitledBorder.LEADING, TitledBorder.TOP, null, null));
        panel_9.setBounds(10, 19, 212, 151);
        panel_1.add(panel_9);
        panel_9.setLayout(null);
        
        JLabel lblM1 = new JLabel("M1 Speed:");
        lblM1.setBounds(10, 17, 104, 14);
        panel_9.add(lblM1);
        lblM1.setFont(new Font("Courier New", Font.PLAIN, 14));
        
        labelM1v = new JLabel("0");
        labelM1v.setBounds(91, 17, 57, 14);
        panel_9.add(labelM1v);
        labelM1v.setFont(new Font("Courier New", Font.PLAIN, 14));
        
        JLabel lblM2 = new JLabel("M2 Speed:");
        lblM2.setBounds(10, 31, 104, 14);
        panel_9.add(lblM2);
        lblM2.setFont(new Font("Courier New", Font.PLAIN, 14));
        
        labelM2v = new JLabel("0");
        labelM2v.setBounds(91, 31, 57, 14);
        panel_9.add(labelM2v);
        labelM2v.setFont(new Font("Courier New", Font.PLAIN, 14));
        
        JLabel lblM3 = new JLabel("M3 Speed:");
        lblM3.setBounds(10, 46, 104, 14);
        panel_9.add(lblM3);
        lblM3.setFont(new Font("Courier New", Font.PLAIN, 14));
        
        labelM3v = new JLabel("0");
        labelM3v.setBounds(91, 46, 57, 14);
        panel_9.add(labelM3v);
        labelM3v.setFont(new Font("Courier New", Font.PLAIN, 14));
        
        JLabel lblM4 = new JLabel("M4 Speed:");
        lblM4.setBounds(10, 61, 104, 14);
        panel_9.add(lblM4);
        lblM4.setFont(new Font("Courier New", Font.PLAIN, 14));
        
        labelM4v = new JLabel("0");
        labelM4v.setBounds(91, 61, 57, 14);
        panel_9.add(labelM4v);
        labelM4v.setFont(new Font("Courier New", Font.PLAIN, 14));
        
        JLabel lblDiffm = new JLabel("DiffM1:");
        lblDiffm.setBounds(10, 82, 104, 14);
        panel_9.add(lblDiffm);
        lblDiffm.setFont(new Font("Courier New", Font.PLAIN, 14));
        
        labelDiffM1 = new JLabel("0");
        labelDiffM1.setBounds(91, 82, 57, 14);
        panel_9.add(labelDiffM1);
        labelDiffM1.setFont(new Font("Courier New", Font.PLAIN, 14));
        
        JLabel lblDiffm_1 = new JLabel("DiffM2:");
        lblDiffm_1.setBounds(10, 96, 104, 14);
        panel_9.add(lblDiffm_1);
        lblDiffm_1.setFont(new Font("Courier New", Font.PLAIN, 14));
        
        labelDiffM2 = new JLabel("0");
        labelDiffM2.setBounds(91, 96, 57, 14);
        panel_9.add(labelDiffM2);
        labelDiffM2.setFont(new Font("Courier New", Font.PLAIN, 14));
        
        JLabel lblDiffm_2 = new JLabel("DiffM3:");
        lblDiffm_2.setBounds(10, 111, 104, 14);
        panel_9.add(lblDiffm_2);
        lblDiffm_2.setFont(new Font("Courier New", Font.PLAIN, 14));
        
        labelDiffM3 = new JLabel("0");
        labelDiffM3.setBounds(91, 111, 57, 14);
        panel_9.add(labelDiffM3);
        labelDiffM3.setFont(new Font("Courier New", Font.PLAIN, 14));
        
        JLabel lblDiffm_3 = new JLabel("DiffM4:");
        lblDiffm_3.setBounds(10, 126, 104, 14);
        panel_9.add(lblDiffm_3);
        lblDiffm_3.setFont(new Font("Courier New", Font.PLAIN, 14));
        
        labelDiffM4 = new JLabel("0");
        labelDiffM4.setBounds(91, 126, 57, 14);
        panel_9.add(labelDiffM4);
        labelDiffM4.setFont(new Font("Courier New", Font.PLAIN, 14));
        
        panel_10 = new JPanel();
        panel_10.setBorder(new TitledBorder(null, "Angles", TitledBorder.LEADING, TitledBorder.TOP, null, null));
        panel_10.setBounds(10, 170, 212, 98);
        panel_1.add(panel_10);
        panel_10.setLayout(null);
        
        lblRoll = new JLabel("Roll:");
        lblRoll.setBounds(10, 34, 104, 14);
        panel_10.add(lblRoll);
        lblRoll.setFont(new Font("Courier New", Font.PLAIN, 14));
        
        lblPitch = new JLabel("Pitch:");
        lblPitch.setBounds(10, 19, 104, 14);
        panel_10.add(lblPitch);
        lblPitch.setFont(new Font("Courier New", Font.PLAIN, 14));
        
        labelPitch = new JLabel("0");
        labelPitch.setBounds(66, 19, 136, 14);
        panel_10.add(labelPitch);
        labelPitch.setFont(new Font("Courier New", Font.PLAIN, 14));
        
        labelRoll = new JLabel("0");
        labelRoll.setBounds(66, 34, 136, 14);
        panel_10.add(labelRoll);
        labelRoll.setFont(new Font("Courier New", Font.PLAIN, 14));
        
        JLabel label_7 = new JLabel("LPF - Alpha:");
        label_7.setBounds(10, 56, 111, 14);
        panel_10.add(label_7);
        label_7.setFont(new Font("Courier New", Font.PLAIN, 14));
        
        sliderAlphaDeg = new JSlider();
        sliderAlphaDeg.setBounds(10, 71, 134, 23);
        panel_10.add(sliderAlphaDeg);
        
        lblAlphaDeg = new JLabel("0");
        lblAlphaDeg.setBounds(154, 75, 48, 14);
        panel_10.add(lblAlphaDeg);
        lblAlphaDeg.setFont(new Font("Courier New", Font.PLAIN, 14));
        sliderAlphaDeg.addMouseListener(new MouseListener(){

			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				MainAction.arduino.sendAlpha(0);
				
			}
        	
        });
        
        panel_8 = new JPanel();
        panel_8.setBorder(new TitledBorder(null, "Accel Readings", TitledBorder.LEADING, TitledBorder.TOP, null, null));
        panel_8.setBounds(232, 19, 158, 74);
        panel_1.add(panel_8);
        panel_8.setLayout(null);
        
        JLabel lblX = new JLabel("X:");
        lblX.setBounds(10, 20, 104, 14);
        panel_8.add(lblX);
        lblX.setFont(new Font("Courier New", Font.PLAIN, 14));
        
        labelX = new JLabel("0");
        labelX.setBounds(61, 20, 87, 14);
        panel_8.add(labelX);
        labelX.setFont(new Font("Courier New", Font.PLAIN, 14));
        
        JLabel lblY = new JLabel("Y:");
        lblY.setBounds(10, 34, 104, 14);
        panel_8.add(lblY);
        lblY.setFont(new Font("Courier New", Font.PLAIN, 14));
        
        labelY = new JLabel("0");
        labelY.setBounds(61, 34, 87, 14);
        panel_8.add(labelY);
        labelY.setFont(new Font("Courier New", Font.PLAIN, 14));
        
        JLabel lblZ = new JLabel("Z:");
        lblZ.setBounds(10, 49, 104, 14);
        panel_8.add(lblZ);
        lblZ.setFont(new Font("Courier New", Font.PLAIN, 14));
        
        labelZ = new JLabel("0");
        labelZ.setBounds(61, 49, 87, 14);
        panel_8.add(labelZ);
        labelZ.setFont(new Font("Courier New", Font.PLAIN, 14));
        
        JPanel panel_3 = new JPanel();
        panel_3.setBorder(new TitledBorder(null, "Roll PID", TitledBorder.LEADING, TitledBorder.TOP, null, null));
        panel_3.setBounds(10, 444, 212, 93);
        panel_1.add(panel_3);
        panel_3.setLayout(null);
        
        JLabel lblP1 = new JLabel("P:");
        lblP1.setBounds(10, 22, 28, 14);
        panel_3.add(lblP1);
        lblP1.setFont(new Font("Courier New", Font.PLAIN, 14));
        
        sliderP1 = new JSlider();
        sliderP1.setMaximum(255);
        sliderP1.setBounds(28, 19, 123, 23);
        panel_3.add(sliderP1);
        sliderP1.setValue(0);
        sliderP1.setMinorTickSpacing(1);
        sliderP1.addKeyListener(new ArduinoKeyListener());
        sliderP1.addMouseListener(new MouseListener(){

			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				MainAction.arduino.sendPID(1, 0);
				
			}
        	
        });
        
        lblP1v = new JLabel("0");
        lblP1v.setBounds(150, 22, 42, 14);
        panel_3.add(lblP1v);
        lblP1v.setFont(new Font("Courier New", Font.PLAIN, 14));
        
        JLabel lblI1 = new JLabel("I:");
        lblI1.setBounds(10, 45, 28, 14);
        panel_3.add(lblI1);
        lblI1.setFont(new Font("Courier New", Font.PLAIN, 14));
        
        sliderI1 = new JSlider();
        sliderI1.setMaximum(255);
        sliderI1.setBounds(28, 42, 123, 23);
        panel_3.add(sliderI1);
        sliderI1.setValue(0);
        sliderI1.setMinorTickSpacing(1);
        sliderI1.addKeyListener(new ArduinoKeyListener());
        sliderI1.addMouseListener(new MouseListener(){

			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				MainAction.arduino.sendPID(1, 1);
				
			}
        	
        });
        
        lblI1v = new JLabel("0");
        lblI1v.setBounds(150, 45, 42, 14);
        panel_3.add(lblI1v);
        lblI1v.setFont(new Font("Courier New", Font.PLAIN, 14));
        
        JLabel lblD1 = new JLabel("D:");
        lblD1.setBounds(10, 67, 28, 14);
        panel_3.add(lblD1);
        lblD1.setFont(new Font("Courier New", Font.PLAIN, 14));
        
        sliderD1 = new JSlider();
        sliderD1.setMaximum(255);
        sliderD1.setBounds(28, 64, 123, 23);
        panel_3.add(sliderD1);
        sliderD1.setValue(0);
        sliderD1.setMinorTickSpacing(1);
        sliderD1.addKeyListener(new ArduinoKeyListener());
        sliderD1.addMouseListener(new MouseListener(){

			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				MainAction.arduino.sendPID(1, 2);
				
			}
        	
        });
        
        lblD1v = new JLabel("0");
        lblD1v.setBounds(150, 67, 42, 14);
        panel_3.add(lblD1v);
        lblD1v.setFont(new Font("Courier New", Font.PLAIN, 14));
        
        JPanel panel_2 = new JPanel();
        panel_2.setBorder(new TitledBorder(null, "Pitch PID", TitledBorder.LEADING, TitledBorder.TOP, null, null));
        panel_2.setBounds(10, 351, 212, 93);
        panel_1.add(panel_2);
        panel_2.setLayout(null);
        
        JLabel lblP0 = new JLabel("P:");
        lblP0.setBounds(10, 22, 28, 14);
        panel_2.add(lblP0);
        lblP0.setFont(new Font("Courier New", Font.PLAIN, 14));
        
        lblP0v = new JLabel("0");
        lblP0v.setBounds(150, 22, 52, 14);
        panel_2.add(lblP0v);
        lblP0v.setFont(new Font("Courier New", Font.PLAIN, 14));
        
        sliderP0 = new JSlider();
        sliderP0.setMaximum(255);
        sliderP0.setBounds(28, 19, 123, 23);
        panel_2.add(sliderP0);
        sliderP0.setValue(0);
        sliderP0.setMinorTickSpacing(1);
        sliderP0.addKeyListener(new ArduinoKeyListener());
        sliderP0.addMouseListener(new MouseListener(){
        	public void mouseReleased(MouseEvent e){
        		MainAction.arduino.sendPID(0, 0);
        	}

			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
        });
        
        lblI0v = new JLabel("0");
        lblI0v.setBounds(150, 45, 52, 14);
        panel_2.add(lblI0v);
        lblI0v.setFont(new Font("Courier New", Font.PLAIN, 14));
        
        sliderI0 = new JSlider();
        sliderI0.setMaximum(255);
        sliderI0.setBounds(28, 42, 123, 23);
        panel_2.add(sliderI0);
        sliderI0.setValue(0);
        sliderI0.setMinorTickSpacing(1);
        sliderI0.addKeyListener(new ArduinoKeyListener());
        sliderI0.addMouseListener(new MouseListener(){

			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				MainAction.arduino.sendPID(0, 1);
				
			}
        	
        });
        
        JLabel lblI0 = new JLabel("I:");
        lblI0.setBounds(10, 45, 28, 14);
        panel_2.add(lblI0);
        lblI0.setFont(new Font("Courier New", Font.PLAIN, 14));
        
        JLabel lblD0 = new JLabel("D:");
        lblD0.setBounds(10, 67, 28, 14);
        panel_2.add(lblD0);
        lblD0.setFont(new Font("Courier New", Font.PLAIN, 14));
        
        sliderD0 = new JSlider();
        sliderD0.setMaximum(255);
        sliderD0.setBounds(28, 64, 123, 23);
        panel_2.add(sliderD0);
        sliderD0.setValue(0);
        sliderD0.setMinorTickSpacing(1);
        sliderD0.addKeyListener(new ArduinoKeyListener());
        sliderD0.addMouseListener(new MouseListener(){

			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				MainAction.arduino.sendPID(0, 2);
				
			}
        	
        });
        
        lblD0v = new JLabel("0");
        lblD0v.setBounds(150, 67, 52, 14);
        panel_2.add(lblD0v);
        lblD0v.setFont(new Font("Courier New", Font.PLAIN, 14));
        
        panel_5 = new JPanel();
        panel_5.setBorder(new TitledBorder(null, "Accel Options", TitledBorder.LEADING, TitledBorder.TOP, null, null));
        panel_5.setBounds(232, 94, 158, 220);
        panel_1.add(panel_5);
        panel_5.setLayout(null);
        
        lblLpfAlpha = new JLabel("LPF - Alpha:");
        lblLpfAlpha.setBounds(10, 23, 111, 14);
        panel_5.add(lblLpfAlpha);
        lblLpfAlpha.setFont(new Font("Courier New", Font.PLAIN, 14));
        
        sliderAlphaAccel = new JSlider();
        sliderAlphaAccel.setBounds(10, 38, 104, 23);
        panel_5.add(sliderAlphaAccel);
        
        lblAlphaAccel = new JLabel("0");
        lblAlphaAccel.setBounds(116, 42, 36, 14);
        panel_5.add(lblAlphaAccel);
        lblAlphaAccel.setFont(new Font("Courier New", Font.PLAIN, 14));
        
        sliderOffX = new JSlider();
        sliderOffX.setBounds(26, 82, 104, 23);
        panel_5.add(sliderOffX);
        sliderOffX.setValue(0);
        sliderOffX.setMinorTickSpacing(1);
        sliderOffX.setMaximum(75);
        sliderOffX.setMinimum(-75);
        sliderOffX.addKeyListener(new ArduinoKeyListener());
        
        sliderOffY = new JSlider();
        sliderOffY.setBounds(26, 105, 104, 23);
        panel_5.add(sliderOffY);
        sliderOffY.setMinorTickSpacing(1);
        sliderOffY.setMaximum(75);
        sliderOffY.setMinimum(-75);
        sliderOffY.setValue(0);
        sliderOffY.addKeyListener(new ArduinoKeyListener());
        
        sliderOffZ = new JSlider();
        sliderOffZ.setBounds(26, 128, 104, 23);
        panel_5.add(sliderOffZ);
        sliderOffZ.setValue(0);
        sliderOffZ.setMinorTickSpacing(1);
        sliderOffZ.setMinimum(-75);
        sliderOffZ.setMaximum(75);
        sliderOffZ.addKeyListener(new ArduinoKeyListener());
        
        JLabel label_2 = new JLabel("X:");
        label_2.setBounds(10, 86, 30, 14);
        panel_5.add(label_2);
        label_2.setFont(new Font("Courier New", Font.PLAIN, 14));
        
        JLabel label_3 = new JLabel("Y:");
        label_3.setBounds(10, 109, 30, 14);
        panel_5.add(label_3);
        label_3.setFont(new Font("Courier New", Font.PLAIN, 14));
        
        JLabel label_4 = new JLabel("Z:");
        label_4.setBounds(10, 132, 30, 14);
        panel_5.add(label_4);
        label_4.setFont(new Font("Courier New", Font.PLAIN, 14));
        
        JLabel lblAccelOffsets = new JLabel("Offsets:");
        lblAccelOffsets.setBounds(10, 67, 158, 14);
        panel_5.add(lblAccelOffsets);
        lblAccelOffsets.setFont(new Font("Courier New", Font.PLAIN, 14));
        
        lblOffX = new JLabel("0");
        lblOffX.setBounds(130, 86, 36, 14);
        panel_5.add(lblOffX);
        lblOffX.setFont(new Font("Courier New", Font.PLAIN, 14));
        
        lblOffY = new JLabel("0");
        lblOffY.setBounds(130, 109, 36, 14);
        panel_5.add(lblOffY);
        lblOffY.setFont(new Font("Courier New", Font.PLAIN, 14));
        
        lblOffZ = new JLabel("0");
        lblOffZ.setBounds(130, 132, 36, 14);
        panel_5.add(lblOffZ);
        lblOffZ.setFont(new Font("Courier New", Font.PLAIN, 14));
        
        btnDefaultOffsets = new JButton("Default Offsets");
        btnDefaultOffsets.setBounds(16, 157, 129, 23);
        panel_5.add(btnDefaultOffsets);
        
        btnSetGround = new JButton("Set Ground Level");
        btnSetGround.setFont(new Font("Tahoma", Font.PLAIN, 11));
        btnSetGround.setBounds(16, 186, 129, 23);
        panel_5.add(btnSetGround);
        btnSetGround.addActionListener(new ActionListener(){
        	public void actionPerformed(ActionEvent arg0){
        		MainAction.arduino.sendGND();
        	}
        });
        btnDefaultOffsets.addActionListener(new ActionListener(){
        	public void actionPerformed(ActionEvent arg0){
        		MainAction.arduino.sendOffsets(-1);
        	}
        });
        btnDefaultOffsets.addKeyListener(new ArduinoKeyListener());
        sliderOffZ.addMouseListener(new MouseListener(){

			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				MainAction.arduino.sendOffsets(2);
				
			}
        	
        });
        sliderOffY.addMouseListener(new MouseListener(){

			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				MainAction.arduino.sendOffsets(1);
				
			}
        	
        });
        sliderOffX.addMouseListener(new MouseListener(){

			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				MainAction.arduino.sendOffsets(0);
				
			}
        	
        });
        sliderAlphaAccel.addKeyListener(new ArduinoKeyListener());
        sliderAlphaAccel.addMouseListener(new MouseListener(){

			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				MainAction.arduino.sendAlpha(1);
				
			}
        	
        });
        
        panel_4 = new JPanel();
        panel_4.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Gyroscope Readings", TitledBorder.LEADING, TitledBorder.TOP, null, null));
        panel_4.setBounds(402, 19, 158, 59);
        panel_1.add(panel_4);
        panel_4.setLayout(null);
        
        JLabel label_deltaPitch = new JLabel("\u0394Pitch:");
        label_deltaPitch.setBounds(10, 21, 104, 14);
        panel_4.add(label_deltaPitch);
        label_deltaPitch.setFont(new Font("Courier New", Font.PLAIN, 14));
        
        lblDeltaPitch = new JLabel("0");
        lblDeltaPitch.setBounds(73, 21, 86, 14);
        panel_4.add(lblDeltaPitch);
        lblDeltaPitch.setFont(new Font("Courier New", Font.PLAIN, 14));
        
        JLabel label_deltaRoll = new JLabel("\u0394Roll:");
        label_deltaRoll.setBounds(10, 35, 104, 14);
        panel_4.add(label_deltaRoll);
        label_deltaRoll.setFont(new Font("Courier New", Font.PLAIN, 14));
        
        lblDeltaRoll = new JLabel("0");
        lblDeltaRoll.setBounds(73, 35, 86, 14);
        panel_4.add(lblDeltaRoll);
        lblDeltaRoll.setFont(new Font("Courier New", Font.PLAIN, 14));
        
        panel_11 = new JPanel();
        panel_11.setBorder(new TitledBorder(null, "PID Options", TitledBorder.LEADING, TitledBorder.TOP, null, null));
        panel_11.setBounds(232, 351, 158, 186);
        panel_1.add(panel_11);
        panel_11.setLayout(null);
        
        JLabel lblLevel = new JLabel("0L Pitch:");
        lblLevel.setBounds(10, 121, 129, 14);
        panel_11.add(lblLevel);
        lblLevel.setFont(new Font("Courier New", Font.PLAIN, 14));
        
        lbl0Pitch = new JLabel("0");
        lbl0Pitch.setBounds(88, 121, 58, 14);
        panel_11.add(lbl0Pitch);
        lbl0Pitch.setFont(new Font("Courier New", Font.PLAIN, 14));
        
        lbl0Roll = new JLabel("0");
        lbl0Roll.setBounds(88, 135, 58, 14);
        panel_11.add(lbl0Roll);
        lbl0Roll.setFont(new Font("Courier New", Font.PLAIN, 14));
        
        JLabel lbllRoll = new JLabel("0L Roll:");
        lbllRoll.setBounds(10, 135, 127, 14);
        panel_11.add(lbllRoll);
        lbllRoll.setFont(new Font("Courier New", Font.PLAIN, 14));
        
        JLabel lblPid = new JLabel("PID:");
        lblPid.setBounds(10, 21, 104, 14);
        panel_11.add(lblPid);
        lblPid.setFont(new Font("Courier New", Font.PLAIN, 14));
        
        lblPID = new JLabel("0");
        lblPID.setBounds(53, 21, 68, 14);
        panel_11.add(lblPID);
        lblPID.setFont(new Font("Courier New", Font.PLAIN, 14));
        
        btnSwitchPid = new JButton("Switch PID");
        btnSwitchPid.setBounds(10, 37, 111, 23);
        panel_11.add(btnSwitchPid);
        
        btnDefaultPID = new JButton("Set Initial PID");
        btnDefaultPID.setBounds(10, 71, 111, 23);
        panel_11.add(btnDefaultPID);
        
        btnSetLevel = new JButton("Set 0 Level");
        btnSetLevel.setBounds(10, 152, 129, 23);
        panel_11.add(btnSetLevel);
        btnSetLevel.addActionListener(new ActionListener(){
        	public void actionPerformed(ActionEvent arg0){
        		MainAction.arduino.send0Level();
        	}
        });
        btnSetLevel.addKeyListener(new ArduinoKeyListener());
        btnDefaultPID.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent arg0) {
        		MainAction.arduino.sendPID(-1, -1);
        	}
        });
        btnDefaultPID.addKeyListener(new ArduinoKeyListener());
        btnSwitchPid.addActionListener(new ActionListener(){
        	public void actionPerformed(ActionEvent arg0){
        		MainAction.arduino.sendSwitchPID();
        	}
        });
        btnSwitchPid.addKeyListener(new ArduinoKeyListener());
        
        JPanel panel_7 = new JPanel();
        panel_7.setLayout(null);
        panel_7.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Gyro Options", TitledBorder.LEADING, TitledBorder.TOP, null, null));
        panel_7.setBounds(402, 78, 158, 149);
        panel_1.add(panel_7);
        
        JLabel label_6 = new JLabel("LPF - Alpha:");
        label_6.setFont(new Font("Courier New", Font.PLAIN, 14));
        label_6.setBounds(10, 23, 111, 14);
        panel_7.add(label_6);
        
        sliderAlphaGyro = new JSlider();
        sliderAlphaGyro.setBounds(10, 38, 104, 23);
        sliderAlphaGyro.addKeyListener(new ArduinoKeyListener());
        sliderAlphaGyro.addMouseListener(new MouseListener(){

			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				MainAction.arduino.sendAlpha(2);
				
			}
        	
        });
        panel_7.add(sliderAlphaGyro);
        
        lblAlphaGyro = new JLabel("0");
        lblAlphaGyro.setFont(new Font("Courier New", Font.PLAIN, 14));
        lblAlphaGyro.setBounds(116, 42, 36, 14);
        panel_7.add(lblAlphaGyro);
        
        btnSetStillLevel = new JButton("Set Still Level");
        btnSetStillLevel.addKeyListener(new ArduinoKeyListener());
        btnSetStillLevel.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent arg0) {
        		MainAction.arduino.sendStillLevel();
        	}
        });
        btnSetStillLevel.setFont(new Font("Tahoma", Font.PLAIN, 11));
        btnSetStillLevel.setBounds(10, 115, 129, 23);
        panel_7.add(btnSetStillLevel);
        
        JLabel lblXMed = new JLabel("X Med:");
        lblXMed.setFont(new Font("Courier New", Font.PLAIN, 14));
        lblXMed.setBounds(10, 72, 104, 14);
        panel_7.add(lblXMed);
        
        lblGyroXMed = new JLabel("0");
        lblGyroXMed.setFont(new Font("Courier New", Font.PLAIN, 14));
        lblGyroXMed.setBounds(73, 72, 86, 14);
        panel_7.add(lblGyroXMed);
        
        JLabel lblZMed = new JLabel("Z Med:");
        lblZMed.setFont(new Font("Courier New", Font.PLAIN, 14));
        lblZMed.setBounds(10, 86, 104, 14);
        panel_7.add(lblZMed);
        
        lblGyroZMed = new JLabel("0");
        lblGyroZMed.setFont(new Font("Courier New", Font.PLAIN, 14));
        lblGyroZMed.setBounds(73, 86, 86, 14);
        panel_7.add(lblGyroZMed);
        
        panel_6 = new JPanel();
        panel_6.setBounds(10, 266, 212, 83);
        panel_1.add(panel_6);
        panel_6.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Motors Testing", TitledBorder.LEADING, TitledBorder.TOP, null, null));
        panel_6.setLayout(null);
        
        btnTestM4 = new JButton("M4");
        btnTestM4.setBounds(10, 36, 57, 23);
        panel_6.add(btnTestM4);
        
        btnTestM1 = new JButton("M1");
        btnTestM1.setBounds(77, 21, 57, 23);
        panel_6.add(btnTestM1);
        
        btnTestM3 = new JButton("M3");
        btnTestM3.setBounds(77, 49, 57, 23);
        panel_6.add(btnTestM3);
        
        btnTestM2 = new JButton("M2");
        btnTestM2.setBounds(145, 36, 57, 23);
        panel_6.add(btnTestM2);
        
        JPanel panel_12 = new JPanel();
        panel_12.setLayout(null);
        panel_12.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Motors Power", TitledBorder.LEADING, TitledBorder.TOP, null, null));
        panel_12.setBounds(400, 227, 160, 310);
        panel_1.add(panel_12);
        
        btnSwitchM4 = new JButton("M4");
        btnSwitchM4.setBounds(10, 45, 57, 23);
        btnSwitchM4.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent arg0) {
        		MainAction.arduino.sendSwitchMotor(4);
        	}
        });
        btnSwitchM4.addKeyListener(new ArduinoKeyListener());
        panel_12.add(btnSwitchM4);
        
        btnSwitchM1 = new JButton("M1");
        btnSwitchM1.setBounds(50, 21, 57, 23);
        btnSwitchM1.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent arg0) {
        		MainAction.arduino.sendSwitchMotor(1);
        	}
        });
        btnSwitchM1.addKeyListener(new ArduinoKeyListener());
        panel_12.add(btnSwitchM1);
        
        btnSwitchM3 = new JButton("M3");
        btnSwitchM3.setBounds(50, 69, 57, 23);
        btnSwitchM3.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent arg0) {
        		MainAction.arduino.sendSwitchMotor(3);
        	}
        });
        btnSwitchM3.addKeyListener(new ArduinoKeyListener());
        panel_12.add(btnSwitchM3);
        
        btnSwitchM2 = new JButton("M2");
        btnSwitchM2.setBounds(93, 45, 57, 23);
        btnSwitchM2.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent arg0) {
        		MainAction.arduino.sendSwitchMotor(2);
        	}
        });
        btnSwitchM2.addKeyListener(new ArduinoKeyListener());
        panel_12.add(btnSwitchM2);
        
        panel_13 = new JPanel();
        panel_13.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
        panel_13.setBounds(50, 103, 57, 31);
        panel_12.add(panel_13);
        
        lblM1Pow = new JLabel("OFF");
        lblM1Pow.setForeground(Color.RED);
        panel_13.add(lblM1Pow);
        lblM1Pow.setFont(new Font("Courier New", Font.PLAIN, 14));
        
        JPanel panel_14 = new JPanel();
        panel_14.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
        panel_14.setBounds(10, 136, 57, 31);
        panel_12.add(panel_14);
        
        lblM4Pow = new JLabel("OFF");
        lblM4Pow.setForeground(Color.RED);
        lblM4Pow.setFont(new Font("Courier New", Font.PLAIN, 14));
        panel_14.add(lblM4Pow);
        
        JPanel panel_15 = new JPanel();
        panel_15.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
        panel_15.setBounds(93, 136, 57, 31);
        panel_12.add(panel_15);
        
        lblM2Pow = new JLabel("OFF");
        lblM2Pow.setForeground(Color.RED);
        lblM2Pow.setFont(new Font("Courier New", Font.PLAIN, 14));
        panel_15.add(lblM2Pow);
        
        JPanel panel_16 = new JPanel();
        panel_16.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
        panel_16.setBounds(50, 169, 57, 31);
        panel_12.add(panel_16);
        
        lblM3Pow = new JLabel("OFF");
        lblM3Pow.setForeground(Color.RED);
        lblM3Pow.setFont(new Font("Courier New", Font.PLAIN, 14));
        panel_16.add(lblM3Pow);
        
        sldOffM1 = new JSlider();
        sldOffM1.setValue(0);
        sldOffM1.setMinorTickSpacing(1);
        sldOffM1.setMinimum(-100);
        sldOffM1.setBounds(32, 210, 75, 23);
        sldOffM1.addMouseListener(new MouseListener(){
        	public void mouseReleased(MouseEvent e){
        		MainAction.arduino.sendMotorOffset(1);
        	}

			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
        });
        panel_12.add(sldOffM1);
        
        sldOffM2 = new JSlider();
        sldOffM2.setValue(0);
        sldOffM2.setMinorTickSpacing(1);
        sldOffM2.setMinimum(-100);
        sldOffM2.setBounds(32, 232, 75, 23);
        sldOffM2.addMouseListener(new MouseListener(){
        	public void mouseReleased(MouseEvent e){
        		MainAction.arduino.sendMotorOffset(2);
        	}

			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
        });
        panel_12.add(sldOffM2);
        
        sldOffM3 = new JSlider();
        sldOffM3.setValue(0);
        sldOffM3.setMinorTickSpacing(1);
        sldOffM3.setMinimum(-100);
        sldOffM3.setBounds(32, 254, 75, 23);
        sldOffM3.addMouseListener(new MouseListener(){
        	public void mouseReleased(MouseEvent e){
        		MainAction.arduino.sendMotorOffset(3);
        	}

			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
        });
        panel_12.add(sldOffM3);
        
        sldOffM4 = new JSlider();
        sldOffM4.setValue(0);
        sldOffM4.setMinorTickSpacing(1);
        sldOffM4.setMinimum(-100);
        sldOffM4.setBounds(32, 276, 75, 23);
        sldOffM4.addMouseListener(new MouseListener(){
        	public void mouseReleased(MouseEvent e){
        		MainAction.arduino.sendMotorOffset(4);
        	}

			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
        });
        panel_12.add(sldOffM4);
        
        lblOffM1 = new JLabel("0");
        lblOffM1.setFont(new Font("Courier New", Font.PLAIN, 14));
        lblOffM1.setBounds(106, 214, 44, 14);
        panel_12.add(lblOffM1);
        
        lblOffM2 = new JLabel("0");
        lblOffM2.setFont(new Font("Courier New", Font.PLAIN, 14));
        lblOffM2.setBounds(106, 236, 44, 14);
        panel_12.add(lblOffM2);
        
        lblOffM3 = new JLabel("0");
        lblOffM3.setFont(new Font("Courier New", Font.PLAIN, 14));
        lblOffM3.setBounds(106, 258, 44, 14);
        panel_12.add(lblOffM3);
        
        lblOffM4 = new JLabel("0");
        lblOffM4.setFont(new Font("Courier New", Font.PLAIN, 14));
        lblOffM4.setBounds(106, 280, 44, 14);
        panel_12.add(lblOffM4);
        
        JLabel lblM = new JLabel("M1:");
        lblM.setFont(new Font("Courier New", Font.PLAIN, 14));
        lblM.setBounds(10, 214, 30, 14);
        panel_12.add(lblM);
        
        JLabel lblM_3 = new JLabel("M2:");
        lblM_3.setFont(new Font("Courier New", Font.PLAIN, 14));
        lblM_3.setBounds(10, 236, 30, 14);
        panel_12.add(lblM_3);
        
        JLabel lblM_2 = new JLabel("M3:");
        lblM_2.setFont(new Font("Courier New", Font.PLAIN, 14));
        lblM_2.setBounds(10, 258, 30, 14);
        panel_12.add(lblM_2);
        
        JLabel lblM_1 = new JLabel("M4:");
        lblM_1.setFont(new Font("Courier New", Font.PLAIN, 14));
        lblM_1.setBounds(10, 280, 30, 14);
        panel_12.add(lblM_1);
        
        btnTestM2.addActionListener(new ActionListener(){
        	public void actionPerformed(ActionEvent arg0){
        		MainAction.arduino.sendTestM(2);
        	}
        });
        btnTestM3.addActionListener(new ActionListener(){
        	public void actionPerformed(ActionEvent arg0){
        		MainAction.arduino.sendTestM(3);
        	}
        });
        btnTestM1.addActionListener(new ActionListener(){
        	public void actionPerformed(ActionEvent arg0){
        		MainAction.arduino.sendTestM(1);
        	}
        });
        btnTestM4.addActionListener(new ActionListener(){
        	public void actionPerformed(ActionEvent arg0){
        		MainAction.arduino.sendTestM(4);
        	}
        });
        
        lblTextModeOn.setVisible(false);
        setVisible(true);
    }
    
    public void setInitial(){
    	slider.setEnabled(false);
    	sliderLight.setEnabled(false);
    	textField.setEnabled(false);
    	btnSendText.setEnabled(false);
    	btnNewButton.setEnabled(false);
    	
    	btnTestM1.setEnabled(false);
    	btnTestM2.setEnabled(false);
    	btnTestM3.setEnabled(false);
    	btnTestM4.setEnabled(false);
    	btnSwitchM1.setEnabled(false);
    	btnSwitchM2.setEnabled(false);
    	btnSwitchM3.setEnabled(false);
    	btnSwitchM4.setEnabled(false);
    	
    	txtCom.setEnabled(true);
    	button.setEnabled(true);   
    	
    	sliderOffX.setEnabled(false);
    	sliderOffY.setEnabled(false);
    	sliderOffZ.setEnabled(false);
    	sliderAlphaAccel.setEnabled(false);
    	sliderAlphaGyro.setEnabled(false);
    	btnDefaultOffsets.setEnabled(false);
    	btnSetLevel.setEnabled(false);
    	btnSetGround.setEnabled(false);
    	btnSetStillLevel.setEnabled(false);
    	sliderAlphaDeg.setEnabled(false);
		sldOffM1.setEnabled(false);
		sldOffM2.setEnabled(false);
		sldOffM3.setEnabled(false);
		sldOffM4.setEnabled(false);
    	
    	labelM1v.setText("N/A");
    	labelM2v.setText("N/A");
    	labelM3v.setText("N/A");
    	labelM4v.setText("N/A");
    	labelPitch.setText("N/A");
    	labelRoll.setText("N/A");
    	labelX.setText("N/A");
    	labelY.setText("N/A");
    	labelZ.setText("N/A");
    	labelDiffM1.setText("N/A");
    	labelDiffM2.setText("N/A");
    	labelDiffM3.setText("N/A");
    	labelDiffM4.setText("N/A");
    	lblDeltaPitch.setText("N/A");
    	lblDeltaRoll.setText("N/A");
    	lblGyroXMed.setText("N/A");
    	lblGyroZMed.setText("N/A");
    	
    	lblP0v.setText("N/A");
    	lblI0v.setText("N/A");
    	lblD0v.setText("N/A");
    	lblP1v.setText("N/A");
    	lblI1v.setText("N/A");
    	lblD1v.setText("N/A");
    	
    	lblPID.setText("N/A");
    	
    	lblOffM1.setText("N/A");
    	lblOffM2.setText("N/A");
    	lblOffM3.setText("N/A");
    	lblOffM4.setText("N/A");
    	
    	disablePIDCommand();
	}
    
    public void setConnected(){
    	slider.setEnabled(true);
    	sliderLight.setEnabled(true);
    	textField.setEnabled(true);
    	btnSendText.setEnabled(true);
    	btnNewButton.setEnabled(true);
    	btnSetGround.setEnabled(true);
    	sliderAlphaDeg.setEnabled(true);
    	
    	btnTestM1.setEnabled(true);
    	btnTestM2.setEnabled(true);
    	btnTestM3.setEnabled(true);
    	btnTestM4.setEnabled(true);
    	
    	btnSwitchM1.setEnabled(true);
    	btnSwitchM2.setEnabled(true);
    	btnSwitchM3.setEnabled(true);
    	btnSwitchM4.setEnabled(true);
    	
    	sldOffM1.setEnabled(true);
		sldOffM2.setEnabled(true);
		sldOffM3.setEnabled(true);
		sldOffM4.setEnabled(true);
    	
    	enablePIDCommand();
    	
    	txtCom.setEnabled(false);
    	button.setEnabled(false); 
    	slider.requestFocus();    
    	clearTelemetry();
    }
    
    public void print(String s){
    	MainAction.window1.textArea.append(s);
		MainAction.window1.textArea.setCaretPosition(MainAction.window1.textArea.getDocument().getLength());	
    }
    
    public void println(String s){
    	MainAction.window1.textArea.append(s);
    	MainAction.window1.textArea.append(Character.toString('\n'));
		MainAction.window1.textArea.setCaretPosition(MainAction.window1.textArea.getDocument().getLength());
    }
    
    public void clearTelemetry(){
    	labelM1v.setText("0");
    	labelM2v.setText("0");
    	labelM3v.setText("0");
    	labelM4v.setText("0");
    	labelPitch.setText("0");
    	labelRoll.setText("0");
    	labelX.setText("0");
    	labelY.setText("0");
    	labelZ.setText("0");
    	labelDiffM1.setText("0");
    	labelDiffM2.setText("0");
    	labelDiffM3.setText("0");
    	labelDiffM4.setText("0");
    	lblDeltaPitch.setText("0");
    	lblDeltaRoll.setText("0");
    	
    	lblP0v.setText("0");
    	lblI0v.setText("0");
    	lblD0v.setText("0");
    	lblP1v.setText("0");
    	lblI1v.setText("0");
    	lblD1v.setText("0");
    	
    	lblOffX.setText("0");
    	lblOffY.setText("0");
    	lblOffZ.setText("0");
    	lbl0Pitch.setText("0");
    	lbl0Roll.setText("0");
    }
    
    public void disablePIDCommand(){
    	sliderP0.setEnabled(false);
    	sliderI0.setEnabled(false);
    	sliderD0.setEnabled(false);
    	sliderP1.setEnabled(false);
    	sliderI1.setEnabled(false);
    	sliderD1.setEnabled(false);
    	btnDefaultPID.setEnabled(false);
    	btnSwitchPid.setEnabled(false);
    }
    
    public void enablePIDCommand(){
    	sliderP0.setEnabled(true);
    	sliderI0.setEnabled(true);
    	sliderD0.setEnabled(true);
    	sliderP1.setEnabled(true);
    	sliderI1.setEnabled(true);
    	sliderD1.setEnabled(true);
    	btnDefaultPID.setEnabled(true);
    	btnSwitchPid.setEnabled(true);
    }
    
    public void setOffsets(int x, int y, int z){
    	lblOffX.setText(Integer.toString(x));
    	lblOffY.setText(Integer.toString(y));
    	lblOffZ.setText(Integer.toString(z));
    }
}

