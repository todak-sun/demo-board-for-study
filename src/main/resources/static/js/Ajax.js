export default class Ajax {
    #baseUrl = '';
    #baseHeader = {};
    constructor(baseUrl, baseHeader) {
        this.#baseUrl = baseUrl;
        this.#baseHeader = baseHeader;
    }

    getHeader(key) {
        return this.#baseHeader[key];
    }

    setHeader(key, value) {
        this.#baseHeader[key] = value;
    }

    get(url) {
        return (data = {}, headers = {}) => {
            let requestUrl = `${this.#baseUrl}${url}`;
            requestUrl += `?${Object.entries(data).map(([key, value]) => `${key}=${value}`).join('&')}`;
            return fetch(requestUrl, {method: 'GET', headers: {...this.#baseHeader, ...headers}})
        }
    }

    post(url) {
        return (data = {}, headers = {}) => {
            let requestUrl = `${this.#baseUrl}${url}`;
            return fetch(requestUrl, {
                method: 'POST',
                headers: {...this.#baseHeader, ...headers},
                body: JSON.stringify(data)
            })
        }
    }

    put(url) {
        return (data = {}, headers = {}) => {
            let requestUrl = `${this.#baseUrl}${url}`;
            return fetch(requestUrl, {
                method: 'PUT',
                headers: {...this.#baseHeader, ...headers},
                body: JSON.stringify(data)
            })
        }
    }

    delete(url) {
        return (headers = {}) => {
            let requestUrl = `${this.#baseUrl}${url}`;
            return fetch(requestUrl, {method: 'DELETE', headers: {...this.#baseHeader, ...headers}})
        }
    }
}