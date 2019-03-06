<template>
  <div>
    <app-header></app-header>
    <router-view></router-view>
    <app-footer></app-footer>

  </div>
</template>

<script>

  import room from './views/room.vue';
  import allRooms from './views/allRooms.vue';
  import addRooms from './views/addRoom.vue';
  import header from './components/header.vue';
  import footer from './components/footer.vue';

  export default {
    components: {
      'app-room': room,
      'app-header': header,
      'app-footer': footer,
      'all-rooms': allRooms,
      'add-room': addRooms,
    },

  }
</script>

<style>
  h1{
    font-family: Arial;
  }
  p{
    font-family: Arial;
  }

</style>
