package com.cybersoft4u.xian.iatoms.common.web.taglib;

import java.util.List;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.util.StringUtils;

/**
 * Purpose: 
 * @author cybersoft
 * @since  JDK 1.6
 * @date   2016/5/20
 * @MaintenancePersonnel cybersoft
 */
public class FileuploadTag extends TagSupport {
	
	private static final String DEFUALT_ID					= "qq-template"; 
	private static final String DEFUALT_SHOW_NAME			= "選擇檔案...";	
	/**
	 * id
	 */
	private String id;
	/**
	 * 名稱
	 */
	private String name;
	/**
	 * 上傳控件顯示的名字--默認為選擇檔案...
	 */
	private String showName;	
	/**
	 * 上傳的Url
	 */
	private String uploadUrl;
	/**
	 * deleteUrl
	 */
	private String deleteUrl;
	/**
	 * 上傳的參數
	 */
	private String uploadParams;
	/**
	 * 刪除上傳文件的參數
	 */
	private String deleletParams;
	/**
	 * 允許上傳的格式
	 */
	private String  allowedExtensions;	
	/**
	 * 文件大小超過最大限度提示信息
	 */
	private String sizeErrorMessage;
	/**
	 * 
	 * 允許上傳的長度
	 */
	private Integer sizeLimit = 10240000;
	/**
	 * 文件上傳最小長度
	 */
	private Integer minSizeLimit = 0;
	/**
	 * 是否允許上傳多個文件
	 */
	private boolean multiple = false;
	/**
	 * 允許同時上傳的最大個數,默認3
	 */
	private Integer maxConnections = 3;
	/**
	 * 是否用於匯入
	 */
	private boolean whetherImport = false;	
	/**
	 * 是否顯示文件列表
	 */
	 private boolean showFileList = false;	 
	 /**
	  * 是否可以刪除
	  */
	 private boolean whetherDelete = true;	 
	 /**
	  * 自定義方法
	  */
	 private String javaScript;
	 /**
	  * 是否自动上传
	  */
	 private boolean autoUpload = true;
	 
	 private boolean whetherDownLoad = false;
	 /**
	  * 已上傳的文件集合
	  */
	 private List<FileUpLoad> uploadedFilelist;
	 /**
	  * 錯誤信息是否彈出
	  */
	 private boolean messageAlert = true;
	 
	 private boolean showUnderline = false;
	
	 private String width;
	 
	 /**
	  * 錯誤信息ID
	  */
	 private String messageId;
	 /**
	  * 可接受的文件
	  */
	 private String acceptFiles;
	 
	 /**
	  * 是否自定義error事件
	  */
	 private boolean isCustomError = false;
	 /**
	  * 錯誤的格式提示消息
	  */
	 private String errorMsg;
	 
	/**
	 * 
	 * Constructor:
	 */
	public FileuploadTag() {
		super();
	}
	
