import {createElem, groupElemBy} from "./util.js";

export function SignInForm(init) {
    const form = createElem('form', {class: 'border border-secondary p-md-4'})
    form.innerHTML = `
        <h2>회원가입</h2>
        <div class="mb-3">
            <label class="form-label">계정</label>
            <input type="email" name="username" class="form-control" required>
        </div>
        <div class="mb-3">
            <label class="form-label">비밀번호</label>
            <input type="password" class="form-control" name="password" required>
        </div>
        <div class="mb-3">
            <label class="form-label">비밀번호 재확인</label>
            <input type="password" class="form-control" name="password" required>
        </div>
        <button name="btn-sign-in" type="submit" class="btn btn-outline-primary">가입</button>
    `;
    const ret = groupElemBy('name')(form)
    if (init) init(ret);
    return ret;
}

export function LoginForm(init) {
    const form = createElem('form', {class: 'border border-secondary p-md-4'})
    form.innerHTML = `
        <h2>로그인</h2>
        <div class="mb-3">
            <label class="form-label">계정</label>
            <input type="email" name="username" class="form-control" required>
        </div>
        <div class="mb-3">
            <label class="form-label">비밀번호</label>
            <input type="password" class="form-control" name="password" required>
        </div>
        <div class="mb-3 form-check">
            <input id="check-remember" type="checkbox" class="form-check-input" name="check-remember">
            <label class="form-check-label" for="check-remember">자동 로그인</label>
        </div>
        <button type="submit" class="btn btn-outline-primary">로그인</button>
    `;
    const ret = groupElemBy('name')(form);
    if (init) init(ret);
    return ret;
}

export function BoardWriteForm(init) {
    const form = createElem('form', {class: 'border border-secondary p-md-4'})
    form.innerHTML = `
        <h2>게시판 글쓰기</h2>
        <div class="mb-3">
            <label class="form-label"> 제목</label>
            <input type="text" class="form-control" placeholder="제목" name="title" required>
        </div>
        <div class="mb-3">
            <label class="form-label">내용</label>
            <textarea class="form-control" rows="3" name="content" required></textarea>
        </div>
        <button type="submit" class="btn btn-outline-dark">작성</button>
    `;
    const ret = groupElemBy('name')(form);
    if (init) init(ret);
    return ret;
}