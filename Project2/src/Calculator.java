import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;


public class Calculator extends JFrame implements ActionListener {
	private static final long serialVersionUID = 1L;
	static Stack<Character> operators = new Stack<>();
	static Stack<Long> operands = new Stack<>();
	static LinkedList<String> expression = new LinkedList<>();
	static String currentEntry = "0";
	final static String hex = "  HEX  ";
	final static String dec = "  DEC  ";
	final static String oct = "  OCT  ";
	final static String bin = "  BIN  ";
	JTextField inputField;
	JTextField expressionField;
	JButton[] numberButtons = new JButton[16];
	JButton[] operatorButtons = new JButton[10];//lsh, rsh, '%', '/', '*', '-', '+',  '(', ')', '='
	//not calling the invert button an operator because I will have it do a separate function
	JButton[] baseButtons = new JButton[4];//hex, dec, oct, bin
	
	
	//JButton[] bitLogicButtons = new JButton[4];//or, xor, not, and
	//I missed the part where it says this isn't necessary
	JButton negativeButton;
	JButton clearEntryButton;
	JButton clearButton;
	JButton backButton;
	JButton shiftButton;
	JButton debug;
	boolean shift = false;
	boolean temp = true;
	boolean noNum = false;
	boolean needsParen = false;
	int openParens = 0;
	int baseMode = 1;
	String[] baseStrings = new String[4];
	String activeString = "0";
	//Bits b = new Bits();
	long decValue = 0;
	
