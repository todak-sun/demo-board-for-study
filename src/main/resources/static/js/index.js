import Ajax from './Ajax.js'
import {getEl, groupElemBy, appendElem, createElem} from './util.js'
import {LoginForm, BoardWriteForm, SignInForm} from './template.js'

const log = console.log;

document.addEventListener('DOMContentLoaded', (e) => {
    const rootDom = getEl('#root');
    const navBtnGroup = getEl('#nav-btn-group')

    const CONST = {
        TOKEN_KEY: 'my-auth-token'
    }
    const ajax = new Ajax('', {'Content-Type': 'application/json'});

    const state = {
        token: null,
        boards: [],
    }


    function createBtn(attrs, init) {
        return createElem('button', attrs, init);
    }

    initialize();

    function initialize() {
        if (localStorage.getItem(CONST.TOKEN_KEY) && localStorage.getItem(CONST.TOKEN_KEY) !== 'undefined') {
            state.token = localStorage.getItem(CONST.TOKEN_KEY);
            ajax.setHeader('Authorization', `Bearer ${state.token}`);
            // changeLoginState();

            ajax.get('/api/v1/boards')({size: 10, page: 0})
                .then(res => res.json())
                .then(json => {
                    const {data} = json;
                    appendElem(
                        getEl('#board-table > tbody'),
                        createBoardList(data))
                });
            appendElem(rootDom, BoardWriteForm(boardWriteFormInit)._this)
            appendElem(rootDom,
                createBtn({text: '로그아웃', class: 'btn btn-outline-danger'}, (btn) => {
                    btn.addEventListener('click', (e) => {
                        log('로그아웃')
                    })
                }))
        } else {
            appendElem(navBtnGroup,
                createBtn({text: '회원가입', class: 'btn btn-outline-success'}, (btn) => {
                    btn.addEventListener('click', (e) => {
                        log('회원가입');
                    })
                })
            )
            appendElem(rootDom, LoginForm(loginFormInit)._this)
        }
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
                    const {token} = json;
                    username.value = '';
                    password.value = '';

                    state.token = token;
                    localStorage.setItem(CONST.TOKEN_KEY, token);
                    ajax.setHeader('Authorization', `Bearer ${state.token}`);

                })
                .catch(err => console.error(err));
        })
    }

    function signInFormInit(signInForm) {
        signInForm._this.addEventListener('submit', (e) => {
            const {username, password, passwordRe} = signInForm;
            e.preventDefault();

            const userRequestBody = {
                username: username.value,
                password: password.value,
                passwordRe: passwordRe.value
            }
            ajax.post('/sign-in')(userRequestBody)
                .then(res => res.json())
                .then(json => {
                    username.value = '';
                    password.value = '';
                    passwordRe.value = '';
                })
                .catch(err => console.error(err));
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
                    log(json);
                }))
                .catch(err => console.error(err));
        })
    }


    function createBoardList(boards) {
        return boards.reduce((frag, board) => {
            const {id, title, content, writtenAt, updatedAt, writtenBy} = board;
            const tr = appendElem(createElem('tr'), [
                createElem('td', {text: id}),
                createElem('td', {text: title}),
                createElem('td', {text: writtenBy}),
                createElem('td', {text: writtenAt}),
                createElem('td', {text: updatedAt})
            ]);
            return appendElem(frag, tr);
        }, document.createDocumentFragment());
    }

    function changeLoginState() {
        // navBar.welcome.textContent = state.token ? '로그인 중' : '로그인 해야됨';
    }
});


