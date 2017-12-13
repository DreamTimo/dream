package com.dlbase.pinyin;

import com.dlbase.util.DLStringUtil;

import net.sourceforge.pinyin4j.PinyinHelper;

public class DLAssortPinyinList {

	private DLHashList<String,DLPinyinModel> hashList=new DLHashList<String,DLPinyinModel>(new DLKeySort<String,DLPinyinModel>(){

		@Override
		public String getKey(DLPinyinModel v) {
			// TODO Auto-generated method stub
			String tempStr = null;
			if(DLStringUtil.notEmpty(v.getName())){
				tempStr = v.getName();
			}
			
			return getFirstChar(tempStr);
		}});
	
		public  String getFirstChar(String value) {
			if(DLStringUtil.isEmpty(value)){
				return null;
			}
			char firstChar = value.charAt(0);
			String first = null;
			String last = null;
			String[] print = PinyinHelper.toHanyuPinyinStringArray(firstChar);

			if (print == null) {

				if ((firstChar >= 97 && firstChar <= 122)) {
					firstChar -= 32;
				}
				if (firstChar >= 65 && firstChar <= 90) {
					first = String.valueOf((char) firstChar);
				} else {
					first = "↑";
				}
			} else {
				first = String.valueOf((char)(print[0].charAt(0) -32));
			}
			if (last == null) {
				last = "#";
			}
			return first;
		}

		public DLHashList<String, DLPinyinModel> getHashList() {
			return hashList;
		}
		
		

}
