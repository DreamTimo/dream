package com.dlbase.dateutil;

/**
 * Numeric Wheel adapter.
 */
public class DLNumericWheelAdapter implements DLWheelAdapter {

	// Values
	private int minValue ;
	private int maxValue ;

	// format
	private String format;

	private String values = null;

	/**
	 * Constructor
	 * 
	 * @param minValue
	 *            the wheel min value
	 * @param maxValue
	 *            the wheel max value
	 */
	public DLNumericWheelAdapter(int minValue, int maxValue) {
		this.minValue = minValue;
		this.maxValue = maxValue;
	}

	/**
	 * Constructor
	 * 
	 * @param minValue
	 *            the wheel min value
	 * @param maxValue
	 *            the wheel max value
	 * @param format
	 *            the format string
	 */
	public DLNumericWheelAdapter(int minValue, int maxValue, String format) {
		this.minValue = minValue;
		this.maxValue = maxValue;
		this.format = format;
	}

	public String getItem(int index) {
		if (index >= 0 && index < getItemsCount()) {
			int value = minValue + index;
			values = (format != null ? String.format(format, value) : Integer
					.toString(value));
			setValue(values);
			return values;
		}
		return null;
	}

	public String getValues() {
		return values;
	}

	public void setValue(String value) {
		this.values = value;
	}

	public int getItemsCount() {
		return maxValue - minValue + 1;
	}

	// 得到最大项目长度。它是用来确定轮宽度。
	// 如果返回1,将使用默认轮宽度

	public int getMaximumLength() {
		int max = Math.max(Math.abs(maxValue), Math.abs(minValue));
		@SuppressWarnings("unused")
		int maxLen = Integer.toString(max).length();
		if (minValue < 0) {
			maxLen++;
		}
		return maxValue - minValue + 1;
	}

	public int getMinValue() {
		return minValue;
	}

	public int getMaxValue() {
		return maxValue;
	}

}
