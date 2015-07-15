package com.point.web.listener;

import java.io.IOException;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.log4j.Logger;

import com.point.web.util.MMSTool;

/**
 * @Title: MMSftp监听注册类
 * @Description:MMSftp监听注册类
 * @Since: 2015年7月06日上午10:20:20
 * @author wangchunlong
 */
public class MMSFTPListener {

	private static Logger log = Logger.getLogger(MMSFTPListener.class);
	private FTPClient ftpClient;

	public MMSFTPListener() {
		this.reSet();
	}

	public void reSet() {
		// 以当前系统时间拼接文件名
		this.connectServer(MMSTool.getMMSMap().get("ftpurl"), 21, MMSTool
				.getMMSMap().get("ftpusername"),
				MMSTool.getMMSMap().get("ftppassword"), MMSTool.getMMSMap()
						.get("ftppath"));
	}

	/**
	 * @param ip
	 * @param port
	 * @param userName
	 * @param userPwd
	 * @param path
	 * @throws SocketException
	 * @throws IOException
	 *             function:连接到服务器
	 */
	public void connectServer(String ip, int port, String userName,
			String userPwd, String path) {
		ftpClient = new FTPClient();
		try {
			// 连接
			ftpClient.connect(ip, port);
			// 登录
			ftpClient.login(userName, userPwd);
			if (path != null && path.length() > 0) {
				// 跳转到指定目录
				ftpClient.changeWorkingDirectory(path);
			}
		} catch (SocketException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @throws IOException
	 *             function:关闭连接
	 */
	public void closeServer() {
		if (ftpClient.isConnected()) {
			try {
				ftpClient.logout();
				ftpClient.disconnect();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * @param path
	 * @return function:读取指定目录下的文件名
	 * @throws IOException
	 */
	public List<String> getFileList(String path) {
		List<String> fileLists = new ArrayList<String>();
		// 获得指定目录下所有文件名
		FTPFile[] ftpFiles = null;
		try {
			ftpFiles = ftpClient.listFiles(path);
		} catch (IOException e) {
			e.printStackTrace();
		}
		for (int i = 0; ftpFiles != null && i < ftpFiles.length; i++) {
			FTPFile file = ftpFiles[i];
			if (file.isFile()) {
				fileLists.add(file.getName());
			}
		}
		return fileLists;
	}

	static boolean compareName(String fileName, String dateStr) {
		for (int i = 0; i < dateStr.length(); i++) {
			if (dateStr.charAt(i) != fileName.charAt(i + 4)) {
				return false;
			}
		}
		return true;
	}

	public FTPClient getFtpClient() {
		return ftpClient;
	}

	public void setFtpClient(FTPClient ftpClient) {
		this.ftpClient = ftpClient;
	}


}
