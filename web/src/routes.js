import allRooms from './views/allRooms.vue'
import room from './views/room.vue'
import addProperty from './views/addRoom.vue'
import login from './views/login.vue'
import register from "./views/register"
import home from "./views/home.vue"
import allRoomsSearch from './views/allRoomsSearch.vue'


export default [
  {path: "/:id", component: allRooms },
  {path: "/", component: allRooms },
  {path: '/room/:id', component: room},
  {path: '/property/add', component: addProperty},
  {path: '/login', component: login},
  {path: '/register', component: register},
  {path: '/home', component: home},

]
