import Vue from 'vue'
import Vuex from 'vuex'
import axios from 'axios'
import {consoleError} from "vuetify/lib/util/console";

Vue.use(Vuex);

export const store = new Vuex.Store({
  state: {
    status: '',
    user_id: '',
    token: '',
    loginAddress: 'http://localhost:8080/api/login',
    apiAddress: 'http://localhost:8080/api/',
    apiAddress2: 'http://localhost:8080/api/user/authCheck'
  },
  getters : {
    isLoggedIn: state => !!state.token,
    getToken: state => {return state.token},
    authStatus: state => state.status,
    getApiAddress: state => {return state.apiAddress},
    getApiAddress2: state => {return state.apiAddress2},
    getUser_id: state => {return state.user_id}
  },
  mutations: {
    auth_request(state){
      state.status = 'loading'
    },
    auth_success(state, token){
      state.status = 'success';
      if (token) {
        state.token = token;
      }
    },
    auth_error(state){
      state.status = 'error'
    },
    logout(state){
      state.status = '';
      state.token = '';
      state.user_id = '';
    },
    setUser(state, user_id){
      state.user_id = user_id;
    }
  },
  actions: {
    login: (context, payload) => {
      console.log("logging in..., ");
      context.commit('auth_request');
      return new Promise((resolve, reject) => {
        axios.post(context.state.apiAddress + "login", payload
        )
          .then(res => {
            const token = res.headers.authorization;
            localStorage.setItem('token', token)
            context.commit('auth_success', token);
            axios.defaults.headers.common['Authorization'] = token;
            resolve(res);
          })
          .catch(err => {
            context.commit('auth_error');
            localStorage.removeItem('token');
            reject(err);
          })
      })
    },
    logout: (context) => {
      context.commit('logout');
      console.log("you are logged out");
      localStorage.removeItem('token');
    },
    register: (context, payload) => {
      return new Promise((resolve, reject) => {
        context.commit('auth_request');
        axios.post(context.state.apiAddress + "register", payload)
          .then(resp => {
            const token = resp.headers.authorization;
            // const user = resp.data.user
            localStorage.setItem('token', token)
            axios.defaults.headers.common['Authorization'] = token
            context.commit('auth_success', token)
            resolve(resp)
          })
          .catch(err => {
            context.commit('auth_error', err)
            localStorage.removeItem('token')
            reject(err)
          })
      })
    },
    doPost: (context, payload) => {
      return new Promise(((resolve, reject) => {
        axios.post(context.getters.getApiAddress + payload.url, payload.body)
          .then(resp => resolve(resp))
          .catch(reason => reject(reason))
      }))
    },
    doGet: (context, payload) => {
      return new Promise(((resolve, reject) => {
        axios.get(context.getters.getApiAddress + payload.url)
          .then(resp => {
            // console.log(resp);
            resolve(resp)
          })
          .catch(reason => reject(reason))
      }))
    },
    getUser: (context) => {
      const token = localStorage.getItem('token')
      if (token) {
        axios.defaults.headers.common['Authorization'] = token;
        context.dispatch("doGet", {
          url: "user/authCheck"
        })
          .then(resp => {
            context.commit("setUser", resp.data.user_id);
            context.commit("auth_success");
          })
          .catch(reason => {
            console.log(reason)
            context.dispatch("logOut");
            console.log("errororororrrorr authCheckis")
          })
      }
    }
  }
})
