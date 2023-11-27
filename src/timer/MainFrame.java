/**
 *
 */
package timer;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.awt.event.ActionListener;
import java.util.*;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.WindowConstants;

/**
 * @author sven
 *
 */
public class MainFrame extends JFrame {
	private static final long serialVersionUID = 889249422558488806L;

	protected JTextField time, nExtra;
	protected JComboBox<GraphicsDevice> combo;
	protected JCheckBox fullscreen, edit, endtime;
	protected JCheckBox n10, n20, n25, n30;

	private ResourceBundle lang;


	public MainFrame() {
		lang = ResourceBundle.getBundle("lang");

		// add action
		ActionListener ae = e -> EventQueue.invokeLater(() -> {
            try {
                String answer = time.getText();
                int minutes = Integer.parseInt(answer);
                if (minutes > 0) {
                    // new WorkThreadExample();

                    new TimerFrame(minutes, fullscreen.isSelected(),
                            (GraphicsDevice) combo.getSelectedItem(), edit.isSelected(), BuildList());
                } else {
                    JOptionPane.showMessageDialog(MainFrame.this, lang.getString("time.notvalid"));
                }
            } catch (NumberFormatException e1) {
                JOptionPane.showMessageDialog(MainFrame.this, lang.getString("error.number"));
            } catch (HeadlessException e1) {
                JOptionPane.showMessageDialog(MainFrame.this,
                        e1.getClass() + " " + e1.getMessage());
            }
        });

		// add Panel
		setLayout(new BorderLayout());
		add(new JLabel(lang.getString("time")), BorderLayout.WEST);

		time = new JTextField("60");
		add(time);
		time.addActionListener(ae);

		JButton bn = new JButton(lang.getString("start"));
		bn.addActionListener(ae);

		add(bn, BorderLayout.EAST);

		JPanel p = new JPanel(new GridLayout(10, 1));
		fullscreen = createBox(p, lang.getString("fullscreen"));
		combo = new JComboBox<>(GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices());
		edit = createBox(p, lang.getString("time.change"));
		p.add(combo);
		p.add(new JLabel(lang.getString("optional")));
		endtime = createBox(p, lang.getString("optional.endtime"));
		n10 = createBox(p, lang.getString("optional.nta.10"), false);
		n20 = createBox(p, lang.getString("optional.nta.20"), false);
		n25 = createBox(p, lang.getString("optional.nta.25"), false);
		n30 = createBox(p, lang.getString("optional.nta.30"), false);
		nExtra = new JTextField("");
		p.add(nExtra);

		add(p, BorderLayout.SOUTH);

		// show it
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		pack();
		setLocationRelativeTo(null);
		setVisible(true);
	}

	private List<String> BuildList() {
		ArrayList<String> texts = new ArrayList<>();
		if (endtime.isSelected())
			texts.add("end");
		if (n10.isSelected())
			texts.add("+10");
		if (n20.isSelected())
			texts.add("+20");
		if (n25.isSelected())
			texts.add("+25");
		if (n30.isSelected())
			texts.add("+30");
		if (!nExtra.getText().isEmpty())
			texts.add(nExtra.getText());

		Collections.reverse(texts);

		return texts;
	}

	/**
	 * @param p
	 */
	private JCheckBox createBox(JPanel p, String text) {
		return createBox(p, text, true);
	}

	/**
	 * @param p
	 */
	private JCheckBox createBox(JPanel p, String text, boolean select) {
		JCheckBox c = new JCheckBox(text);
		c.setSelected(select);
		p.add(c);
		return c;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(() -> {
            try {
                try {
                    // Set System L&F
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch (Exception e) {
                    // handle exception
                    e.printStackTrace();
                }
                new MainFrame();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

	}

}
