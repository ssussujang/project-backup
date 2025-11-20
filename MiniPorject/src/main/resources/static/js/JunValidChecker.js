// 잘못되었으면 true값 (뭔가 문제가있으면 true, 정상적이면 false)

// <input>을 넣었을 때
// 아무것도 안썼으면 true, 뭐라도 썼으면 false
function isEmpty(input) {
	return (!input.value);
}

// <input>을 넣었을 때
// 한글 / 특수문자가 적혀있다면 true, 없다면 false
function containsAnother(input) {
	let pass = "1234567890abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ@_.";
	let iv = input.value;
	for (let i = 0; i < iv.length; i++) {
		if (pass.indexOf(iv[i] == -1)) { // pass에 있는 내용 중에서 input의 내용이 없으면 true
			return true;
		}
	}
	return false;
}

// <input>을 넣었을 때 그 글자수보다 적으면 true, 그 글자수 이상이면 false
function atLeastLetter(input, len) {
	return (input.value.length < len);
}

// <input>을 2개 넣었을때 내용이 다르면 true, 같으면 false
function notEqualPw(input1, input2) {
	return (input1.value != input2.value);
}

// <input>과 문자열세트를 넣었을 때 문자열이 input에 없으면 true, 있으면 false
function notContain(input, passSet) {
	let iv = input.value;
	for (let i = 0; i < passSet.length; i++) {
		if (iv.indexOf(passSet[i]) != -1) {
			return false;
		}
	}
	return true;
}

// <input>을 넣었을 때 숫자가 아닌 값이 있으면, true 숫자만 있으면 false
function isNotNumber(input) {
	return isNaN(input.value);
}

// <input>에 확장자를 넣었을 때 확장자명이 아니면 true, 맞으면 false
// 확장자는 파일명.확장자 로 들어온다.
function isNotType(input, type) {
	type = "." + type;
	return (input.value.indexOf(type) == -1);
}

// <input>에 이름이 들어갈때 한글이 아니면 true 맞으면 false 리턴
function isKor(input) {
	let kor = /[ㄱ-ㅎ|ㅏ-ㅣ|가-힣]/;
	return (!kor.test(input.value));
}