package com.dlbase.pickutil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

import com.dlbase.util.DLArrayUtil;
import com.dlbase.util.DLStringUtil;


public class DLPickerDataSource implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1709391596543857957L;

	private int pickComponentsCount = 3;
	
	private int firstIndex = 0;
	private int secondIndex = 0;
	private int threeIndex = 0;
	
	private int defaultFirstIndex = 0;
	private int defaultSecondIndex = 0;
	private int defaultThreeIndex = 0;
	
	private ArrayList<DLPickerItemModel> firstArray = null;
	private HashMap<String, ArrayList<DLPickerItemModel>> secondArray = null;
	private HashMap<String, ArrayList<DLPickerItemModel>> threeArray = null;
	
	public DLPickerDataSource(){
		firstArray  = new ArrayList<DLPickerItemModel>();
		secondArray = new HashMap<String, ArrayList<DLPickerItemModel>>();
		threeArray  = new HashMap<String, ArrayList<DLPickerItemModel>>();
	}
	
	public void setDefaultData(String firstValue,String secondValue,String threeValue){
		
		if(pickComponentsCount == 1){
			int defirst = getDefaultIndex(firstValue, firstArray);
			if(defirst>-1){
				setDefaultFirstIndex(defirst);
			}
		}else if(pickComponentsCount == 2){
			int defirst = getDefaultIndex(firstValue, firstArray);
			if(defirst>-1){
				setDefaultFirstIndex(defirst);
			}
			int desecond = -1;
			ArrayList<DLPickerItemModel> tempSecArray = secondArray.get(firstValue);
			if(tempSecArray!=null){
				desecond = getDefaultIndex(secondValue, tempSecArray);
			}
			if(desecond>-1){
				setDefaultSecondIndex(desecond);
			}
		}else if(pickComponentsCount == 3){
			int defirst = getDefaultIndex(firstValue, firstArray);
			if(defirst>-1){
				setDefaultFirstIndex(defirst);
			}
			int desecond = -1;
			ArrayList<DLPickerItemModel> tempSecArray = secondArray.get(firstValue);
			if(tempSecArray!=null){
				desecond = getDefaultIndex(secondValue, tempSecArray);
			}
			if(desecond>-1){
				setDefaultSecondIndex(desecond);
			}
			int dethree = -1;
			ArrayList<DLPickerItemModel> tempThrArray = threeArray.get(secondValue);
			if(tempThrArray!=null){
				dethree = getDefaultIndex(threeValue, tempThrArray);
			}
			if(dethree>-1){
				setDefaultThreeIndex(dethree);
			}
		}
	}
	
	public int getDefaultIndex(String value,ArrayList<DLPickerItemModel> itemArray){
		int defaultIndex = -1;
		
		if(DLStringUtil.notEmpty(value)){
			for (int i = 0; i < itemArray.size(); i++) {
				DLPickerItemModel tempItem = itemArray.get(i);
				if(tempItem!=null){
					if(tempItem.getPickName().equals(value) || tempItem.getPickId().equals(value)){
						defaultIndex = i;
						break;
					}
				}
			}
		}
		
		return defaultIndex;
	}
	
	public ArrayList<String> getFirstData() {
		ArrayList<String> result = new ArrayList<String>();
		if(DLArrayUtil.notEmpty(firstArray)){
			for (int i = 0; i < firstArray.size(); i++) {
				result.add(firstArray.get(i).getPickName());
			}
		}
		return result;
	}
	
	public ArrayList<String> getSecondData(String key){
		ArrayList<String> result = new ArrayList<String>();
		
		ArrayList<DLPickerItemModel> tempArray = getSecondArray(key);
		if(DLArrayUtil.notEmpty(tempArray)){
			for (int i = 0; i < tempArray.size(); i++) {
				result.add(tempArray.get(i).getPickName());
			}
		}
	
		return result;
	}
	
	public ArrayList<String> getThreeData(String key){
		ArrayList<String> result = new ArrayList<String>();
		ArrayList<DLPickerItemModel> tempArray = getThreeArray(key);
		if(DLArrayUtil.notEmpty(tempArray)){
			for (int i = 0; i < tempArray.size(); i++) {
				result.add(tempArray.get(i).getPickName());
			}
		}
		return result;
	}
	
	public DLPickerItemModel getFirstItem(int index){
		if(DLArrayUtil.notEmpty(firstArray)){
			return firstArray.get(index);
		}
		return null;
	}
	
	public ArrayList<DLPickerItemModel> getSecondArray(String key){
		return secondArray.get(key);
	}
	
	public DLPickerItemModel getSecondItem(String key,int index){
		
		ArrayList<DLPickerItemModel> tempArray = getSecondArray(key);
		
		if(DLArrayUtil.notEmpty(tempArray)){
			return tempArray.get(index);
		}
		return null;
	}
	
	public ArrayList<DLPickerItemModel> getThreeArray(String key){
		return threeArray.get(key);
	}
	
	public DLPickerItemModel getThreeItem(String key,int index){
		
		ArrayList<DLPickerItemModel> tempArray = getThreeArray(key);
		
		if(DLArrayUtil.notEmpty(tempArray)){
			return tempArray.get(index);
		}
		return null;
	}
	
	public DLPickerItemModel getFirstResult(){
		return getFirstItem(getFirstIndex());
	}
	
	public DLPickerItemModel getSecondResult(){
		return getSecondItem(getFirstResult().getPickName(), getSecondIndex());
	}
	
	public DLPickerItemModel getThreeResult(){
		return getThreeItem(getSecondResult().getPickName(), getThreeIndex());
	}

	public int getPickComponentsCount() {
		return pickComponentsCount;
	}

	public void setPickComponentsCount(int pickComponentsCount) {
		this.pickComponentsCount = pickComponentsCount;
	}

	public int getFirstIndex() {
		return firstIndex;
	}

	public void setFirstIndex(int firstIndex) {
		this.firstIndex = firstIndex;
	}

	public int getSecondIndex() {
		return secondIndex;
	}

	public void setSecondIndex(int secondIndex) {
		this.secondIndex = secondIndex;
	}

	public int getThreeIndex() {
		return threeIndex;
	}

	public void setThreeIndex(int threeIndex) {
		this.threeIndex = threeIndex;
	}

	public int getDefaultFirstIndex() {
		return defaultFirstIndex;
	}

	public void setDefaultFirstIndex(int defaultFirstIndex) {
		this.defaultFirstIndex = defaultFirstIndex;
		setFirstIndex(defaultFirstIndex);
	}

	public int getDefaultSecondIndex() {
		return defaultSecondIndex;
	}

	public void setDefaultSecondIndex(int defaultSecondIndex) {
		this.defaultSecondIndex = defaultSecondIndex;
		setSecondIndex(defaultSecondIndex);
	}

	public int getDefaultThreeIndex() {
		return defaultThreeIndex;
	}

	public void setDefaultThreeIndex(int defaultThreeIndex) {
		this.defaultThreeIndex = defaultThreeIndex;
		setThreeIndex(defaultThreeIndex);
	}

	public ArrayList<DLPickerItemModel> getFirstArray() {
		return firstArray;
	}

	public void setFirstArray(ArrayList<DLPickerItemModel> firstArray) {
		this.firstArray = firstArray;
	}

	public HashMap<String, ArrayList<DLPickerItemModel>> getSecondArray() {
		return secondArray;
	}

	public void setSecondArray(HashMap<String, ArrayList<DLPickerItemModel>> secondArray) {
		this.secondArray = secondArray;
	}

	public HashMap<String, ArrayList<DLPickerItemModel>> getThreeArray() {
		return threeArray;
	}

	public void setThreeArray(HashMap<String, ArrayList<DLPickerItemModel>> threeArray) {
		this.threeArray = threeArray;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	
}
