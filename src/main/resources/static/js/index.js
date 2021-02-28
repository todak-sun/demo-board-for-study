import {appendElem, createElem, getEl} from './util.js'
import {BoardViewForm, BoardWriteForm, LoginModalForm, ModalFormLayer, SignUpForm} from './template.js'
import Ajax from './Ajax.js'

const log = console.log;

document.addEventListener('DOMContentLoaded', (e) => {
    const elem = {
        navBtnGroup: getEl('#nav-btn-group'),
        modal: null,
        boardTable: getEl('#board-table')
    }

    const CONST = {
        TOKEN_KEY: 'my-auth-token'
    }
    const ajax = new Ajax('', {'Content-Type': 'application/json'});

    const state = {
        token: localStorage.getItem(CONST.TOKEN_KEY),
        boards: [],
    }

    changeLoginState();

    function changeLoginState() {
        Array.from(elem.navBtnGroup.children).forEach(child => {
            elem.navBtnGroup.removeChild(child);
        })
        if (state.token && state.token !== 'undefined') {
            appendElem(elem.navBtnGroup, [btnLogout()._this, btnWrite()._this])
            setToken(state.token);
            printBoards();
        } else {
            appendElem(elem.navBtnGroup, [btnLogin()._this, btnSignup()._this])
        }
    }

    function printBoards() {
        ajax.get('/api/v1/boards')().then(res => res.json()).then(json => {
            Array.from(elem.boardTable.children).forEach(child => {
                elem.boardTable.removeChild(child);
            });
            const {data: boards} = json;
            appendElem(elem.boardTable, createBoardList(boards));
        }).catch(err => {
            console.log(err);
        })
    }

    function btnWrite() {
        const btn = createBtn({class: 'btn btn-outline-success', text: '글쓰기'});
        btn.addEventListener('click', (e) => {
            const layer = ModalFormLayer({title: '글쓰기'})
            elem.modal = new bootstrap.Modal(layer._this);
            layer._this.addEventListener('hidden.bs.modal', (e) => {
                elem.modal._element.parentElement.removeChild(elem.modal._element);
            })
            layer.addContent(BoardWriteForm(boardWriteFormInit)._this);
            elem.modal.show();
        })
        return {_this: btn, type: 'write'}
    }

    function btnLogin() {
        const btn = createBtn({class: 'btn btn-outline-primary', text: '로그인'})
        btn.addEventListener('click', (e) => {
            const layer = ModalFormLayer({title: '로그인'})
            elem.modal = new bootstrap.Modal(layer._this);
            layer._this.addEventListener('hidden.bs.modal', (e) => {
                elem.modal._element.parentElement.removeChild(elem.modal._element);
            })
            layer.addContent(LoginModalForm(loginFormInit)._this);
            elem.modal.show();
        })
        return {_this: btn, type: 'login'};
    }

    function btnLogout() {
        const btn = createBtn({class: 'btn btn-outline-danger', text: '로그아웃'})
        btn.addEventListener('click', (e) => {
            setToken(null);
            changeLoginState();
        })
        return {_this: btn, type: 'logout'};
    }

    function btnSignup() {
        const btn = createBtn({class: 'btn btn-outline-primary', text: '회원가입'})
        btn.addEventListener('click', (e) => {
            const layer = ModalFormLayer({title: '회원가입'})
            layer.addContent(SignUpForm(signUpFormInit)._this);
            layer._this.addEventListener('hidden.bs.modal', (e) => {
                elem.modal._element.parentElement.removeChild(elem.modal._element);
            })
            elem.modal = new bootstrap.Modal(layer._this);
            elem.modal.show();
        })
        return {_this: btn, type: 'signup'};
    }

    function createBtn(attrs, init) {
        return createElem('button', attrs, init);
    }

    function loginFormInit(loginForm) {
        loginForm._this.addEventListener('submit', (e) => {
            e.preventDefault();
            const {username, password} = loginForm;

            const loginRequestBody = {
                username: username.value,
                password: password.value
            }
            ajax.post('/auth')(loginRequestBody)
                .then(res => res.json())
                .then(json => {
                    const {data: token} = json;
                    username.value = '';
                    password.value = '';

                    setToken(token);
                    alert('로그인 성공')

                    elem.modal.hide();
                    changeLoginState();
                })
                .catch(err => {
                    console.error(err);
                    alert(err.message);
                });
        })
    }

    function setToken(token) {
        if (token) {
            localStorage.setItem(CONST.TOKEN_KEY, token);
            ajax.setHeader('Authorization', `Bearer ${state.token}`);
        } else {
            localStorage.removeItem(CONST.TOKEN_KEY);
            ajax.removeHeader('Authorization');
        }
        state.token = token;
    }

    function signUpFormInit(signUpForm) {
        signUpForm._this.addEventListener('submit', (e) => {
            const {username, password, passwordRe} = signUpForm;
            e.preventDefault();

            const userRequestBody = {
                username: username.value,
                password: password.value,
                passwordRe: passwordRe.value
            }
            ajax.post('/sign-in')(userRequestBody)
                .then(res => res.json())
                .then(json => {
                    log(json);
                    username.value = '';
                    password.value = '';
                    passwordRe.value = '';
                    alert('회원가입에 성공했습니다.')
                    elem.modal.hide();
                })
                .catch(err => {
                    console.error(err);
                    alert(err.message);
                });
        })
    }

    function boardWriteFormInit(boardWriteForm) {
        boardWriteForm._this.addEventListener('submit', (e) => {
            e.preventDefault();
            const {title, content} = boardWriteForm;
            const boardWriteRequest = {
                title: title.value,
                content: content.value
            }

            ajax.post('/api/v1/boards')(boardWriteRequest)
                .then((res => res.json()))
                .then((json => {
                    alert('글쓰기 성공');
                    const {data: board} = json;
                    elem.boardTable.prepend(createBoardList(board));
                    elem.modal.hide();
                }))
                .catch(err => {
                    console.error(err)
                    alert(err.message);
                });
        })
    }

    function boardViewFormInit(boardViewForm) {
        const {btnUpdate, btnDelete, title, content, id} = boardViewForm;
        if (btnUpdate && btnDelete) {
            btnUpdate.addEventListener('click', (e) => {
                const boardUpdateRequest = {
                    title: title.value,
                    content: content.value
                }

                ajax.put(`/api/v1/boards/${id}`)(boardUpdateRequest)
                    .then(res => res.json())
                    .then(json => {
                        const {title, content} = json;
                        boardViewForm.title.value = title;
                        boardViewForm.content.value = content;
                        alert('수정 완료');
                        elem.modal.hide();
                        printBoards();
                    })
                    .catch(err => {
                        console.error(err);
                        alert(err.message);
                    })
            })

            btnDelete.addEventListener('click', (e) => {
                ajax.delete(`/api/v1/boards/${id}`)()
                    .then(res => res.json())
                    .then(json => {
                        alert('삭제 완료');
                        elem.modal.hide();
                        printBoards();
                    })
                    .catch(err => {
                        console.error(err);
                        alert(err.message);
                    });
            })
        }
    }

    function createBoardList(boards) {
        if (!Array.isArray(boards)) boards = [boards];
        return boards.reduce((frag, board) => {
            const {id, title, updatedAt, writtenBy} = board;
            const tr = appendElem(createElem('tr'), [
                createElem('td', {text: id}),
                createElem('td', {text: title}),
                createElem('td', {text: writtenBy}),
                createElem('td', {text: updatedAt})
            ]);

            tr.addEventListener('click', function (e) {
                ajax.get(`/api/v1/boards/${id}`)().then(res => res.json())
                    .then(json => {
                        const {data} = json;
                        const layer = ModalFormLayer({title: data.title})
                        elem.modal = new bootstrap.Modal(layer._this);
                        layer._this.addEventListener('hidden.bs.modal', (e) => {
                            elem.modal._element.parentElement.removeChild(elem.modal._element);
                        })
                        layer.addContent(BoardViewForm(data, boardViewFormInit)._this,);
                        elem.modal.show();
                    })
            })

            return appendElem(frag, tr);
        }, document.createDocumentFragment());
    }
});