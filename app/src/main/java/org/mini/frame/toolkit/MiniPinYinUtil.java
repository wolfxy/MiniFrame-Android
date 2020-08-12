package org.mini.frame.toolkit;

import android.text.TextUtils;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Wuquancheng on 15/7/18.
 */
public class MiniPinYinUtil {

    private static MiniPinYinUtil instance;

    private MiniPinYinUtil() {
    }

    public static MiniPinYinUtil instance() {
        synchronized (MiniPinYinUtil.class) {
            if (instance == null) {
                instance = new MiniPinYinUtil();
            }
        }
        return instance;
    }

    /**
     * 获取全拼
     *
     * @param input 霍永刚   huoyongggang
     * @return
     */
    public String getPinYinForAll(String input) {
        if (MiniStringUtil.isEmpty(input)) {
            return null;
        }
        char[] chars = input.toCharArray();
        StringBuffer stringBuffer = new StringBuffer();
        HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
        format.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        for (int i = 0; i < chars.length; i++) {
            try {
                boolean english = isEnglish(String.valueOf(chars[i]));
                if (english){
                    stringBuffer.append(String.valueOf(chars[i]).toLowerCase());
                }else{
                    String[] pinyinArray = PinyinHelper.toHanyuPinyinStringArray(chars[i], format);
                    if (pinyinArray != null) {
                        stringBuffer.append(pinyinArray[0]);
                    }
                }
            } catch (BadHanyuPinyinOutputFormatCombination badHanyuPinyinOutputFormatCombination) {
                badHanyuPinyinOutputFormatCombination.printStackTrace();
            }

        }
        return stringBuffer.toString();
    }


    public static boolean isEnglish(String charaString) {
        return charaString.matches("^[a-zA-Z]*");
    }

    /**
     * 获取名字首字母
     *
     * @param input 霍永刚  hyg
     * @return
     */
    public String getPinYinFirstLetter(String input) {
        if (MiniStringUtil.isEmpty(input)) {
            return null;
        }
        char[] chars = input.toCharArray();
        StringBuffer sb = new StringBuffer();
        HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
        format.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        for (int i = 0; i < chars.length; i++) {
            try {
                boolean english = isEnglish(String.valueOf(chars[i]));
                if (english){
                    sb.append(String.valueOf(chars[i]).toLowerCase());
                }else{
                    String[] pinyinArray = PinyinHelper.toHanyuPinyinStringArray(chars[i], format);
                    if (pinyinArray != null) {
                        sb.append(pinyinArray[0].substring(0, 1));
                    }
                }
            } catch (BadHanyuPinyinOutputFormatCombination badHanyuPinyinOutputFormatCombination) {
                badHanyuPinyinOutputFormatCombination.printStackTrace();
            }
        }
        return sb.toString();
    }

    /**
     * 获取名字组合
     *
     * @param input
     * @return
     */
    public String getPinYinForGroup(String input) {
        if (MiniStringUtil.isEmpty(input)) {
            return null;
        }
        char[] chars = input.toCharArray();
        StringBuffer sb = new StringBuffer();
        HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
        format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        for (int i = 0; i < chars.length; i++) {
            try {
                String[] pinyinArray = PinyinHelper.toHanyuPinyinStringArray(chars[i], format);
                if (pinyinArray != null) {
                    if (!TextUtils.isEmpty(pinyinArray[0])) {
                        sb.append(pinyinArray[0]);
                    }
                }
            } catch (BadHanyuPinyinOutputFormatCombination badHanyuPinyinOutputFormatCombination) {
                badHanyuPinyinOutputFormatCombination.printStackTrace();
            }
        }
        return sb.toString();
    }

    /**
     * 得到单字拼音数组
     *
     * @param input
     * @return 返回名称的数组  霍永刚  huo  yong  gang
     */
    public List<String> getPinYinForSingle(String input) {
        if (MiniStringUtil.isEmpty(input)) {
            return null;
        }
        char[] chars = input.toCharArray();
        List<String> list = new ArrayList<String>();
        HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
        format.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        for (int i = 0; i < chars.length; i++) {
            try {
                boolean english = isEnglish(String.valueOf(chars[i]));
                if (english){
                    list.add(String.valueOf(chars[i]).toLowerCase());
                }else{
                    String[] pinyinArray = PinyinHelper.toHanyuPinyinStringArray(chars[i], format);
                    if (pinyinArray != null) {
                        list.add(pinyinArray[0]);
                    }
                }
            } catch (BadHanyuPinyinOutputFormatCombination badHanyuPinyinOutputFormatCombination) {
                badHanyuPinyinOutputFormatCombination.printStackTrace();
            }

        }
        return list;
    }

    public List<String> getPinYinForSearch(String input) {
        if (MiniStringUtil.isEmpty(input)) {
            return null;
        }
        List<String> list = new ArrayList<>();
        list.add(getPinYinForAll(input));
        list.add(getPinYinFirstLetter(input));

        List<String> single = getPinYinForSingle(input);
        if (single != null) {
            for (int i = 0; i < single.size(); i++) {
                list.add(single.get(i));
            }
        }
//        for (int i = 1; i < input.length() - 1; i++) {
//            list.add(getPinYinForGroup(input.substring(i, input.length())));
//        }

        return list;
    }
}
