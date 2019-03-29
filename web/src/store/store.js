import Vue from 'vue'
import Vuex from 'vuex'
import axios from 'axios'

Vue.use(Vuex);

export const store = new Vuex.Store({
  state: {
    status: '',
    token: localStorage.getItem('token') || '',
    loginAddress: 'http://localhost:8080/api/login',
    apiAddress: 'http://localhost:8080/api/',
    apiAddress2: 'http://localhost:8080/api/secureTest'
  },
  getters : {
    isLoggedIn: state => !!state.token,
    getToken: state => {return state.token},
    authStatus: state => state.status,
    getApiAddress: state => {return state.apiAddress}
  },
  mutations: {
    auth_request(state){
      state.status = 'loading'
    },
    auth_success(state, token){
      state.status = 'success';
      state.token = token;
    },
    auth_error(state){
      state.status = 'error'
    },
    logout(state){
      state.status = '';
      state.token = ''
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
    }
  },
  created(context){
    if (context.token) {
      Vue.prototype.$http.defaults.headers.common['Authorization'] = context.token
      console.log("token lisatud")
    }
  }
})

// export const store = new Vuex.Store({
//   state: {
//     status: '',
//     token: '',
//     apiAddress: 'http://locallhost:8080'
//   },
//   getters : {
//     isLoggedIn: state => !!state.token,
//     getToken: state => {return state.token},
//     authStatus: state => state.status,
//   },
//   mutations: {
//     auth_request(state){
//       this.$store.state.status = 'loading'
//     },
//     auth_success(state, token){
//       this.$store.state.status = 'success';
//       this.$store.state.token = token;
//     },
//     auth_error(state){
//       this.$store.state.status = 'error'
//     },
//     logout(state){
//       this.$store.state.status = '';
//       this.$store.state.token = ''
//     },
//   },
// //   actions: {
//     login({commit}, user){
//       console.log("logging in...")
//       return new Promise((resolve, reject) => {
//         commit('auth_request');
//         axios({url: this.$store.state.apiAddress + '/login', data: user, method: 'POST' })
//           .then(resp => {
//             const token = resp.headers.Authorization;
//             // localStorage.setItem('token', token);
//             axios.defaults.headers.common['Authorization'] = "Bearer " + token;
//             commit('auth_success', token, user);
//             resolve(resp)
//           })
//           .catch(err => {
//             commit('auth_error');
//             localStorage.removeItem('token');
//             reject(err)
//           })
//       })
//     },
//     register({commit}, user){
//       return new Promise((resolve, reject) => {
//         commit('auth_request');
//         axios({url: 'http://localhost:3000/register', data: user, method: 'POST' })
//           .then(resp => {
//             const token = resp.data.token;
//             const user = resp.data.user;
//             localStorage.setItem('token', token);
//             axios.defaults.headers.common['Authorization'] = token;
//             commit('auth_success', token, user);
//             resolve(resp)
//           })
//           .catch(err => {
//             commit('auth_error', err);
//             localStorage.removeItem('token');
//             reject(err)
//           })
//       })
//     }
//   }
// });
