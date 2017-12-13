package com.dlbase.pinyin;


import java.util.Comparator;

import com.dlbase.util.DLStringUtil;

import net.sourceforge.pinyin4j.PinyinHelper;

public class DLLanguageComparator_CN implements Comparator<String> {
	

	public int compare(String ostr1, String ostr2) {

		if(DLStringUtil.isEmpty(ostr1) ||DLStringUtil.isEmpty(ostr2)){
			return 0;
		}
		
		for (int i = 0; i < ostr1.length() && i < ostr2.length(); i++) {

			int codePoint1 = ostr1.charAt(i);
			int codePoint2 = ostr2.charAt(i);
			if (Character.isSupplementaryCodePoint(codePoint1)
					|| Character.isSupplementaryCodePoint(codePoint2)) {
				i++;
			}
			if (codePoint1 != codePoint2) {
				if (Character.isSupplementaryCodePoint(codePoint1)
						|| Character.isSupplementaryCodePoint(codePoint2)) {
					return codePoint1 - codePoint2;
				}
				String pinyin1 = pinyin((char) codePoint1);
				String pinyin2 = pinyin((char) codePoint2);

				if (pinyin1 != null && pinyin2 != null) { 
					if (!pinyin1.equals(pinyin2)) {
						return pinyin1.compareTo(pinyin2);
					}
				} else {
					return codePoint1 - codePoint2;
				}
			}
		}
		return ostr1.length() - ostr2.length();
	}

	private String pinyin(char c) {
		String[] pinyins = PinyinHelper.toHanyuPinyinStringArray(c);
		if (pinyins == null) {
			return null;
		}
		return pinyins[0];
	}

}