	public int doStartTag() throws JspException {
		try {
			if(!StringUtils.hasText(id)) {
				throw new JspTagException("id is null");
			}
			JspWriter out = pageContext.getOut();
			StringBuffer buffer = new StringBuffer();
			
			if (this.whetherImport) {
				buffer.append("<style type=\"text/css\">");
				buffer.append(".qq-upload-button {");
				buffer.append("display:block;lineheight:17px!important;lineheight:15px;padding: 0 0!important;border-bottom:0px!important;");
				buffer.append("text-align:center;  background:transparent!important;color:blue!important;");
				buffer.append("}");
				buffer.append(".qq-upload-button-hover {}");
				buffer.append(".qq-upload-button-focus {}");
				buffer.append("</style>");
			}
			buffer.append("<div ");
			if (this.whetherImport) {
				buffer.append("style=\"display: inline-block;\"");
			}
			buffer.append(">");
			buffer.append("<script type=\"text/template\" id=\"")
					.append(FileuploadTag.DEFUALT_ID)
					.append("_").append(this.id).append("\">");
			buffer.append("<div class=\"qq-uploader-selector qq-uploader\" >");		
			
			buffer.append(" <div class=\"qq-upload-button-selector qq-upload-button\" ");
			if (this.whetherImport) {
				buffer.append("style=\" width: ");
				if (StringUtils.hasText(this.width)) {
					buffer.append(this.width);
				} else {
					buffer.append("auto");
				}
				buffer.append(" ! important; position: relative; top:4px; overflow: hidden; direction: ltr;\"");
			}
			buffer.append(">");
			buffer.append(" <div id='showName'>");
			if (StringUtils.hasText(this.showName)) {
				if (showUnderline) {
					buffer.append(" <u>");
					buffer.append(this.showName);
					buffer.append(" </u>");
				} else {
					buffer.append(this.showName);
				}
			} else {
				buffer.append(FileuploadTag.DEFUALT_SHOW_NAME);
			}
			buffer.append("</div>");
			buffer.append("</div>");
			buffer.append(" <div class=\"qq-upload-list-selector qq-upload-list\"");
			
			//不顯示列表
			if (this.whetherImport || !this.showFileList) {
				buffer.append( "style = \"display:none \"");
			}
			buffer.append(" >");
			buffer.append("<div class=\"qq-upload-success\" style=\"border-bottom-color: gray; border-bottom-width: 1px; border-bottom-style: dotted; \">");
			
			buffer.append("<a class=\"qq-upload-file-selector qq-upload-file ucFileUploaderLinkButton\"></a>");
			buffer.append("<a class=\"cyber-imagebutton qq-upload-delete-selector qq-upload-delete\" id=\"btnDeleted\" href=\"#\">刪除</a>");
			buffer.append("<span class=\"qq-upload-spinner-selector qq-upload-spinner\"></span>");
		
			buffer.append("<a class=\"qq-upload-cancel-selector qq-upload-cancel\" href=\"#\">取消</a>");
			buffer.append("<span class=\"qq-upload-failed-text\" style=\"color: red;\">失敗</span>");
			buffer.append("</div>");
			buffer.append("</div>");
			buffer.append("</div>");
			buffer.append("</script>");
			
			buffer.append("<div id=\"").append(this.id).append("\"></div>");
			
			//已上傳的文件
			if (!this.whetherImport && !CollectionUtils.isEmpty(this.uploadedFilelist)) {
				buffer.append("<div class=\"qq-upload-list-selector qq-upload-list\"");
				if (!this.showFileList) {
					buffer.append(" style=\"display:none;\"");
				}
				buffer.append(">");
				if (!CollectionUtils.isEmpty(uploadedFilelist)) {
					for (FileUpLoad fileUpLoad : uploadedFilelist) {						
						buffer.append("<div class=\"qq-upload-success\" style=\"border-bottom-color: gray; border-bottom-width: 1px; border-bottom-style: dotted;\">");
						buffer.append("<a class=\"qq-upload-file ucFileUploaderLinkButton\" onclick = \"delFile(this);\" href=\"#\">刪除</a> <br/>");
						
						buffer.append("<a class=\"qq-upload-file ucFileUploaderLinkButton\" href=\"#\"  onclick=\"downloadFile('").append(fileUpLoad.getFileValue()).append("','").append(fileUpLoad.getFilePath());
						buffer.append("')\">").append(fileUpLoad.getFileName());
						buffer.append("</a>");
						buffer.append("<input type=\"hidden\" class=\"filePath\" value=\"").append(fileUpLoad.getFileValue()).append("\"/>");
						buffer.append("<span class=\"qq-upload-size-selector qq-upload-size\">").append(fileUpLoad.getSize()).append("</span>");
						buffer.append("</div>");
					}
				}
				buffer.append("</div>");
			}		
			
			buffer.append(" <script> var " + this.id + ";");
			buffer.append(" $(document).ready(function() { ");
			buffer.append(" ").append(this.id).append(" = new qq.FineUploader({ ");
			if (!messageAlert) {
				buffer.append(" messageAlert:false,");
			}
			buffer.append(" debug: false,element: document.getElementById('").append(this.id).append("'),");
			buffer.append(" template: \"").append(FileuploadTag.DEFUALT_ID).append("_").append(this.id).append("\",");
			buffer.append(" request: {");
			buffer.append("  endpoint:\"").append(this.uploadUrl).append("\",");
			if (StringUtils.hasText(this.uploadParams)) {
				buffer.append(" params:").append(this.uploadParams);
			}
			buffer.append(" },");
			
			buffer.append(" validation: { ");
			if (StringUtils.hasText(acceptFiles)) {
				buffer.append(" acceptFiles:'").append(acceptFiles).append("',");
			}
			
			if (StringUtils.hasText(this.allowedExtensions)) {
				if (!this.allowedExtensions.equals("all")) {
					buffer.append("allowedExtensions: [").append(this.allowedExtensions).append("],");
				}
			} else {
				buffer.append("allowedExtensions:['xlsx'],");
			}
			buffer.append(" minSizeLimit:").append(this.minSizeLimit).append(",");
			buffer.append(" sizeLimit:").append(this.sizeLimit).append(" },");
			if (autoUpload) { 
				buffer.append("  autoUpload:true,");
			} else {
				buffer.append("  autoUpload:false,");
			}
			buffer.append(" multiple:").append(this.multiple).append(",");
			buffer.append("  disableCancelForFormUploads:false,");
			if (this.multiple){
				buffer.append(" maxConnections:").append(this.maxConnections).append(",");
			}
			buffer.append("  messages:{");
			buffer.append(" emptyError:'{file}為空文檔，請重新上傳',");
			buffer.append(" maxHeightImageError:'圖像太高了',");
			buffer.append(" maxWidthImageError:'圖像太寬了',");
			buffer.append("  minHeightImageError:'',");
			buffer.append("  minWidthImageError:'',");
			buffer.append(" minSizeError:'{file} 太小了，最小文件大小為 {minSizeLimit}.',");
			buffer.append(" noFilesError:'沒有上傳的文件.',");
			buffer.append(" onLeave:'文件正在上傳, 如果你取消,上传将被取消.',");
			buffer.append("  sizeError:'{file}超過系統限制上限{sizeLimit}，請重新上傳',");
			
			buffer.append(" tooManyItemsError:'太多文件({netItems})將被上傳. 最多上傳數量為 {itemLimit}.',");
			if(StringUtils.hasText(this.errorMsg)) {
				buffer.append(" typeError:'格式錯誤,支援上傳副檔名為: "+this.errorMsg+".',");
			} else {
				buffer.append(" typeError:'{file}有一個無效的副檔名. 有效的格式為: {extensions}.',");
			}
			buffer.append(" uuidName:'newUuid'");
			buffer.append(" },");
			buffer.append(" text:{ ");
			buffer.append(" defaultResponseError:'Upload failure reason unknown'");
			buffer.append(" },");
			
			//delete
			if (this.whetherDelete && !this.whetherImport) {
				buffer.append(" deleteFile: { ");
				buffer.append("  enabled: true,");
				buffer.append("  endpoint:\"").append(deleteUrl).append("\",");
				buffer.append(" deletingFailedText:'刪除失敗',");
				buffer.append(" deletingStatusText:'刪除中...',");
				buffer.append(" forceConfirm:false,");
				buffer.append("  method:'POST',");
				if (StringUtils.hasText(this.deleletParams)) {
					buffer.append(" params:").append(this.deleletParams);
				}
				buffer.append("},");
			}
			buffer.append("classes:{");
			buffer.append(" success:\"qq-upload-success\",");
			buffer.append(" cancle:\"qq-upload-cancel\"");
			buffer.append(" }, ");
			
			//返回時
			buffer.append(" callbacks:{ ");
			buffer.append("  onComplete: function(id, fileName, response){");
			if (this.whetherDownLoad && !this.whetherImport) {
				buffer.append("  var index = id; ");
				buffer.append(" setTimeout(function(){");
				buffer.append("  if(response.success == true){");
				buffer.append("  if(index.replace){");
				buffer.append(" index = index.replace('qq-upload-handler-iframe', '');}");
				buffer.append("var link = $(\"#").append(this.id).append("\").find('.qq-upload-file:eq('+ index +')');");
			
				buffer.append(" link.attr('href','#');");
				buffer.append(" link.attr('onclick','downloadFile(\\\"'+response.qquuid+'\\\")');");
				
				buffer.append("  }}, 1000);");
			}
				buffer.append(" }");
			if (this.whetherImport && this.autoUpload){
				buffer.append(",");
				buffer.append(" onStatusChange:function(id,oldStatus,newStatus) {");
				buffer.append("if (newStatus == qq.status.SUBMITTED) { ");
				
				buffer.append(" var blockStyle = {message:'loading...',css:{width:'150px',top:'40%',left:'40%',height:'50px',border:'3px solid #95B8E7',lineHeight:'50px'},overlayCSS:{backgroundColor:'#aaa',cursor:'default'}};");
				buffer.append(" $.blockUI(blockStyle);");
				buffer.append(" } else if (newStatus == qq.status.UPLOAD_SUCCESSFUL) {");
				buffer.append(" $.unblockUI();");
				buffer.append(" } else if (newStatus == qq.status.UPLOAD_FAILED ||");
				buffer.append(" newStatus == qq.status.CANCELED || newStatus == qq.status.REJECTED) {");
				buffer.append("$.unblockUI();}}");
			}
			if (isCustomError) {
				buffer.append(",").append("onError:function(id, name, reason, maybeXhrOrXdr) {");
				buffer.append("if (maybeXhrOrXdr) {");
				buffer.append("var sessionStatus = maybeXhrOrXdr.getResponseHeader('sessionstatus');");
				buffer.append("if(sessionStatus != 'timeout') {");
				buffer.append("$('#" + messageId + "').text(reason);}");
				buffer.append("} else {");
				buffer.append("$('#" + messageId + "').text(reason);}");
				buffer.append("},").append("onSubmit:function(id,name){");
				buffer.append("$('#" + messageId + "').text('');}");
			}
			if (StringUtils.hasText(javaScript)){
				buffer.append(",");
				buffer.append(javaScript);
			}
			buffer.append("}");
			buffer.append(" });");
			buffer.append("});");
			buffer.append("</script> ");
			buffer.append("</div>");
			out.println(buffer.toString());
			return 0;
		} catch (Exception e) {
			throw new JspTagException(e.getMessage());
		}
	}

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the showName
	 */
	public String getShowName() {
		return showName;
	}

