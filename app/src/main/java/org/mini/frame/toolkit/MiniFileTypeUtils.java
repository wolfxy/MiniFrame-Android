package org.mini.frame.toolkit;

/**
 * 判断文件类型
 * 
 * @author gassion
 * 
 */
public class MiniFileTypeUtils {



	public static String getFileType(String path) {
		// 文件后缀
		String type = null;

		String ext = path.substring(path.lastIndexOf("."));

		// 判断常用文件类型
		// word
		if (ext.toLowerCase().equals(".doc") || ext.toLowerCase().equals(".docx")) {
			type = "Word文件";
		}
		// excel
		else if (ext.toLowerCase().equals(".xls") || ext.toLowerCase().equals(".xlsx")) {
			type = "Excel文件";
		}
		// ppt
		else if (ext.toLowerCase().equals(".ppt") || ext.toLowerCase().equals(".pptx")) {
			type = "PPT文件";
		}
		// 压缩包
		else if (ext.toLowerCase().equals(".rar") || ext.toLowerCase().equals(".zip")) {
			type = "压缩文件";
		} else if (ext.toLowerCase().equals(".jpg") || ext.toLowerCase().equals(".jpeg")
				|| ext.toLowerCase().equals(".bmp") || ext.toLowerCase().equals(".png")
				|| ext.toLowerCase().equals(".gif")) {
			type = "图片文件";
		} else {
			type = "附件";
		}
		return type;
	}


    public static String getSuffix(String filename) {
        int dix = filename.lastIndexOf('.');
        if (dix < 0) {
	  return "";
        } else {
	  return filename.substring(dix + 1);
        }
    }

    public static String getFileType() {
        String fileTypeName =
	      ".3gp;.apk;.asf;.avi;.bin;.bmp;.c;.class;.conf;.cpp;.doc;.docx;.xls;.java;.png;.txt"
		    + ".xlsx;.exe;.gif;.gtar;.gz;.h;.htm;.html;.jar;.js;.m4a;.m4b;.m4p;.rar;"
		    + ".m4u;.mp3;.mpe;.mpga;.pdf;.ppt;.pptx;.rc;.tar;.wav;.wps;.zip;.jpg;.jpeg;.amr;.xml;";
        return fileTypeName;
    }


}
