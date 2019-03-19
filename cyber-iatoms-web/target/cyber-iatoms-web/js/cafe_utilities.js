	function trim(value)	{
		var temp = value;
		var obj = /^(\s*)([\W\w]*)(\b\s*$)/;
		if (obj.test(temp)) { temp = temp.replace(obj, '$2'); }
		var obj = / +/g;
		temp = temp.replace(obj, " ");
		if (temp == " ") { temp = ""; }
		return temp;
	}
//check whether a specific value of field is empty or not.
	function isEmpty(s) {
		if (s == null || s == undefined) return true;
		s=trim(s);
		if ((s.length==0) || (s=="")) return true;
		for (var i=0 ; i < s.length ; i++) {
			if (s[i]!=" ") return false;
		}
		return true;
	}
	/**
	 * 提交表單的公用方法。
	 * @param objForm
	 * @param useCaseNo
	 * @param serviceId
	 * @param actionId
	 */
	function actionClicked(objForm, useCaseNo, serviceId, actionId) {
		disableAllButtons(objForm); 
		objForm.useCaseNo.value = useCaseNo;
		objForm.serviceId.value = serviceId;
		objForm.actionId.value = actionId;
		objForm.submit();
	}

	function actionClickedByFormAction(objForm, useCaseNo, serviceId, actionId, formAction) {
		disableAllButtons(objForm); 
		objForm.useCaseNo.value = useCaseNo;
		objForm.serviceId.value = serviceId;
		objForm.actionId.value = actionId;
		objForm.action = formAction;
		objForm.submit();
	}
	/**
	 * disable all buttons.
	 **/			
	function disableAllButtons(objForm){
		fobj = objForm.elements;
		for (i=0;i<fobj.length;i++) {
			if (fobj[i].type=="button") {
				if (objForm.elements[i].disabled ==false)
					objForm.elements[i].disabled=true;
			}
			//input標籤，type="image"
			if (fobj[i].type=="image") {
				if (objForm.elements[i].disabled ==false)
					objForm.elements[i].disabled=true;
			}
		}
		//img標籤
		var imgs = document.getElementsByTagName("img"); 
		for(j=0;j<imgs.length;j++){
			if (imgs[j].disabled ==false)
				imgs[j].disabled=true;
		}
		//image標籤
		var images = document.getElementsByTagName("image"); 
		for(k=0;j<images.length;j++){
			if (images[k].disabled ==false)
				images[k].disabled=true;
			}
	}