	public void init() {
		
		baseStrings[0] = "0";
		baseStrings[1] = "0";
		baseStrings[2] = "0";
		baseStrings[3] = "0";
		
		
		Color lightButton = new Color(250,250,250);
		Color darkButton = new Color(240,240,240);
		Color backgroundColor = new Color(230,230,230);
		Font numButtonFont = new Font("Segoe UI", Font.BOLD, 16);
		Font plainTextFont = new Font("Segoe UI", Font.PLAIN, 14);
		Font largePlainTextFont = new Font("Segoe UI", Font.PLAIN, 18);
		Font boldTextFont = new Font("Segoe UI", Font.PLAIN, 14);
		Font uniSmallFont = new Font("DejaVu Sans", Font.PLAIN, 14); 
		Font uniLargeFont = new Font("DejaVu Sans", Font.PLAIN, 18); 
		
		Dimension defaultSize = new Dimension(500,800);
		setSize(defaultSize);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(defaultSize);
		setLayout(new GridBagLayout());
		setBackground(backgroundColor);
		getRootPane().setBorder(BorderFactory.createMatteBorder(4, 4, 4, 4, backgroundColor));
		this.getContentPane().setBackground(backgroundColor);
		
//------Universal constraints for the GridBagLayout
		GridBagConstraints c = new GridBagConstraints();
		c.weightx = .165;
		c.weighty = 0;
		c.insets = new Insets(2, 2, 2, 2);

//------Constraints applicable only to the area above the main set of buttons
		c.fill = GridBagConstraints.HORIZONTAL;
		
//------Adding in all components, headed by positions then constraints, left to right, top down
		c.gridx = 0;
		c.gridy = 0;
		debug = new JButton("\u2630");
		debug.setBackground(backgroundColor);
		Font font = new Font("DejaVu Sans", Font.PLAIN, 24);
		debug.setFont(font);
		debug.setBorder(javax.swing.BorderFactory.createEmptyBorder());
		add(debug, c);
		
		c.gridx++;
		c.ipadx = 25;
		c.gridwidth = 5;
		font = new Font("Segoe UI", Font.PLAIN, 22);
		JTextField programmer = new JTextField("  Programmer");
		programmer.setBackground(backgroundColor);
		programmer.setFont(font);
		programmer.setEditable(false);
		programmer.setBorder(javax.swing.BorderFactory.createEmptyBorder());
		add(programmer, c);
		
//------Section for adding all of the full-width, info-displaying components
		c.ipady = 10;
		c.gridwidth = 6;
		c.gridy++;
		c.gridx = 0;
		font = new Font("Segoe UI", Font.PLAIN, 20);
		expressionField = new JTextField();
		expressionField.setBackground(backgroundColor);
		expressionField.setHorizontalAlignment(SwingConstants.RIGHT);
		expressionField.setFont(font);
		expressionField.setEditable(false);
		expressionField.setBorder(javax.swing.BorderFactory.createEmptyBorder());
		add(expressionField, c);

		c.gridy++;
		font = new Font("Segoe UI", Font.BOLD, 28);
		inputField = new JTextField("0");
		inputField.setBackground(backgroundColor);
		inputField.setHorizontalAlignment(SwingConstants.RIGHT);
		inputField.setFont(font);
		inputField.setEditable(false);
		inputField.setBorder(javax.swing.BorderFactory.createEmptyBorder());
		add(inputField, c);

		c.ipady = 5;
		c.gridy++;
		font = new Font("Segoe UI", Font.BOLD, 14);
		JButton hexButton = new JButton(hex + "0");
		baseButtons[0] = hexButton;
		hexButton.setBackground(backgroundColor);
		hexButton.setHorizontalAlignment(SwingConstants.LEFT);
		hexButton.setFont(font);
		hexButton.setBorder(javax.swing.BorderFactory.createEmptyBorder());
		add(hexButton, c);

		c.gridy++;
		JButton decButton = new JButton(dec + "0");
		baseButtons[1] = decButton;
		decButton.setBackground(backgroundColor);
		decButton.setHorizontalAlignment(SwingConstants.LEFT);
		decButton.setFont(font);
		decButton.setBorder(javax.swing.BorderFactory.createEmptyBorder());
		add(decButton, c);
		
		c.gridy++;
		JButton octButton = new JButton(oct + "0");
		baseButtons[2] = octButton;
		octButton.setBackground(backgroundColor);
		octButton.setHorizontalAlignment(SwingConstants.LEFT);
		octButton.setFont(font);
		octButton.setBorder(javax.swing.BorderFactory.createEmptyBorder());
		add(octButton, c);
		
		c.gridy++;
		JButton binButton = new JButton(bin + "0");
		baseButtons[3] = binButton;
		binButton.setBackground(backgroundColor);
		binButton.setHorizontalAlignment(SwingConstants.LEFT);
		binButton.setFont(font);
		binButton.setBorder(javax.swing.BorderFactory.createEmptyBorder());
		add(binButton, c);
		
		c.gridy++;
		c.ipady = 15;
		c.gridy++;
		
//marks start of lower buttons
//------Line break to make finding start of next row easier----------------------	
//------Line break to make finding start of next row easier----------------------		

		c.weighty = .076;
		c.fill = GridBagConstraints.BOTH;
		c.gridwidth = 1;
		c.ipadx = 25;
		
		c.gridy++;
		c.gridx = 0;
		JButton lshButton = new JButton("Lsh");
		operatorButtons[0] = lshButton;
		lshButton.setBackground(darkButton);
		lshButton.setFont(plainTextFont);
		lshButton.setBorder(javax.swing.BorderFactory.createEmptyBorder());
		add(lshButton, c);

		c.gridx++;
		JButton rshButton = new JButton("Rsh");
		operatorButtons[1] = rshButton;
		rshButton.setBackground(darkButton);
		rshButton.setFont(plainTextFont);
		rshButton.setBorder(javax.swing.BorderFactory.createEmptyBorder());
		add(rshButton, c);
		
		c.gridx++;
		JButton orButton = new JButton("Or");
		//bitLogicButtons[0] = orButton;
		orButton.setBackground(darkButton);
		orButton.setFont(plainTextFont);
		orButton.setBorder(javax.swing.BorderFactory.createEmptyBorder());
		add(orButton, c);

		c.gridx++;
		JButton xorButton = new JButton("Xor");
		//bitLogicButtons[1] = xorButton;
		xorButton.setBackground(darkButton);
		xorButton.setFont(plainTextFont);
		xorButton.setBorder(javax.swing.BorderFactory.createEmptyBorder());
		add(xorButton, c);

		c.gridx++;
		JButton notButton = new JButton("Not");
		//bitLogicButtons[2] = notButton;
		notButton.setBackground(darkButton);
		notButton.setFont(plainTextFont);
		notButton.setBorder(javax.swing.BorderFactory.createEmptyBorder());
		add(notButton, c);

		c.gridx++;
		JButton andButton = new JButton("And");
		//bitLogicButtons[3] = andButton;
		andButton.setBackground(darkButton);
		andButton.setFont(plainTextFont);
		andButton.setBorder(javax.swing.BorderFactory.createEmptyBorder());
		add(andButton, c);

//-----------Line break to make finding start of next row easier----------------------	
//-----------Line break to make finding start of next row easier----------------------		

		c.gridy++;
		c.gridx = 0;
		shiftButton = new JButton("\u2191");
		shiftButton.setBackground(darkButton);
		shiftButton.setHorizontalAlignment(SwingConstants.CENTER);
		shiftButton.setFont(uniLargeFont);
		shiftButton.setBorder(javax.swing.BorderFactory.createEmptyBorder());
		add(shiftButton, c);
		
		c.gridx++;
		JButton modButton = new JButton("Mod");
		operatorButtons[2] = modButton;
		modButton.setBackground(darkButton);
		modButton.setFont(plainTextFont);
		modButton.setBorder(javax.swing.BorderFactory.createEmptyBorder());
		add(modButton, c);
		
		c.gridx++;
		clearEntryButton = new JButton("CE");
		clearEntryButton.setBackground(darkButton);
		clearEntryButton.setFont(boldTextFont);
		clearEntryButton.setBorder(javax.swing.BorderFactory.createEmptyBorder());
		add(clearEntryButton, c);
		
		c.gridx++;
		clearButton = new JButton("C");
		
		clearButton.setBackground(darkButton);
		clearButton.setFont(boldTextFont);
		clearButton.setBorder(javax.swing.BorderFactory.createEmptyBorder());
		add(clearButton, c);
		
		c.gridx++;
		backButton = new JButton("\u232B");
		backButton.setBackground(darkButton);
		backButton.setHorizontalAlignment(SwingConstants.CENTER);
		backButton.setFont(uniSmallFont);
		backButton.setBorder(javax.swing.BorderFactory.createEmptyBorder());
		add(backButton, c);
		
		c.gridx++;
		JButton divideButton = new JButton("\u00F7");
		operatorButtons[3] = divideButton;
		divideButton.setBackground(darkButton);
		divideButton.setHorizontalAlignment(SwingConstants.CENTER);
		divideButton.setFont(uniLargeFont);
		divideButton.setBorder(javax.swing.BorderFactory.createEmptyBorder());
		add(divideButton, c);

//-----------Line break to make finding start of next row easier----------------------	
//-----------Line break to make finding start of next row easier----------------------		
		
		c.gridy++;
		c.gridx = 0;
		JButton aButton = new JButton("A");
		numberButtons[10] = aButton;
		aButton.setBackground(lightButton);
		aButton.setFont(boldTextFont);
		aButton.setBorder(javax.swing.BorderFactory.createEmptyBorder());
		add(aButton, c);

		c.gridx++;
		JButton bButton = new JButton("B");
		numberButtons[11] = bButton;
		bButton.setBackground(lightButton);
		bButton.setFont(boldTextFont);
		bButton.setBorder(javax.swing.BorderFactory.createEmptyBorder());
		add(bButton, c);		
		
		c.gridx++;
		JButton sevenButton = new JButton("7");
		numberButtons[7] = sevenButton;
		sevenButton.setBackground(lightButton);
		sevenButton.setFont(numButtonFont);
		sevenButton.setBorder(javax.swing.BorderFactory.createEmptyBorder());
		add(sevenButton, c);

		c.gridx++;
		JButton eightButton = new JButton("8");
		numberButtons[8] = eightButton;
		eightButton.setBackground(lightButton);
		eightButton.setFont(numButtonFont);
		eightButton.setBorder(javax.swing.BorderFactory.createEmptyBorder());
		add(eightButton, c);

		c.gridx++;
		JButton nineButton = new JButton("9");
		numberButtons[9] = nineButton;
		nineButton.setBackground(lightButton);
		nineButton.setFont(numButtonFont);
		nineButton.setBorder(javax.swing.BorderFactory.createEmptyBorder());
		add(nineButton, c);

		c.gridx++;
		JButton multiplyButton = new JButton("\u2573");
		operatorButtons[4] = multiplyButton;
		multiplyButton.setBackground(darkButton);
		multiplyButton.setHorizontalAlignment(SwingConstants.CENTER);
		multiplyButton.setFont(uniSmallFont);
		multiplyButton.setBorder(javax.swing.BorderFactory.createEmptyBorder());
		add(multiplyButton, c);

//-----------Line break to make finding start of next row easier----------------------	
//-----------Line break to make finding start of next row easier----------------------			
		
		c.gridy++;
		c.gridx = 0;
		JButton cButton = new JButton("C");
		numberButtons[12] = cButton;
		cButton.setBackground(lightButton);
		cButton.setFont(boldTextFont);
		cButton.setBorder(javax.swing.BorderFactory.createEmptyBorder());
		add(cButton, c);
		
		c.gridx++;
		JButton dButton = new JButton("D");
		numberButtons[13] = dButton;
		dButton.setBackground(lightButton);
		dButton.setFont(boldTextFont);
		dButton.setBorder(javax.swing.BorderFactory.createEmptyBorder());
		add(dButton, c);
		
		c.gridx++;
		JButton fourButton = new JButton("4");
		numberButtons[4] = fourButton;
		fourButton.setBackground(lightButton);
		fourButton.setFont(numButtonFont);
		fourButton.setBorder(javax.swing.BorderFactory.createEmptyBorder());
		add(fourButton, c);

		c.gridx++;
		JButton fiveButton = new JButton("5");
		numberButtons[5] = fiveButton;
		fiveButton.setBackground(lightButton);
		fiveButton.setFont(numButtonFont);
		fiveButton.setBorder(javax.swing.BorderFactory.createEmptyBorder());
		add(fiveButton, c);

		c.gridx++;
		JButton sixButton = new JButton("6");
		numberButtons[6] = sixButton;
		sixButton.setBackground(lightButton);
		sixButton.setFont(numButtonFont);
		sixButton.setBorder(javax.swing.BorderFactory.createEmptyBorder());
		add(sixButton, c);

		c.gridx++;
		JButton subtractButton = new JButton("\uFF0D");
		operatorButtons[5] = subtractButton;
		subtractButton.setBackground(darkButton);
		subtractButton.setHorizontalAlignment(SwingConstants.CENTER);
		subtractButton.setFont(uniLargeFont);
		subtractButton.setBorder(javax.swing.BorderFactory.createEmptyBorder());
		add(subtractButton, c);

//-----------Line break to make finding start of next row easier----------------------	
//-----------Line break to make finding start of next row easier----------------------			
						
		c.gridy++;
		c.gridx = 0;
		JButton eButton = new JButton("E");
		numberButtons[14] = eButton;
		eButton.setBackground(lightButton);
		eButton.setFont(boldTextFont);
		eButton.setBorder(javax.swing.BorderFactory.createEmptyBorder());
		add(eButton, c);	

		c.gridx++;
		JButton fButton = new JButton("F");
		numberButtons[15] = fButton;
		fButton.setBackground(lightButton);
		fButton.setFont(boldTextFont);
		fButton.setBorder(javax.swing.BorderFactory.createEmptyBorder());
		add(fButton, c);

		c.gridx++;
		JButton oneButton = new JButton("1");
		numberButtons[1] = oneButton;
		oneButton.setBackground(lightButton);
		oneButton.setFont(numButtonFont);
		oneButton.setBorder(javax.swing.BorderFactory.createEmptyBorder());
		add(oneButton, c);
		
		c.gridx++;
		JButton twoButton = new JButton("2");
		numberButtons[2] = twoButton;
		twoButton.setBackground(lightButton);
		twoButton.setFont(numButtonFont);
		twoButton.setBorder(javax.swing.BorderFactory.createEmptyBorder());
		add(twoButton, c);
		
		c.gridx++;
		JButton threeButton = new JButton("3");
		numberButtons[3] = threeButton;
		threeButton.setBackground(lightButton);
		threeButton.setFont(numButtonFont);
		threeButton.setBorder(javax.swing.BorderFactory.createEmptyBorder());
		add(threeButton, c);
		
		c.gridx++;
		JButton addButton = new JButton("\uFF0B");
		operatorButtons[6] = addButton;
		addButton.setBackground(darkButton);
		addButton.setFont(uniLargeFont);
		addButton.setBorder(javax.swing.BorderFactory.createEmptyBorder());
		add(addButton, c);

//-----------Line break to make finding start of next row easier----------------------	
//-----------Line break to make finding start of next row easier----------------------			
		
		c.gridy++;
		c.gridx = 0;
		JButton openParenButton = new JButton("(");
		operatorButtons[7] = openParenButton;
		openParenButton.setBackground(darkButton);
		openParenButton.setFont(largePlainTextFont);
		openParenButton.setBorder(javax.swing.BorderFactory.createEmptyBorder());
		add(openParenButton, c);

		c.gridx++;
		JButton closeParenButton = new JButton(")");
		operatorButtons[8] = closeParenButton;
		closeParenButton.setBackground(darkButton);
		closeParenButton.setFont(largePlainTextFont);
		closeParenButton.setBorder(javax.swing.BorderFactory.createEmptyBorder());
		add(closeParenButton, c);

		c.gridx++;
		negativeButton = new JButton("\u00B1");
		negativeButton.setBackground(darkButton);
		negativeButton.setFont(uniLargeFont);
		negativeButton.setBorder(javax.swing.BorderFactory.createEmptyBorder());
		add(negativeButton, c);

		c.gridx++;
		JButton zeroButton = new JButton("0");
		numberButtons[0] = zeroButton;
		zeroButton.setBackground(lightButton);
		zeroButton.setFont(numButtonFont);
		zeroButton.setBorder(javax.swing.BorderFactory.createEmptyBorder());
		add(zeroButton, c);
		
		c.gridx++;
		JButton decimalButton = new JButton(".");
		decimalButton.setBackground(darkButton);
		decimalButton.setFont(plainTextFont);
		decimalButton.setBorder(javax.swing.BorderFactory.createEmptyBorder());
		add(decimalButton, c);
		
		c.gridx++;
		JButton equalsButton = new JButton("\uFF1D");
		operatorButtons[9] = equalsButton;
		equalsButton.setBackground(darkButton);
		equalsButton.setFont(uniLargeFont);
		equalsButton.setBorder(javax.swing.BorderFactory.createEmptyBorder());
		add(equalsButton, c);		
		pack();
		
//------Adding actionlisteners to all buttons
		
		for(int i = 0; i < 4; i++) {
			baseButtons[i].addActionListener(this);
		}	
		for(int i = 0; i < 10; i++) {
			operatorButtons[i].addActionListener(this);
		}
		for(int i = 0; i < 16; i++) {
			numberButtons[i].addActionListener(this);
		}
		for(int i = 0; i < 4; i++) {
			//bitLogicButtons[i].addActionListener(this);
		}
		negativeButton.addActionListener(this);
		clearEntryButton.addActionListener(this);
		clearButton.addActionListener(this);
		backButton.addActionListener(this);
		shiftButton.addActionListener(this);
		debug.addActionListener(this);
	}


