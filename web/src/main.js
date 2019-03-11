import Vue from 'vue'
import App from './App.vue'
import VueResource from 'vue-resource'
import Routes from './routes'
import VueRouter from 'vue-router'
import axios from 'axios'
import VueAxios from 'vue-axios'
import LoadScript from 'vue-plugin-load-script'
import Vuelidate from 'vuelidate'
import VueGmaps from 'vue-gmaps'

Vue.use(Vuelidate);
Vue.use(LoadScript);
Vue.use(VueResource);
Vue.use(VueRouter);
Vue.use(VueGmaps, {
  key: "AIzaSyA0KwxE33umTFkw6tesrmBAZ6EwqtJGgGU",
  libraries: ['places']
});


//Vue.use(VueAxios, axios);
//Vue.prototype.$http = axios;

const router = new VueRouter({
  routes: Routes,
  mode: 'history'
});

new Vue({
  el: '#app',
  render: h => h(App),
  router: router
})
