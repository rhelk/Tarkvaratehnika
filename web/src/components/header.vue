<template>
  <header>
    <h1>{{title}}</h1>
  </header>
</template>

<script>

  export default {
    data(){
      return{
        title: 'Room'
      }
    },

  }
</script>

<style scoped>

  header{
    background: whitesmoke;
    padding: 25px;
  }
  h1{
    color: #222;
    text-align:center;
  }

</style>
