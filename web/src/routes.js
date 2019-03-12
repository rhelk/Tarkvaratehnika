import allRooms from './views/allRooms.vue'
import room from './views/room.vue'
import addProperty from './views/addRoom.vue'
import login from './views/login.vue'
import register from "./views/register";

export default [
  {path: "/", component: allRooms },
  {path: '/room/:id', component: room},
  {path: '/add', component: addProperty},
  {path: '/login', component: login},
  {path: '/register', component: register},
]
