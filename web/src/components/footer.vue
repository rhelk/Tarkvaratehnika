<template>
  <footer>
    <p>{{copyright}}</p>
  </footer>
</template>

<script>
    export default {
      data() {
        return {
          copyright: 'Copyright 2019 RentDeck'
        }
      }
    }
</script>

<style scoped>
  footer{
    background: white;
    padding: 20px;
    margin: 20px;
  }
  p{
    color: darkgray;
    text-align: left;
  }

</style>
