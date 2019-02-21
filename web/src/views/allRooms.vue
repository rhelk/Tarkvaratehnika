<template>
  <div id="allRooms">
    <h1> Find your perfect RentDeck room</h1>
    <div v-for="room in rooms" class="oneRoom">
      <img :src="room.pic_url">
      <router-link v-bind:to="/room/ + room.property_id"><h2>{{room.title}}</h2></router-link>
    </div>
  </div>
</template>

<script>
  export default {
    data() {
      return {
        rooms: []

      }
    },
    created() {
      this.$http.get('http://localhost:8080/api/properties').then(function (data) {
        this.rooms = data.body;
      })
    }
  }
</script>

<style scoped>
  #allRooms {
    max-width: 1000px;
    margin: 0 auto;
  }
  .oneRoom{
    padding: 20px;
    margin: 20px 0;
    box-sizing: border-box;
    background: whitesmoke;
    border-radius: 20px;
  }
  .oneRoom img{
   width: 20%;
    float: right;
  }

  a{
    color: black;
    text-decoration: none;
    padding: 6px 8px;
  }

</style>
