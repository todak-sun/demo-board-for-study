import {createElem, getEl, groupElemBy, appendElem} from "./util.js";

export function SignUpForm(init) {
    const form = createElem('form', {class: 'border border-secondary p-md-4'})
    form.innerHTML = `
        <div class="modal-body">
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
                <input type="password" class="form-control" name="password-re" required>
            </div>
        </div>
        <div class="modal-footer">
            <button name="btn-sign-in" type="submit" class="btn btn-outline-primary">가입</button>
        </div>
    `;
    const ret = groupElemBy('name')(form)
    if (init) init(ret);
    return ret;
}

export function LoginModalForm(init) {
    const form = createElem('form');
    form.innerHTML = `
    <div class="modal-body">                    
        <div class="mb-3">
            <label class="form-label">계정</label>
            <input type="email" name="username" class="form-control" required>
        </div>
        <div class="mb-3">
            <label class="form-label">비밀번호</label>
            <input type="password" class="form-control" name="password" required>
        </div>        
    </div>
    <div class="modal-footer">
        <button type="submit" class="btn btn-outline-primary">로그인</button>
    </div>
    `;
    const ret = groupElemBy('name')(form)
    if (init) init(ret);
    return ret;
}

export function BoardViewForm({title, content, mine, id}, init) {
    const form = createElem('form', {class: 'border border-secondary p-md-4'})
    form.innerHTML = `
        <div class="modal-body">
            <div class="mb-3">
                <label class="form-label"> 제목</label>
                <input type="text" class="form-control" placeholder="제목" name="title" required value="${title}" ${mine ? '' : 'readonly'}>
            </div>
            <div class="mb-3">
                <label class="form-label">내용</label>
                <textarea class="form-control" rows="3" name="content" required ${mine ? '' : 'readonly'}>${content}</textarea>
            </div>
        </div>
        ${mine ? `
            <div class="modal-footer">
                <button type="button" name="btn-update" class="btn btn-outline-success">수정</button>
                <button type="button" name="btn-delete" class="btn btn-outline-danger">삭제</button>
            </div>
        ` : ''}
    `;
    const ret = groupElemBy('name')(form);
    ret.id = id;
    if(init) init(ret);
    return ret;
}

export function BoardWriteForm(init) {
    const form = createElem('form', {class: 'border border-secondary p-md-4'})
    form.innerHTML = `
        <div class="modal-body">
            <div class="mb-3">
                <label class="form-label"> 제목</label>
                <input type="text" class="form-control" placeholder="제목" name="title" required>
            </div>
            <div class="mb-3">
                <label class="form-label">내용</label>
                <textarea class="form-control" rows="3" name="content" required></textarea>
            </div>
        </div>
        <div class="modal-footer">
            <button type="submit" class="btn btn-outline-dark">작성</button>
        </div>
    `;
    const ret = groupElemBy('name')(form);
    if (init) init(ret);
    return ret;
}

export function ModalFormLayer({title = '제목'}) {
    const modal = createElem('div', {class: 'modal', tabindex: '-1'});
    modal.innerHTML = `
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title" name="title">${title}</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                </div>
            </div>
        </div>
    `;
    const area = getEl('.modal-content', modal);
    return {
        _this: modal,
        addContent: content => appendElem(area, content)
    }
}