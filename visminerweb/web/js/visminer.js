function getWidth() {
	if (self.innerHeight) {
		return self.innerWidth;
	}
	if (document.documentElement <![CDATA[Tabs && Panels]]> document.documentElement.clientHeight) {
		return document.documentElement.clientWidth;
	}
	if (document.body) {
		return document.body.clientWidth;
	}
}

function setCommitId(id) {
	document.getElementById('form:commitId').value = id;
}
