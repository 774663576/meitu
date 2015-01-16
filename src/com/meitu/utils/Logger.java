package com.meitu.utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Calendar;
import java.util.Date;

import android.text.TextUtils;

/**
 * ��־��¼������
 * 
 */
public class Logger {
	private static boolean isOutPut = true;

	/**
	 * ��־����
	 * 
	 */
	public enum Level {

		/*** ���Լ��� **/
		DEBUG(0), // ����
		/*** ��Ϣ��¼���� **/
		INFO(1), // ��Ϣ
		/*** ���漶�� **/
		WARN(2), // ����
		/*** ���󼶱� **/
		ERROR(3), // ����
		/*** ���ش��󼶱� **/
		FATAL(4); // ���ش���

		private final int m_level;

		private Level(int level) {
			m_level = level;
		}

		/**
		 * ����Level��intֵ
		 * 
		 * @return
		 */
		public int getLevel() {
			return m_level;
		}

		public String getLevelDescn() {
			switch (m_level) {
			case 0:
				return "DEBUG";
			case 1:
				return "INFO";
			case 2:
				return "WARN";
			case 3:
				return "ERROR";
			case 4:
				return "FATAL";
			default:
				break;
			}
			return "";
		}
	};

	// ��ǰ��־����Ĭ��ΪDEBUG
	private static Level logLevel = Level.DEBUG;

	// ��־���Ŀ¼��Ĭ��ΪSD����/asr/crash��
	private static String logPath = FileUtils
			.getgetAbsoluteDir("/interestfriend/crash");

	// ��־�ļ���ǰ�
	private static String m_logNamePrex = "crash_";

	// ��־�ļ���С���ֵ�����ֽ�Ϊ��λ
	private static long maxFileSize = 1024 * 1024L;

	// ��־�ļ��ı�������
	private static int logRemainDays = 7;

	// �Ƿ�д��־�ļ��������д��־�ļ���ʹ�ñ�׼���System.out
	private static boolean writeFile = false;

	/**
	 * �����Ƿ�д��־�ļ�
	 * 
	 * @param writeFile
	 *            trueΪд��־�ļ���falseΪ��׼���
	 */
	public static void setWriteFile(boolean writeFile) {
		Logger.writeFile = writeFile;
	}

	/**
	 * ������־����
	 * 
	 * @param level
	 */
	public static void setLogLevel(Level level) {
		logLevel = level;
	}

	/**
	 * ������־����·����������õ�·����������ʹ��Ĭ��·�������Ĭ��·��Ҳ�Ҳ�����д��־
	 * 
	 * @param path
	 */
	public static void setLogPath(String path) {
		logPath = path;
	}

	/**
	 * ������־�ļ���ǰꡣ���������ʹ��Ĭ��ֵ��crash_��
	 * 
	 * @param prex
	 */
	public static void setLogFilePrex(String prex) {
		m_logNamePrex = prex;
	}

	/**
	 * ������־�ļ���С���ֵ�����ֽ�Ϊ��λ
	 * 
	 * @param size
	 *            �����������־�ļ���С���ֽ���
	 */
	public static void setFileMaxSize(long size) {
		maxFileSize = size;
	}

	/**
	 * ������־�ļ��ı�������
	 * 
	 * @param days
	 */
	public static void setLogRemainDays(int days) {
		logRemainDays = days;
	}

	public static void setOutPut(boolean isOutPut) {
		Logger.isOutPut = isOutPut;
	}

	/**
	 * ��ȡ��־�ļ���������·��
	 * 
	 * @return
	 */
	private static String getLogFileName() {
		if (TextUtils.isEmpty(logPath)) {
			if (TextUtils.isEmpty(FileUtils
					.getgetAbsoluteDir("/interestfriend/crash"))) {
				return "";
			}
			logPath = FileUtils.getgetAbsoluteDir("/interestfriend/crash");
		}
		String fileName = logPath + "/" + m_logNamePrex
				+ DateUtils.getCurrDateStr("yyyyMMdd") + ".log";
		return fileName;
	}

	/**
	 * ������������������־�ļ�
	 */
	private static void clearInvalidLogFile(File logPath) {
		// ��ȡ��������
		Date expiredDate = DateUtils.offsetDate(new Date(),
				Calendar.DAY_OF_MONTH, -logRemainDays);
		String expiredFileName = m_logNamePrex
				+ DateUtils.format(expiredDate, "yyyyMMdd") + ".log";
		File fList[] = logPath.listFiles();
		for (int i = 0; i < fList.length; i++) {
			String fName = StringUtils.substringAfterLast(fList[i].getName(),
					"/");
			if (fName.startsWith(m_logNamePrex)
					&& expiredFileName.compareTo(fName) >= 0) {
				fList[i].delete();
			}
		}
	}

	private static File checkLogFile() {
		String fileName = getLogFileName(); // ��ȡ��־�ļ���
		// �����ȡ��־�ļ���Ϊ�գ���˵��SD��û�гɹ����أ����Բ�д��־
		if (TextUtils.isEmpty(fileName)) {
			return null;
		}
		File filePath = new File(logPath);
		// �����־���·���������򴴽���־·��
		if (!filePath.exists()) {
			filePath.mkdirs();
		}
		File logFile = new File(fileName);
		try {
			if (logFile.isFile() && logFile.length() >= maxFileSize) {
				logFile.delete();
			}
			if (!logFile.exists()) { // �����־�ļ��������������Ѿ������������޵���־�ļ����������µ���־�ļ�
				logFile.createNewFile();
				clearInvalidLogFile(filePath);
			}
		} catch (IOException e) {
			return null;
		}
		return logFile;
	}

