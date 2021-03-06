import allRooms from './views/allRooms.vue'
import room from './views/room.vue'
import addProperty from './views/addRoom.vue'
import login from './views/login.vue'
import register from "./views/register"
import home from "./views/home.vue"
import changeProperty from './views/changeProperty.vue'
import myRooms from './views/myRooms.vue'
import user from './views/userRentRequests.vue'
import myRentObj from './views/rentRequests.vue'



export default [
  {path: "/all/:id", component: allRooms },
  {path: "/all", component: allRooms },
  {path: '/room/:id', component: room},
  {path: '/property/add', component: addProperty},
  {path: '/login', component: login},
  {path: '/register', component: register},
  {path: '/', component: home},
  {path: '/change/:id', component: changeProperty},
  {path: '/my/rooms', component: myRooms},
  {path: '/user', component: user},
  {path: '/rentout', component: myRentObj},

]
