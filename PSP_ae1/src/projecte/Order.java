package projecte;

import java.awt.EventQueue;
import javax.swing.JOptionPane;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.Color;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Classe Order: Representa la interfície gràfica per a realitzar comandes de tetrominos.
 */
public class Order extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField textTetromino;
	private JLabel lblIntrodueixQuantitat;
	private JTextField textQuantitat;
	private JTextField textField;

	/**
	 * Mètode principal per iniciar l'aplicació.
	 * @param args Arguments de la línia de comandes.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Order frame = new Order();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Constructor de la classe Order.
	 */
	public Order() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 681, 195);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(192, 192, 192));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JButton btnAfegir = new JButton("Afegir");
		btnAfegir.setFont(new Font("Tahoma", Font.BOLD, 12));
		btnAfegir.setBounds(550, 17, 86, 31);
		contentPane.add(btnAfegir);
		
		/**
		 * ActionListener per al botó "Afegir".
		 * Aquest ActionListener s'encarrega de realitzar les accions necessàries quan es fa clic en el botó "Afegir".
		 */
		btnAfegir.addActionListener(new ActionListener() {
		    /**
		     * Mètode actionPerformed que s'executa quan es fa clic en el botó "Afegir".
		     * @param e Event d'acció.
		     */
		    public void actionPerformed(ActionEvent e) {
		        // Obtindre el tipus de tetromino i la quantitat ingressada
		        String tipusTetromino = textTetromino.getText();
		        int quantitat = 0;

		        try {
		            // Intentar obtenir la quantitat com a enter
		            String quantitatText = textQuantitat.getText();
		            if (quantitatText == null || quantitatText.trim().isEmpty()) {
		                throw new NumberFormatException("Cadena buida o nul·la");
		            }
		            quantitat = Integer.parseInt(quantitatText);
		        } catch (NumberFormatException ex) {
		            // Gestionar l'excepció si la quantitat no és un número vàlid
		            System.out.println("Error al convertir a enter: " + ex.getMessage());
		            System.out.println("Valor introduït: " + textQuantitat.getText());
		            ex.printStackTrace();
		            
		            // Mostrar un missatge d'error a l'usuari
		            JOptionPane.showMessageDialog(null, "Introdueix una quantitat vàlida.", "Error",
		                    JOptionPane.ERROR_MESSAGE);
		            return;
		        }

		        // Lògica per cridar a Manufacture.fabricarTetrominos amb els paràmetres adequats
		        Manufacture.fabricarTetrominos(tipusTetromino, quantitat, true);
		    }
		});

		JLabel lblIntroduirTetrominos = new JLabel("Introdueix el tetròmino que estes interessat:");
		lblIntroduirTetrominos.setFont(new Font("Tahoma", Font.BOLD, 15));
		lblIntroduirTetrominos.setBounds(10, 10, 368, 42);
		contentPane.add(lblIntroduirTetrominos);
		
		textTetromino = new JTextField();
		textTetromino.setFont(new Font("Tahoma", Font.PLAIN, 15));
		textTetromino.setBounds(390, 16, 124, 31);
		contentPane.add(textTetromino);
		textTetromino.setColumns(10);
		
		lblIntrodueixQuantitat = new JLabel("Introdueix la quantitat que estes interessat:");
		lblIntrodueixQuantitat.setFont(new Font("Tahoma", Font.BOLD, 15));
		lblIntrodueixQuantitat.setBounds(10, 56, 368, 42);
		contentPane.add(lblIntrodueixQuantitat);
		
		textQuantitat = new JTextField();
		textQuantitat.setFont(new Font("Tahoma", Font.PLAIN, 15));
		textQuantitat.setColumns(10);
		textQuantitat.setBounds(390, 62, 124, 31);
		contentPane.add(textQuantitat);
		
		JButton btnFinalitzar = new JButton("Finalitzar");
		btnFinalitzar.setFont(new Font("Tahoma", Font.BOLD, 12));
		btnFinalitzar.setBounds(550, 58, 96, 31);
		contentPane.add(btnFinalitzar);
		
		/**
		 * ActionListener per al botó "Finalitzar".
		 * Aquest ActionListener s'encarrega de realitzar les accions necessàries quan es fa clic en el botó "Finalitzar".
		 */
		btnFinalitzar.addActionListener(new ActionListener() {
		    /**
		     * Mètode actionPerformed que s'executa quan es fa clic en el botó "Finalitzar".
		     * @param e Event d'acció.
		     */
		    public void actionPerformed(ActionEvent e) {
		        // Lògica per cridar a Manufacture.guardarTotesLesOrdresEnFitxer amb la direcció adequada
		        Manufacture.guardarTotesLesOrdresEnFitxer(textField.getText());
		    }
		});

		JLabel lblIntrodueixLaDirecci = new JLabel("Introdueix la direcció que estes interessat:");
		lblIntrodueixLaDirecci.setFont(new Font("Tahoma", Font.BOLD, 15));
		lblIntrodueixLaDirecci.setBounds(10, 108, 368, 42);
		contentPane.add(lblIntrodueixLaDirecci);
		
		textField = new JTextField();
		textField.setFont(new Font("Tahoma", Font.PLAIN, 15));
		textField.setColumns(10);
		textField.setBounds(390, 114, 256, 31);
		contentPane.add(textField);
	}
}