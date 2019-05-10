package nicelee.file;

import java.io.File;
import java.util.regex.Pattern;

public class FileUtil {

	/**
	 * 将folder 文件夹下的文件按照规则重命名
	 * 
	 * @param folder           目标文件夹
	 * @param doWithAllFolders 是否遍历子文件夹(尚未启用)
	 * @param isRegx           是否是正则
	 * @param target           目标字符串
	 * @param replacement      替换字符串
	 */
	public static void rename(File folder, boolean doWithAllFolders, boolean isRegx, String target,
			String replacement) {
		Printer.println("替换开始");
		for (File file : folder.listFiles()) {
			String newName = null;
			if (isRegx) {
				newName = file.getName().replaceAll(target, replacement);
			} else {
				newName = file.getName().replace(target, replacement);
			}
			File newFile = new File(file.getParentFile(), newName);
			file.renameTo(newFile);
		}
		Printer.println("替换完毕");
	}

	/**
	 * 为符合条件的文件新建同名文件夹，并移入其中
	 * @param folder 目标文件夹
	 * @param isRegx 是否是正则(否则判断为是否包含 目标字符串)
	 * @param target 目标字符串
	 */
	public static void createFolderAndMove(File folder, boolean isRegx, String target) {
		Printer.println("开始为符合条件的文件新建文件夹，并移入其中");
		Pattern patternTarget = Pattern.compile(target);
		Pattern patternSuffix = Pattern.compile("\\.[^\\.]+$");
		for (File file : folder.listFiles()) {
			// 仅针对文件
			if(file.isFile()) {
				String fileName = file.getName();
				Printer.println(fileName);
				// 如果符合匹配条件
				if ((isRegx && patternTarget.matcher(fileName).find()) 
						|| (!isRegx && fileName.contains(target))) {
					// 新建文件夹
					File newFolder = new File(folder, patternSuffix.matcher(fileName).replaceFirst(""));
					Printer.println(newFolder.getAbsolutePath());
					if(!newFolder.exists()) {
						newFolder.mkdir();
					}
					// 文件移入新文件夹
					file.renameTo(new File(newFolder, fileName));
				}
			}
		}
		Printer.println("动作完毕");
	}
	
	
	/**
	 * 列出目标文件夹下 符合条件的文件夹名称
	 * @param folder 目标文件夹
	 * @param isRegx 是否是正则(否则判断为是否包含 目标字符串)
	 * @param target 目标字符串
	 * @param isList 列出符合条件的/不包含该条件的
	 * @param isAbsPath 打印绝对路径
	 * @param isDealName 是否对文件名进行处理(isAbsPath = false 时生效)
	 * @param replacement 对文件名的替换字符串(isAbsPath = false 时生效)
	 */
	public static void list(File folder, boolean isRegx, String target, boolean isList, boolean isAbsPath, boolean isDealName, String replacement) {
		Printer.clear();
		Printer.println("列表开始");
		Pattern patternTarget = Pattern.compile(target);
		for (File file : folder.listFiles()) {
			// 仅针对文件夹
			if(file.isDirectory()) {
				boolean isValid = isValid(file, isRegx, patternTarget, target);
				isValid = isList ? isValid : !isValid;
				// 如果符合条件，打印相关内容
				if(isValid) {
					String strToPrint = null;
					if(isAbsPath) {
						strToPrint = file.getAbsolutePath();
					}else if(!isDealName) {
						strToPrint = file.getName();
					}else {
						if (isRegx) {
							strToPrint = file.getName().replaceAll(target, replacement);
						} else {
							strToPrint = file.getName().replace(target, replacement);
						}
					}
					Printer.println(strToPrint);
				}
			}
		}
		Printer.println("完毕");
	}
	
	private static boolean isValid(File folder, boolean isRegx, Pattern patternTarget, String target) {
		for (File file : folder.listFiles()) {
			String fileName = file.getName();
			// 如果符合匹配条件
			if ((isRegx && patternTarget.matcher(fileName).find()) 
					|| (!isRegx && fileName.contains(target))) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 列出folder 下所有小于size M的文件夹
	 * @param folder
	 * @param size
	 */
	public static void listEmptyFile(File folder, int size) {
		Printer.clear();
		for (File file : folder.listFiles()) {
			if (file.isDirectory() && countSize(file) <= size * 1024 * 1024) {
				Printer.println(file.getAbsolutePath());
			}
		}
	}

	private static long countSize(File folder) {
		long size = 0;
		for (File file : folder.listFiles()) {
			size += file.length();
		}
		return size;
	}

}
