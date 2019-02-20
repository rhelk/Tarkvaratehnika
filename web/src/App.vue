<template>
  <div>
    <app-header></app-header>
    <app-room></app-room>
    <app-footer></app-footer>

  </div>
</template>

<script>

  import room from './views/room.vue';
  import header from './components/header.vue';
  import footer from './components/footer.vue';

  export default {
    components: {
      'app-room': room,
      'app-header': header,
      'app-footer': footer,
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
