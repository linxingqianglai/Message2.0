package client;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JDialog;
import javax.swing.JLabel;

public class ColorOptionDialog implements ActionListener {
		private Color color;
		private JColorChooser ui_Color_JColorChooser;//颜色选择器
		private JDialog jDialog;
		public ColorOptionDialog(Color color,Component c){
			this.color=color;
			 this.ui_Color_JColorChooser=  new JColorChooser(color);
			 jDialog =  JColorChooser.createDialog(c, "颜色选择", true, this.ui_Color_JColorChooser, this, this);
			 jDialog.setVisible(true);
			 
		}



		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			
			if(((JButton)e.getSource()).getText().equals("确定")){
				this.color=this.ui_Color_JColorChooser.getColor();
			}
			
		}
		
		public Color getColor(){
			return this.color;
		}
}
