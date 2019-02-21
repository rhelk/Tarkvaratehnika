import allRooms from './views/allRooms.vue'
import room from './views/room.vue'

export default [
  {path: "/", component: allRooms },
  {path: '/room/:id', component: room}
]
