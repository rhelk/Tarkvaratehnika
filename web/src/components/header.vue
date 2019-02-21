<template>
  <nav>
    <ul>
      <li>
        <router-link to = '/' exact> Home </router-link>
      </li>
    </ul>
  </nav>
</template>

<script>

  export default {

  }
</script>

<style scoped>

  ul{
    list-style-type: none;
    text-align: right;
    margin: 0;
  }
  li{
    display: inline-block;
    margin: 0 10px;
  }
  a{
    color: black;
    text-decoration: none;
    padding: 6px 8px;
  }
  nav{
    background: whitesmoke;
    padding: 35px;
  }

</style>
