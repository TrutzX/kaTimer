//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package timer;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.GraphicsDevice;
import java.awt.HeadlessException;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.Serial;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;
import javax.swing.*;

public class TimerFrame extends JFrame {
	@Serial
	private static final long serialVersionUID = -7723095074098820654L;
	protected JLabel timeLabel;
	protected JLabel endLabel;
	protected int minutes;
	protected Date endTime;
	protected Date startTime;
	protected boolean fullscreen;
	protected GraphicsDevice device;
	protected List<String> texts;
	private ResourceBundle lang;

	public TimerFrame(int minutes, boolean fullscreen, GraphicsDevice device, boolean edit, List<String> texts) throws HeadlessException {

		lang = ResourceBundle.getBundle("lang");

		this.minutes = minutes;
		this.device = device;
		this.texts = texts;
		this.endTime = new Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(this.endTime);
		this.startTime = cal.getTime();
		cal.add(12, minutes);
		this.endTime = cal.getTime();
		this.setLayout(new BorderLayout());
		this.timeLabel = new JLabel("S", null, 0);
		this.timeLabel.setOpaque(true);
		this.timeLabel.setBackground(Color.BLACK);
		this.timeLabel.setForeground(Color.white);
		this.add(this.timeLabel, "Center");
		if (!texts.isEmpty()) {
			this.endLabel = new JLabel("", null, 0);
			this.endLabel.setOpaque(true);
			this.endLabel.setBackground(Color.BLACK);
			this.endLabel.setForeground(Color.white);
			this.add(this.endLabel, "North");
		}

		this.reCalcEndTime();
		this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		this.switchFullscreen(fullscreen);
		if (edit) {
			this.addKeyListener(new KeyAdapter() {
				public void keyReleased(KeyEvent e) {
					Calendar cal;
					if (e.getKeyChar() == '+') {
						cal = Calendar.getInstance();
						cal.setTime(TimerFrame.this.endTime);
						cal.add(Calendar.MINUTE, 1);
						TimerFrame.this.endTime = cal.getTime();
						TimerFrame.this.timeLabel.setForeground(Color.WHITE);
						TimerFrame.this.reCalcTime();
						TimerFrame.this.reCalcEndTime();
					}

					if (e.getKeyChar() == '-') {
						cal = Calendar.getInstance();
						cal.setTime(TimerFrame.this.endTime);
						cal.add(Calendar.MINUTE, -1);
						TimerFrame.this.endTime = cal.getTime();
						TimerFrame.this.reCalcTime();
						TimerFrame.this.reCalcEndTime();
					}

					if (e.getKeyCode() == 27) {
						TimerFrame.this.switchFullscreen(!TimerFrame.this.fullscreen);
					}

				}
			});
		}

		(new Thread(() -> {
            while(true) {
                SwingUtilities.invokeLater(TimerFrame.this::reCalcTime);

                try {
                    Thread.sleep(1000L);
                } catch (InterruptedException var2) {
                    JOptionPane.showMessageDialog(TimerFrame.this, var2.getClass() + " " + var2.getMessage());
                    var2.printStackTrace();
                }
            }
        })).start();
	}

	private void switchFullscreen(boolean fullscreen) {
		if (!this.isVisible()) {
			if (fullscreen) {
				this.setUndecorated(true);
				this.pack();
				this.device.setFullScreenWindow(this);
			} else {
				this.setSize(640, 480);
				this.setVisible(true);
				this.setLocationRelativeTo(null);
			}
		} else {
			if (this.fullscreen) {
				this.device.getFullScreenWindow().dispose();
				this.device.setFullScreenWindow(null);
				this.setUndecorated(false);
				this.pack();
				this.setSize(640, 480);
				this.setLocationRelativeTo(null);
				this.setVisible(true);
			} else {
				this.dispose();
				this.setUndecorated(true);
				this.pack();
				this.device.setFullScreenWindow(this);
			}

			this.reCalcTime();
		}

		this.fullscreen = fullscreen;
	}

	public static long getDateDiff(Date date1, Date date2, TimeUnit timeUnit) {
		long diffInMillies = date2.getTime() - date1.getTime();
		return timeUnit.convert(diffInMillies, TimeUnit.MILLISECONDS);
	}

	private void reCalcEndTime() {
		String title = MessageFormat.format(lang.getString("time.until"), (new SimpleDateFormat("HH:mm")).format(this.endTime));
		this.setTitle(title);
		if (this.endLabel != null) {
			this.endLabel.setText(title);
		}
	}

	private void UpdateSecondScreen() {
		if (this.endLabel.getText().equals("end")) {
			this.reCalcEndTime();
		} else if (this.endLabel.getText().startsWith("+")) {
			float diff = 1.0F + Float.parseFloat(this.endLabel.getText().substring(1, 3)) / 100.0F;
			new Date();
			Calendar cal = Calendar.getInstance();
			cal.setTime(this.startTime);
			cal.add(Calendar.MINUTE, (int)((float)this.minutes * diff));
			Date time = cal.getTime();
			String title = MessageFormat.format(lang.getString("optional.nta.show"), this.endLabel.getText().substring(1, 3),(new SimpleDateFormat("HH:mm")).format(time),this.getDateDisplay(time));
			this.endLabel.setText(title);
		}
	}

	private void reCalcTime() {
		if (this.timeLabel.getFont().getSize() != this.getHeight() * 3 / 4) {
			this.timeLabel.setFont(new Font(this.timeLabel.getFont().getName(), this.timeLabel.getFont().getStyle(), this.getHeight() * 3 / 4));
			if (this.endLabel != null) {
				this.endLabel.setFont(new Font(this.endLabel.getFont().getName(), this.endLabel.getFont().getStyle(), this.getHeight() / 8));
			}
		}

		int min = (int)getDateDiff(new Date(), this.endTime, TimeUnit.MINUTES);
		int sec = (int)getDateDiff(new Date(), this.endTime, TimeUnit.SECONDS);
		if (this.endLabel != null) {
			this.endLabel.setText(this.texts.get(sec / 5 % this.texts.size()));
			this.UpdateSecondScreen();
		}

		if (min == 0) {
			this.timeLabel.setForeground(Color.YELLOW);
			if (sec >= 0 && sec < 4) {
				Toolkit.getDefaultToolkit().beep();
			} else if (sec < 0) {
				this.timeLabel.setForeground(Color.RED);
			}
		}

		this.timeLabel.setText(this.getDateDisplay(this.endTime));
		this.timeLabel.repaint();
	}

	private String getDateDisplay(Date time) {
		int min = (int)getDateDiff(new Date(), time, TimeUnit.MINUTES);
		if (min != 0) {
			if (min >= 20) {
				++min;
				min /= 10;
				min *= 10;
			} else if (min >= 5) {
				min /= 5;
				min *= 5;
			} else if (min < 0) {
				min *= -1;
			}

			return min + "'";
		} else {
			int sec = (int)getDateDiff(new Date(), time, TimeUnit.SECONDS);
			if (sec < 0 || sec >= 10) {
				if (sec < 0) {
					sec *= -1;
					this.timeLabel.setForeground(Color.RED);
				} else {
					sec /= 10;
					sec *= 10;
				}
			}

			return sec + "''";
		}
	}
}
