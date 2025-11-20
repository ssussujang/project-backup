document.addEventListener("DOMContentLoaded",function(){
	const registerBtn = document.getElementById("registerBtn");
	if(registerBtn){
		registerBtn.addEventListener("click", function(){
			window.location.href = "memberRegister.html";
		});
	}
});