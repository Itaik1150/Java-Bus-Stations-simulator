import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

public class MyGui extends JFrame {

	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MyGui frame = new MyGui();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public MyGui() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setToolTipText("");
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);

		JSpinner numOfTechnicalAttendant = new JSpinner(); // Receiving the number of technical attendants
		numOfTechnicalAttendant.setModel(new SpinnerNumberModel(Integer.valueOf(1), null, null, Integer.valueOf(1)));
		numOfTechnicalAttendant.setBounds(300, 100, 100, 20);
		contentPane.add(numOfTechnicalAttendant);

		JSpinner cleanerWorkRate = new JSpinner(); // Receiving the cleaner work rate
		cleanerWorkRate.setModel(new SpinnerNumberModel(Double.valueOf(2), null, null, Integer.valueOf(1)));
		cleanerWorkRate.setBounds(300, 151, 101, 20);
		contentPane.add(cleanerWorkRate);

		JButton startbutton = new JButton("START");
		startbutton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int numTechAttendant = ((int) (numOfTechnicalAttendant.getValue()));
				double workRate = (double) cleanerWorkRate.getValue();
				if (!checkInt(numTechAttendant) || !checkDouble(workRate)) {// If the values aren't valid - technical
																			// attendant needs to be int & work time
																			// double
					JOptionPane.showMessageDialog(null, "Illegal input!", "Error", JOptionPane.ERROR_MESSAGE);
					return;
				}
				numTechAttendant = setNumOfTech(numTechAttendant);// If the input was invalid - will run with default
																	// value: 1
				workRate = setCleanerWorkRate(workRate);// If the input was invalid - will run with default value: 2
				CentralBusStation newE = new CentralBusStation("Bus.txt", numTechAttendant, workRate);
				newE.start();
			}

			private boolean checkInt(int input) { // check if input is int
				try {
					int value1 = input;
					return true;
				} catch (Exception e) {
					return false;
				}
			}

			private boolean checkDouble(double input) { // check if input is double
				try {
					double value2 = input;
					return true;
				} catch (Exception e) {
					return false;
				}
			}

			private int setNumOfTech(int num) { // set num of Technical attendants - min 1 person
				if (num < 1) {
					JOptionPane.showMessageDialog(null, "Technical attendants number need to be at least: 1", "Info",
							JOptionPane.INFORMATION_MESSAGE);
					numOfTechnicalAttendant
							.setModel(new SpinnerNumberModel(Integer.valueOf(1), null, null, Integer.valueOf(1)));
					return 1;
				}
				return num;
			}

			private double setCleanerWorkRate(double num) { // set cleaner work rate - min 2 sec
				if (num < 2) {
					JOptionPane.showMessageDialog(null, "Cleaner work time need to be at least: 2", "Info",
							JOptionPane.INFORMATION_MESSAGE);
					cleanerWorkRate
							.setModel(new SpinnerNumberModel(Integer.valueOf(2), null, null, Integer.valueOf(1)));
					return 2;
				}
				return num;
			}
		});
		startbutton.setBounds(90, 213, 85, 21);
		contentPane.add(startbutton);

		JLabel lblNewLabel = new JLabel("Fatma Travel Agency");
		lblNewLabel.setFont(new Font("Arial", Font.BOLD, 14));
		lblNewLabel.setBounds(63, 46, 313, 44);
		contentPane.add(lblNewLabel);

		JButton exitbutton = new JButton("EXIT");
		exitbutton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		exitbutton.setBounds(272, 213, 85, 21);
		contentPane.add(exitbutton);

		JLabel lblNewLabel_1 = new JLabel("number of technical Attendants");
		lblNewLabel_1.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblNewLabel_1.setBounds(75, 101, 970, 19);
		contentPane.add(lblNewLabel_1);

		JLabel lblNewLabel_2 = new JLabel("work time for Cleaners");
		lblNewLabel_2.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblNewLabel_2.setBounds(80, 150, 840, 17);
		contentPane.add(lblNewLabel_2);

	}
}