	/**
	 * д��־����������
	 * 
	 * @param caller
	 *            �����������
	 * @param msg
	 * @param level
	 */
	private static void writeLog(Object caller, String msg, Level level) {
		String logTag = "";
		if (caller != null) {
			if (caller instanceof String) {
				logTag = caller.toString();
			} else {
				logTag = caller.getClass().getName();
			}
		}
		if (level.getLevel() < logLevel.getLevel()) {
			return;
		}
		if (!writeFile) {
			return;
		}
		File logFile = checkLogFile(); // �����־�ļ�
		if (logFile == null) {
			return;
		}
		/*** ��֯��־�������� **/
		String writeMsg = "[" + DateUtils.getCurrDateStr() + "] [";
		writeMsg += level.getLevelDescn() + "] ";
		writeMsg += logTag + ": ";
		writeMsg += msg + "\n";
		try {
			FileWriter fw = new FileWriter(logFile, true);
			fw.append(writeMsg);
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * ���쳣��Ϣ����ת�����ַ�����Ϣ
	 * 
	 * @param ex
	 * @return
	 */
	private static String throwableToString(Throwable ex) {
		Writer writer = new StringWriter();
		PrintWriter printWriter = new PrintWriter(writer);
		ex.printStackTrace(printWriter);
		Throwable cause = ex.getCause();
		while (cause != null) {
			cause.printStackTrace(printWriter);
			cause = cause.getCause();
		}
		printWriter.close();
		return writer.toString();
	}

	/**
	 * ����
	 * 
	 * @param caller
	 *            �����������
	 * @param msg
	 *            ��־��Ϣ�ַ���
	 */
	public static void debug(Object caller, String msg) {
		writeLog(caller, msg, Level.DEBUG);
	}

	/**
	 * ����
	 * 
	 * @param caller
	 *            �����������
	 * @param e
	 *            �쳣��Ϣ����
	 */
	public static void debug(Object caller, Throwable e) {
		writeLog(caller, throwableToString(e), Level.DEBUG);
	}

	/**
	 * ��Ϣ
	 * 
	 * @param caller
	 *            �����������
	 * @param msg
	 *            ��־��Ϣ�ַ���
	 */
	public static void info(Object caller, String msg) {
		writeLog(caller, msg, Level.INFO);
	}

	/**
	 * ��Ϣ
	 * 
	 * @param caller
	 *            �����������
	 * @param e
	 *            �쳣��Ϣ����
	 */
	public static void info(Object caller, Throwable e) {
		writeLog(caller, throwableToString(e), Level.INFO);
	}

	/**
	 * ����
	 * 
	 * @param caller
	 *            �����������
	 * @param msg
	 *            ��־��Ϣ�ַ���
	 */
	public static void warn(Object caller, String msg) {
		writeLog(caller, msg, Level.WARN);
	}

	/**
	 * ����
	 * 
	 * @param caller
	 *            �����������
	 * @param e
	 *            �쳣��Ϣ����
	 */
	public static void warn(Object caller, Throwable e) {
		writeLog(caller, throwableToString(e), Level.WARN);
	}

	/**
	 * ����
	 * 
	 * @param caller
	 *            �����������
	 * @param msg
	 *            ��־��Ϣ�ַ���
	 */
	public static void error(Object caller, String msg) {
		writeLog(caller, msg, Level.ERROR);
	}

	/**
	 * ����
	 * 
	 * @param caller
	 *            �����������
	 * @param e
	 *            �쳣��Ϣ����
	 */
	public static void error(Object caller, Throwable e) {
		writeLog(caller, throwableToString(e), Level.ERROR);
	}

	/**
	 * ���ش���
	 * 
	 * @param caller
	 *            �����������
	 * @param e
	 *            �쳣��Ϣ����
	 */
	public static void fatal(Object caller, String msg) {
		writeLog(caller, msg, Level.FATAL);
	}

	/**
	 * ���ش���
	 * 
	 * @param caller
	 *            �����������
	 * @param msg
	 *            ��־��Ϣ�ַ���
	 */
	public static void fatal(Object caller, Throwable e) {
		writeLog(caller, throwableToString(e), Level.FATAL);
	}

	/**
	 * ���log��Ϣ
	 * 
	 * @param caller
	 * @param msg
	 * @param level
	 */
	public static void out(Object caller, String msg, Level level) {
		outLog(caller, msg, level);
	}

	/**
	 * ���log��Ϣ
	 * 
	 * @param caller
	 * @param e
	 * @param level
	 */
	public static void out(Object caller, Throwable e, Level level) {
		outLog(caller, throwableToString(e), level);
	}

	/**
	 * д��־����������
	 * 
	 * @param caller
	 *            �����������
	 * @param msg
	 * @param level
	 */
	private static void outLog(Object caller, String msg, Level level) {
		String logTag = "";
		if (caller != null) {
			if (caller instanceof String) {
				logTag = caller.toString();
			} else {
				logTag = caller.getClass().getName();
			}
		}
		if (level.getLevel() < logLevel.getLevel()) {
			return;
		}
		if (!isOutPut) {
			return;
		}
		/*** ��֯������� **/
		String outMsg = "[" + DateUtils.getCurrDateStr() + "] [";
		outMsg += level.getLevelDescn() + "] ";
		outMsg += logTag + ": ";
		outMsg += msg + "\n";
		Utils.print(outMsg);
	}
}