	/**
	 * @param showName the showName to set
	 */
	public void setShowName(String showName) {
		this.showName = showName;
	}

	/**
	 * @return the uploadUrl
	 */
	public String getUploadUrl() {
		return uploadUrl;
	}

	/**
	 * @param uploadUrl the uploadUrl to set
	 */
	public void setUploadUrl(String uploadUrl) {
		this.uploadUrl = uploadUrl;
	}


	/**
	 * @return the uploadParams
	 */
	public String getUploadParams() {
		return uploadParams;
	}

	/**
	 * @param uploadParams the uploadParams to set
	 */
	public void setUploadParams(String uploadParams) {
		this.uploadParams = uploadParams;
	}

	/**
	 * @return the deleletParams
	 */
	public String getDeleletParams() {
		return deleletParams;
	}

	/**
	 * @param deleletParams the deleletParams to set
	 */
	public void setDeleletParams(String deleletParams) {
		this.deleletParams = deleletParams;
	}

	/**
	 * @return the allowedExtensions
	 */
	public String getAllowedExtensions() {
		return allowedExtensions;
	}

	/**
	 * @param allowedExtensions the allowedExtensions to set
	 */
	public void setAllowedExtensions(String allowedExtensions) {
		this.allowedExtensions = allowedExtensions;
	}

