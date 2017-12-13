package com.dlbase.Model;

import com.dlbase.base.DLBaseModel;
import com.dlbase.util.DLFileUtil;
import com.luyz.dlbaselib.R;

public class DLFileModel extends DLBaseModel{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4045169286906900140L;

	public static enum TFileType{
		EFile_Default,
		EFile_Word,
		EFile_Excel,
		EFile_Ppt,
		EFile_Pdf,
		EFile_Image,
		EFile_Gif,
		EFile_Video,
		EFile_Voice,
		EFile_Zip,
		EFile_Other
	}
	
	private String filePath;
	private String fileUrl;
	private String fileSize;
	private String fileName;
	private String fileTime;
	private String fileExtension;
	private String fileTypeIcon;
	
	private TFileType fileType;
	//添加
	private boolean isAdd;
	//选中
	private boolean check;
	//是否下载 0 未下载 1下载中 2 已下载
	private int downState=0;
	
	private String userId;
	
	public DLFileModel(){
		isAdd = true;
		check = false;
		fileType = TFileType.EFile_Default;
		downState=0;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public String getFileUrl() {
		return fileUrl;
	}

	public void setFileUrl(String fileUrl) {
		this.fileUrl = fileUrl;
		
	}

	public String getFileSize() {
		return fileSize;
	}

	public void setFileSize(String fileSize) {
		this.fileSize = fileSize;
	}

	public TFileType getFileType() {
		return fileType;
	}

	public void setFileType(TFileType fileType) {
		this.fileType = fileType;
		switch (fileType) {
		case EFile_Word:
			setFileTypeIcon(R.drawable.file_word +"");
			 break;
		case EFile_Excel:
			setFileTypeIcon(R.drawable.file_excel +"");
			 break;
		case EFile_Ppt:
			setFileTypeIcon(R.drawable.file_ppt +"");
			 break;
		case EFile_Pdf:
			setFileTypeIcon(R.drawable.file_pdf +"");
			 break;
		case EFile_Image:
			setFileTypeIcon(R.drawable.file_image +"");
			 break;
		case EFile_Gif:
			setFileTypeIcon(R.drawable.file_gif +"");
			 break;
		case EFile_Video:
			setFileTypeIcon(R.drawable.file_video +"");
			 break;
		case EFile_Voice:
			setFileTypeIcon(R.drawable.file_voice +"");
			 break;
		case EFile_Zip:
			setFileTypeIcon(R.drawable.file_zip +"");
			 break;
		case EFile_Other:
			setFileTypeIcon(R.drawable.file_other +"");
			 break;
		default:
			
			break;
		}
		
	}

	public boolean isAdd() {
		return isAdd;
	}

	public void setAdd(boolean isAdd) {
		this.isAdd = isAdd;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
		
		setFileExtension(DLFileUtil.getExtension(fileName));
	}

	public boolean isCheck() {
		return check;
	}

	public void setCheck(boolean check) {
		this.check = check;
	}

	public String getFileTime() {
		return fileTime;
	}

	public void setFileTime(String fileTime) {
		this.fileTime = fileTime;
	}

	public String getFileExtension() {
		return fileExtension;
	}

	public void setFileExtension(String fileExtension) {
		this.fileExtension = fileExtension;

		if(fileExtension.equals("doc")){
			setFileType(TFileType.EFile_Word);
		}else if(fileExtension.equals("docx")){
			setFileType(TFileType.EFile_Word);
		}else if(fileExtension.equals("xls")){
			setFileType(TFileType.EFile_Excel);
		}else if(fileExtension.equals("xlsx")){
			setFileType(TFileType.EFile_Excel);
		}else if(fileExtension.equals("ppt")){
			setFileType(TFileType.EFile_Ppt);
		}else if(fileExtension.equals("pptx")){
			setFileType(TFileType.EFile_Ppt);
		}else if(fileExtension.equals("pdf")){
			setFileType(TFileType.EFile_Pdf);
		}else if(fileExtension.equals("png")){
			setFileType(TFileType.EFile_Image);
		}else if(fileExtension.equals("jpg")){
			setFileType(TFileType.EFile_Image);
		}else if(fileExtension.equals("jpeg")){
			setFileType(TFileType.EFile_Image);
		}else if(fileExtension.equals("gif")){
			setFileType(TFileType.EFile_Gif);
		}else if(fileExtension.equals("mp4")){
			setFileType(TFileType.EFile_Video);
		}else if(fileExtension.equals("amr")){
			setFileType(TFileType.EFile_Voice);
		}else if(fileExtension.equals("mp3")){
			setFileType(TFileType.EFile_Voice);
		}else if(fileExtension.equals("zip")){
			setFileType(TFileType.EFile_Zip);
		}else if(fileExtension.equals("rar")){
			setFileType(TFileType.EFile_Zip);
		}else{
			setFileType(TFileType.EFile_Other);
		}
	}

	public String getFileTypeIcon() {
		return fileTypeIcon;
	}

	public void setFileTypeIcon(String fileTypeIcon) {
		this.fileTypeIcon = fileTypeIcon;
	}

	public int getDownState() {
		return downState;
	}

	public void setDownState(int downState) {
		this.downState = downState;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	
}
