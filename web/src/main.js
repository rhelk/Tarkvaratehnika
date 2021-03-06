
import Vue from 'vue'
import App from './App.vue'

import Routes from './routes'
import VueRouter from 'vue-router'
import axios from 'axios'

import { store } from './store/store'

import LoadScript from 'vue-plugin-load-script'
import Firebase from 'firebase/app'
import 'firebase/storage'
import BootstrapVue from 'bootstrap-vue'
import 'bootstrap/dist/css/bootstrap.css'
import 'bootstrap-vue/dist/bootstrap-vue.css'
import VeeValidate from 'vee-validate'



Vue.use(BootstrapVue)
Vue.use(LoadScript);
Vue.use(Firebase);

Vue.use(VueRouter);


const config = {
  aria: true,
  classNames: {},
  classes: false,
  delay: 0,
  dictionary: null,
  events: 'input|blur',
  fieldsBagName: 'veeFields',
  inject: true,
  locale: 'en',
  validity: false
};

Vue.use(VeeValidate, config);
// if (token) {
//   axios.defaults.headers.common['Authorization'] = token;
//   this.$store.dispatch("getUser").catch();
// }


//Vue.use(VueAxios, axios);
//Vue.prototype.$http = axios;

const router = new VueRouter({
  routes: Routes,
  mode: 'history'
});

new Vue({
  store: store,
  el: '#app',
  render: h => h(App),
  router: router

})
