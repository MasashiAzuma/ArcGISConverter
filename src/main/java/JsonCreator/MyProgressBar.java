package JsonCreator;

import java.util.Random;

import javax.swing.JProgressBar;

public class MyProgressBar {
	private JProgressBar progressBar;
	
	public MyProgressBar(JProgressBar progressBar) {
		this.progressBar = progressBar;
	}
	
	public void setMax(int n) {
		progressBar.setMaximum(n);
		progressBar.setMinimum(0);
	}
	
	public void update() {
		progressBar.setValue(progressBar.getValue()+1);
	}

	public void start() {
		progressBar.setValue(0);
		progressBar.setString(null);
	}

	public void kill() {
		progressBar.setString("Completed");
	}
}