	/**
	 * @return the sizeLimit
	 */
	public Integer getSizeLimit() {
		return sizeLimit;
	}

	/**
	 * @param sizeLimit the sizeLimit to set
	 */
	public void setSizeLimit(Integer sizeLimit) {
		this.sizeLimit = sizeLimit;
	}

	/**
	 * @return the multiple
	 */
	public boolean isMultiple() {
		return multiple;
	}

	/**
	 * @param multiple the multiple to set
	 */
	public void setMultiple(boolean multiple) {
		this.multiple = multiple;
	}

	/**
	 * @return the maxConnections
	 */
	public Integer getMaxConnections() {
		return maxConnections;
	}

	/**
	 * @param maxConnections the maxConnections to set
	 */
	public void setMaxConnections(Integer maxConnections) {
		this.maxConnections = maxConnections;
	}
	/**
	 * @return the showFileList
	 */
	public boolean isShowFileList() {
		return showFileList;
	}

	/**
	 * @param showFileList the showFileList to set
	 */
	public void setShowFileList(boolean showFileList) {
		this.showFileList = showFileList;
	}

	/**
	 * @return the deleteUrl
	 */
	public String getDeleteUrl() {
		return deleteUrl;
	}

	/**
	 * @param deleteUrl the deleteUrl to set
	 */
	public void setDeleteUrl(String deleteUrl) {
		this.deleteUrl = deleteUrl;
	}