	public long evaluate(){ 
        //char[] tokens = expression.toCharArray();  
        operators.clear();
        operands.clear();
        
        
        String exp = "";
		LinkedList<String> temp = new LinkedList<>(expression);
    	while(!temp.isEmpty()) {
    		String s = temp.removeFirst();
    		exp += s;
    	}
        
        exp = insertBlanks(exp);
        String[] tokens = exp.split(" ");
        
        for(String token: tokens){
        	if(token.length() == 0)
        		continue;
        	else if(token.charAt(0) == '+' || token.charAt(0) == '_') {
        		
        		while(!operators.isEmpty() &&
        			(operators.peek() == '+' ||
					 operators.peek() == '_' ||
					 operators.peek() == '*' ||
					 operators.peek() == '/' ||
					 operators.peek() == '%' )){
        			doOp();
        		}
        		operators.push(token.charAt(0));
        		
        	}
        	else if(token.charAt(0) == '*' || token.charAt(0) == '/' || token.charAt(0) == '%') {
        		
        		while(!operators.isEmpty() &&
					 (operators.peek() == '*' ||
					 operators.peek() == '%' )){
        			doOp();
        		}
        		operators.push(token.charAt(0));
        	}
        	else if(token.trim().charAt(0) == '(') {
        		operators.push('(');
        	}
        	else if(token.trim().charAt(0) == ')') {
        		while(operators.peek() != '(') {
        			doOp();
        		}
        		operators.pop();
        	}
        	else{
        		operands.push(new Long(token));
        	}
        }
        
        while(!operators.isEmpty()) {
        	doOp();
        }
        
        return operands.pop();
    } 
	public String insertBlanks(String s) {
		String result = "";
		for(int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
			if(c == '(' || c == ')' || c == '+' || c == '_' ||
			   c == '*' || c == '/' || c == '<' || c == '>' ||
			   c == '%')
				result += " " + c + " ";
			else
				result += c;
		}
		return result;
	}
    // A utility method to apply an operator 'op' on operands 'a'  
    // and 'b'. Return the result. 
    public static void doOp(){ 
        System.out.println("pushed " + operands.peek());
    	char operator = operators.pop();
    	System.out.println("just popped " + operator);
    	long b = operands.pop();
    	long a = operands.pop();
      
        if(operator == '+') {
            operands.push(a + b); 
        }
        else if(operator == '_') {
            operands.push(a - b); 
        }
        else if(operator == '*') {
            operands.push(a * b); 
        }
        else if(operator == '%') {
            operands.push(a % b); 
        }
        else if(operator == '/') {
            if (b == 0) 
                throw new UnsupportedOperationException("Cannot divide by zero"); 
            operands.push(a / b); 
            
        } 
        
    } 
	
  
    public char intToChar(int i) {
    	switch(i) {
    	case 0:
    		return '0';
    	case 1:
    		return '1';
    	case 2:
    		return '2';
    	case 3:
    		return '3';
    	case 4:
    		return '4';
    	case 5:
    		return '5';
    	case 6:
    		return '6';
    	case 7:
    		return '7';
    	case 8:
    		return '8';
    	case 9:
    		return '9';
    	case 10:
    		return 'A';
    	case 11:
    		return 'B';
    	case 12:
    		return 'C';
    	case 13:
    		return 'D';
    	case 14:
    		return 'E';
    	case 15:
    		return 'F';
    	default:
    		return 'z';
    	}
    }
    
