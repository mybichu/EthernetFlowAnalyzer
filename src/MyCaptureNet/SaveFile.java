package MyCaptureNet;

import java.awt.Component;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * 用于保存文件的类
 * 
 * @author 于修彦
 *
 */
public class SaveFile {
	/**
	 * 保存内容到文件
	 * 
	 * @param content
	 *            内容
	 */
	public void saveFile(Component parent, String content) {
		FileNameExtensionFilter filter = new FileNameExtensionFilter("*.txt", "txt");
		JFileChooser fc = new JFileChooser();
		fc.setFileFilter(filter);
		fc.setMultiSelectionEnabled(false);
		int result = fc.showSaveDialog(parent);
		if (result == JFileChooser.APPROVE_OPTION) {
			File file = fc.getSelectedFile();

			if (!file.getPath().endsWith(".txt")) {
				file = new File(file.getPath() + ".txt");
			}
			// System.out.println("file path=" + file.getPath());

			FileOutputStream fos = null;

			try {
				if (!file.exists()) {
					file.createNewFile();
				}

				fos = new FileOutputStream(file);
				fos.write(content.getBytes());
				fos.flush();

				// 弹窗提示成功
				JOptionPane.showMessageDialog(parent, "文件保存成功，路径：" + file.getPath(), "成功",
						JOptionPane.INFORMATION_MESSAGE);

			} catch (IOException e) {
				// System.out.println("文件创建失败：");
				// 弹窗提示失败
				JOptionPane.showMessageDialog(parent, "文件保存失败", "错误", JOptionPane.ERROR_MESSAGE);
				e.printStackTrace();
			} finally {
				if (fos != null) {
					try {
						fos.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}
}