	/**
	 * @return the javaScript
	 */
	public String getJavaScript() {
		return javaScript;
	}

	/**
	 * @param javaScript the javaScript to set
	 */
	public void setJavaScript(String javaScript) {
		this.javaScript = javaScript;
	}

	/**
	 * @return the whetherImport
	 */
	public boolean isWhetherImport() {
		return whetherImport;
	}

	/**
	 * @param whetherImport the whetherImport to set
	 */
	public void setWhetherImport(boolean whetherImport) {
		this.whetherImport = whetherImport;
	}

	/**
	 * @return the whetherDelete
	 */
	public boolean isWhetherDelete() {
		return whetherDelete;
	}

	/**
	 * @param whetherDelete the whetherDelete to set
	 */
	public void setWhetherDelete(boolean whetherDelete) {
		this.whetherDelete = whetherDelete;
	}

	/**
	 * @return the whetherDownLoad
	 */
	public boolean isWhetherDownLoad() {
		return whetherDownLoad;
	}

	/**
	 * @param whetherDownLoad the whetherDownLoad to set
	 */
	public void setWhetherDownLoad(boolean whetherDownLoad) {
		this.whetherDownLoad = whetherDownLoad;
	}

	/**
	 * @return the uploadedFilelist
	 */
	public List<FileUpLoad> getUploadedFilelist() {
		return uploadedFilelist;
	}

	/**
	 * @param uploadedFilelist the uploadedFilelist to set
	 */
	public void setUploadedFilelist(List<FileUpLoad> uploadedFilelist) {
		this.uploadedFilelist = uploadedFilelist;
	}

	/**
	 * @return the width
	 */
	public String getWidth() {
		return width;
	}

	/**
	 * @param width the width to set
	 */
	public void setWidth(String width) {
		this.width = width;
	}

	/**
	 * @return the autoUpload
	 */
	public boolean isAutoUpload() {
		return autoUpload;
	}

	/**
	 * @param autoUpload the autoUpload to set
	 */
	public void setAutoUpload(boolean autoUpload) {
		this.autoUpload = autoUpload;
	}

	/**
	 * @return the sizeErrorMessage
	 */
	public String getSizeErrorMessage() {
		return sizeErrorMessage;
	}

	/**
	 * @param sizeErrorMessage the sizeErrorMessage to set
	 */
	public void setSizeErrorMessage(String sizeErrorMessage) {
		this.sizeErrorMessage = sizeErrorMessage;
	}

	/**
	 * @return the showUnderline
	 */
	public boolean isShowUnderline() {
		return showUnderline;
	}

	/**
	 * @param showUnderline the showUnderline to set
	 */
	public void setShowUnderline(boolean showUnderline) {
		this.showUnderline = showUnderline;
	}

	/**
	 * @return the messageAlert
	 */
	public boolean isMessageAlert() {
		return messageAlert;
	}

	/**
	 * @param messageAlert the messageAlert to set
	 */
	public void setMessageAlert(boolean messageAlert) {
		this.messageAlert = messageAlert;
	}

	/**
	 * @return the minSizeLimit
	 */
	public Integer getMinSizeLimit() {
		return minSizeLimit;
	}

	/**
	 * @param minSizeLimit the minSizeLimit to set
	 */
	public void setMinSizeLimit(Integer minSizeLimit) {
		this.minSizeLimit = minSizeLimit;
	}

	/**
	 * @return the acceptFiles
	 */
	public String getAcceptFiles() {
		return acceptFiles;
	}

	/**
	 * @param acceptFiles the acceptFiles to set
	 */
	public void setAcceptFiles(String acceptFiles) {
		this.acceptFiles = acceptFiles;
	}

	/**
	 * @return the messageId
	 */
	public String getMessageId() {
		return messageId;
	}

	/**
	 * @param messageId the messageId to set
	 */
	public void setMessageId(String messageId) {
		this.messageId = messageId;
	}

	/**
	 * @return the isCustomError
	 */
	public boolean isCustomError() {
		return isCustomError;
	}

	/**
	 * @param isCustomError the isCustomError to set
	 */
	public void setIsCustomError(boolean isCustomError) {
		this.isCustomError = isCustomError;
	}

	/**
	 * @return the errorMsg
	 */
	public String getErrorMsg() {
		return errorMsg;
	}

	/**
	 * @param errorMsg the errorMsg to set
	 */
	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

	
	
}