	public String getOperator(int i) {
		switch(i){
		case 0:
			if(shift)
				return "RoL(";
			else
				return "Lsh";
		case 1:
			if(shift)
				return "RoR(";
			else
				return "Rsh";
		case 2:
			return "Mod(";
		case 3:
			return "/";
		case 4:
			return "*";
		case 5:
			return "-";
		case 6:
			return "+";
		case 7:
			return "(";
		case 8:
			return ")";
		case 9:
			return "=";
		default:
			return "unsupported operation";
		}
	}	
	public String getCharOp(int i) {

		switch(i){
		case 0:
			if(!shift)
				//return "RoL(";
				//this is going to be used in a another function

				//return "Lsh(";
				return "<";
		case 1:
			if(!shift)
				//return "RoR(";
				//this is going to be used in a another function
				return "<";
		case 2:
			return "%";
		case 3:
			return "/";
		case 4:
			return "*";
		case 5:
			return "_";
		case 6:
			return "+";
		case 7:
			return "(";
		case 8:
			return ")";
		case 9:
			return "=";
		default:
			return " ";
		}
		
	}
	
	private void update(){
		if(baseMode == 0)
			decValue = hexToLong(inputField.getText());
		if(baseMode == 1)
			decValue = Long.parseLong(inputField.getText());
		if(baseMode == 3)
			decValue = octToLong(inputField.getText());
		if(baseMode == 4)
			decValue = binToLong(inputField.getText());
		
		baseStrings[0] = Long.toHexString(decValue).toUpperCase();
		baseStrings[1] = Long.toString(decValue);
		baseStrings[2] = Long.toOctalString(decValue);
		baseStrings[3] = Long.toBinaryString(decValue);
		baseButtons[0].setText(hex + baseStrings[0]);
		baseButtons[1].setText(dec + baseStrings[1]);
		baseButtons[2].setText(oct + baseStrings[2]);
		baseButtons[3].setText(bin + baseStrings[3]);
		inputField.setText(baseStrings[baseMode]);
	}
	public long binToLong(String s) {
		long base = 1;
		long val = 0;
		long result = 0;
		for(int i = s.length()-1; i >= 0; i--) {
			switch(s.charAt(i)) {
			case '0':
				val = 0;
				break;
			case '1':
				val = 1;
				break;
			}
			
			result += base*val;
			base *= 2;
	
		}
		return result;
	}
	public long octToLong(String s) {
		long base = 1;
		long val = 0;
		long result = 0;
		for(int i = s.length()-1; i >= 0; i--) {
			switch(s.charAt(i)) {
			case '0':
				val = 0;
				break;
			case '1':
				val = 1;
				break;
			case '2':
				val = 2;
				break;
			case '3':
				val = 3;
				break;
			case '4':
				val = 4;
				break;
			case '5':
				val = 5;
				break;
			case '6':
				val = 6;
				break;
			case '7':
				val = 7;
				break;
			case '8':
				val = 8;
				break;
			}
			
			result += base*val;
			base *= 8;
	
		}
		return result;
	}
	public long hexToLong(String s) {
		long base = 1;
		long val = 0;
		long result = 0;
		for(int i = s.length()-1; i >= 0; i--) {
			switch(s.charAt(i)) {
			case '0':
				val = 0;
				break;
			case '1':
				val = 1;
				break;
			case '2':
				val = 2;
				break;
			case '3':
				val = 3;
				break;
			case '4':
				val = 4;
				break;
			case '5':
				val = 5;
				break;
			case '6':
				val = 6;
				break;
			case '7':
				val = 7;
				break;
			case '8':
				val = 8;
				break;
			case '9':
				val = 9;
				break;
			case 'A':
				val = 10;
				break;
			case 'B':
				val = 11;
				break;
			case 'C':
				val = 12;
				break;
			case 'D':
				val = 13;
				break;
			case 'E':
				val = 14;
				break;
			case 'F':
				val = 15;
				break;
			}
			
			result += base*val;
			base *= 16;
	
		}
		return result;
	}
	public String toLongString(String s) {
		return Long.toString(toLong(s));
	}
	public long toLong(String s) {
		if(baseMode == 0)
			decValue = hexToLong(inputField.getText());
		if(baseMode == 1)
			decValue = Long.parseLong(inputField.getText());
		if(baseMode == 3)
			decValue = octToLong(inputField.getText());
		if(baseMode == 4)
			decValue = binToLong(inputField.getText());
		return decValue;
	}
	public void printExpression() {
		LinkedList<String> temp = new LinkedList<>(expression);
		String whole = "";
    	while(!temp.isEmpty()) {
    		String s = temp.removeFirst();
    		whole += s + " ";
    	}
		System.out.println(whole);
		//System.out.println(num);
		//System.out.println(ops);
    }
	private void clear() {
		System.out.println("clearing");
		expressionField.setText("");
		expression.clear();
		needsParen = false;
		openParens = 0;
		noNum = false;
		clearEntry();
	}
	private void clearEntry() {
		inputField.setText("0");
	}
	private void back() {
		String s = "";
		for(int i = 0; i < inputField.getText().length()-1; i++) {
			s += inputField.getText().charAt(i);
		}
		if(s == "")
			s="0";
		inputField.setText(s);
	}
	private void equals() {
		
		if(!noNum) {
			expression.addLast(inputField.getText());
			expressionField.setText(expressionField.getText() + inputField.getText());
		}
		
		printExpression();
		long result = evaluate();
		clear();
		inputField.setText(Long.toString(result));
		System.out.println(result);
		
		temp = true;
	}
	private void operator(int i) {
		
	//this would have been using for bit shifting operations
	/*	if(getOperator(i).contentEquals("RoL(")) {
			if(expressionField.getText().isEmpty()) {
				expressionField.setText("RoL(" + inputField.getText() + ")");
				b.shiftLeft();
				decValue = b.longFromBits();
				update();
				expression.offerLast(inputField.getText());
				noNum = true;
			}
			else{
				expressionField.setText(expressionField.getText() + getOperator(i));
				expressionField.setText(expressionField.getText() + inputField.getText() + ")");
				b.shiftLeft();
				decValue = b.longFromBits();
				update();
				expression.offerLast(inputField.getText());
				noNum = true;
			}
		}
		else if(getOperator(i).contentEquals("RoR(")) {
			if(expressionField.getText().isEmpty()) {
				expressionField.setText("RoR(" + inputField.getText() + ")");
				b.shiftRight();
				decValue = b.longFromBits();
				update();
				expression.offerLast(inputField.getText());
				noNum = true;
			}
			else{
				expressionField.setText(expressionField.getText() + getOperator(i));
				expressionField.setText(expressionField.getText() + inputField.getText() + ")");
				b.shiftRight();
				decValue = b.longFromBits();
				update();
				expression.offerLast(inputField.getText());
				noNum = true;
			}
		}
		else */
		if(getOperator(i).contentEquals("Mod(")) {
			if(expressionField.getText().isEmpty() && !needsParen) {
				expressionField.setText(inputField.getText()+getOperator(i));
				expression.offerLast(toLongString(inputField.getText()));
				expression.offerLast(getCharOp(i));
				needsParen = true;
				noNum = false;
			}
			else if(!noNum && !needsParen){
				expressionField.setText(expressionField.getText() + inputField.getText() + getOperator(i));
				expression.offerLast(toLongString(inputField.getText()));
				expression.offerLast(getCharOp(i));
				needsParen = true;
				noNum = false;
			}
		}
		else if(getOperator(i).contentEquals("(")) {
			openParens++;
			if(expressionField.getText().isEmpty()) {
				expressionField.setText(getOperator(i));
				expression.offerLast(getCharOp(i));
				noNum = false;
			}
			else if(!noNum){
				expressionField.setText(expressionField.getText() + getOperator(i));
				expression.offerLast(getCharOp(i));
				noNum = false;
			}
		}
		
		//if the character is not an opening parentheses
		else {
			//if the last character was a closing parentheses
			if(noNum) {
				if(getOperator(i).contentEquals(")")) 
				{	
					if(openParens > 0) {
						expressionField.setText(expressionField.getText() + getOperator(i));
						openParens--;
						noNum = true;
					}
				}
				else {
					expressionField.setText(expressionField.getText() + getOperator(i));
					expression.offerLast(getCharOp(i));
					noNum = false;
				}
			}
	
		//if the last character were not a closing parentheses
			else {
				expressionField.setText(expressionField.getText() + inputField.getText());
				if(needsParen) {
					expressionField.setText(expressionField.getText() + ")");
					openParens--;
					needsParen = false;
				}
				expressionField.setText(expressionField.getText() + getOperator(i));
				expression.offerLast(toLongString(inputField.getText()));
				expression.offerLast(getCharOp(i));
				//expression.addLast(inputField.getText());
				System.out.println("failed to add");
				//expression.addLast(getOperator(i));
				noNum = false;
				if(getOperator(i).contentEquals(")")) {
					openParens--;
					noNum = true;
				}
			}
		}

		System.out.println("should have added ");
		temp = true;
	}	
	private void shift() {
		if(shift) {
			shift = false;
			operatorButtons[0].setText("Lsh");
			operatorButtons[1].setText("Rsh");
		}
		else {
			shift = true;
			operatorButtons[0].setText("RoL");
			operatorButtons[1].setText("RoR");
		}
	}
	private void number(int i) {
		if(inputField.getText().contentEquals("0") || temp) {
			inputField.setText(Character.toString(intToChar(i)));
		}
		else {
			System.out.println(inputField.getText());
			inputField.setText(inputField.getText() + intToChar(i));
		}
		temp = false;
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		int k = 0;
		switch(baseMode) {
		case 0:
			k = 16;
			break;
		case 1:
			k = 10;
			break;
		case 2:
			k = 8;
			break;
		case 3:
			k = 2;
			break;
		}
		for(int i = 0; i < k; i++) {
			if(e.getSource() == numberButtons[i])
				number(i);
			update();
		}
		for(int i = 0; i < 4; i++) {
			if(e.getSource() == baseButtons[i])
				baseMode = i;

			//inputField.setText(baseStrings[i]);
			update();
		}
		update();
		//b.setBits(decValue);

		if(e.getSource() == debug)
			printExpression();
		
		if(e.getSource() == operatorButtons[9])
			equals();
		//starting at 2 since the first two are bit shifting ones
		for(int i = 2; i < 9; i++) {
			if(e.getSource() == operatorButtons[i]) 
				operator(i);
		}
		
		if(e.getSource() == shiftButton) {
			shift();
		}
		
		if(e.getSource() == clearButton)
			clear();
		
		if(e.getSource() == clearEntryButton)
			clearEntry();
		
		if(e.getSource() == backButton)
			back();
		if(e.getSource() == negativeButton) {
			if(inputField.getText().charAt(0) == '-')
				inputField.setText(inputField.getText().substring(1));
			else
				inputField.setText("-" + inputField.getText());
		}
		System.out.println("action performed");
		
		update();
	}
	public static void main(String[] args) {
		Calculator app = new Calculator();
		app.setTitle("Calculator");
		app.init();
		app.setVisible(true);
	}
}
	//this was going to be used for the bit logic
	/*
	class Bits{
		Boolean[] bits = new Boolean[64];
		
		public void xorBits(Bits b) {
			for(int i = 0; i < 64; i++) {
				if(b.bits[i]==bits[i]) {
					bits[i] = false;
				}
				else
					bits[i] = true;
			}
		}
		public void xorBits(long l) {
			xorBits(new Bits(bitsFromLong(l)));
		}
		public void orBits(Bits b) {
			for(int i = 0; i < 64; i++) {
				if(b.bits[i]||bits[i]) {
					bits[i] = true;
				}
			}
		}
		public void orBits(long l) {
			orBits(new Bits(bitsFromLong(l)));
		}
		public void andBits(Bits b) {
			for(int i = 0; i < 64; i++) {
				if(b.bits[i]&&bits[i]) {
					bits[i] = true;
				}
				else
					bits[i] = false;
			}
		}
		public void setBits(Boolean[] b) {
			for(int i = 0; i < 64; i++) {
				bits[i] = b[i];
			}
		}
		public void setBits(long l) {
			setBits(bitsFromLong(l));
		}
		public void setBits(Bits b) {
			setBits(b.bits);
		}
		public Boolean[] bitsFromLong(long i) {
			Boolean[] b = new Boolean[64];
			String temp = Long.toBinaryString(i);
			for(int k = 0; k < temp.length(); k++) {
				if(temp.charAt(temp.length()-1-k)=='1') {
					b[k] = true;
				}
			}
			return b;
		}
		public long longFromBits(Bits b) {
			long l = 0;
			long base = 1;
			for(int i = 0; i < 64; i++) {
				if(b.bits[i])
					l += base;
				base *= 2;
			}
			return l;
		}
		public long longFromBits() {
			long l = 0;
			long base = 1;
			for(int i = 0; i < 64; i++) {
				if(bits[i])
					l += base;
				base *= 2;
			}
			return l;
		}
		public void shiftLeft() {
			Bits temp = new Bits();
			if(bits[63])
				temp.bits[0] = true;
			for(int i = 0; i < 63; i++) {
				temp.bits[i+1] = bits[i];
			}
			setBits(temp);
		}
		public void shiftLeft(int i) {
			for(int k = 0; k < i%64; k++) {
				shiftLeft();
			}
		}
		public void shiftRight() {
			Bits temp = new Bits();
			if(bits[0])
				temp.bits[63] = true;
			for(int i = 1; i < 64; i++) {
				temp.bits[i-1] = bits[i];
			}
			setBits(temp);
		}
		public void shiftRight(int i) {
			for(int k = 0; k < i%64; k++) {
				shiftRight();
			}
		}
		Bits() {
			this(0);
		}
		Bits(long i){
			setBits(bitsFromLong(i));
		}
		Bits(Boolean[] b){
			setBits(b);
		}
	}
}
*/